import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;

public class UnknownHostMailRemovalTool {

	private final Config config;

	/**
	 * ドメイン名のキャッシュ
	 * (Key - ドメイン名, Value - 有効なドメイン名なら true, 無効なドメイン名なら false)
	 */
	private final Map<String, Boolean> domainCache = new HashMap<String, Boolean>();

	public static void main(String[] args) throws MessagingException, NamingException, IOException {
		Path path =
			args.length > 0
				? Paths.get(args[0])
				: Paths.get("Config.properties");

		new UnknownHostMailRemovalTool(new Config(path)).process();
	}

	private UnknownHostMailRemovalTool(Config config) {
		this.config = config;

		for (String ignoreDomain : config.getIgnoreDomains()) {
			domainCache.put(ignoreDomain, true);
		}
	}

	private void process() throws MessagingException, NamingException, IOException {
		Session session = config.getSession();
		// session.setDebug(true);

		try (AutoCloseableStore store = new AutoCloseableStore(session.getStore("pop3"))) {
			store.connect();

			try (AutoCloseableFolder folder = new AutoCloseableFolder(store.getFolder("INBOX"))) {
				folder.open(Folder.READ_WRITE);

				int start = config.getStartMessageNumber();
				int count = folder.getMessageCount();
				for (int i = start; i <= count; i++) {
					Message message = folder.getMessage(i);

					try {
						for (Address from : message.getFrom()) {
							String address = ((InternetAddress) from).getAddress().toString();
							if (address.contains("@")) {
								String domain = address.split("@")[1];

								if (!isValidDomain(domain)) {
									System.out.format("%d: %s [%s]\n",
										message.getMessageNumber(), message.getSubject(), address);

									message.setFlag(Flags.Flag.DELETED, true);
								}
							}
						}
					} catch (AddressException e) {
						System.err.println(message.getMessageNumber() + ": " + e.getMessage());
					}
				}

				Answer answer = confirm("Are you sure you want to permanently delete? [y/n]");
				if (answer == Answer.YES) {
					folder.setExpunge(true);
				}
			}
		}
	}

	private boolean isValidDomain(String domain) throws NamingException {
		return domainCache.computeIfAbsent(domain, d -> {
			try {
				Properties props = new Properties();
				props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
				props.put(Context.PROVIDER_URL, "dns:");

				InitialDirContext context = new InitialDirContext(props);
				context.getAttributes(domain, new String[] { "MX" });

				return true;
			} catch (NameNotFoundException e) {
				return false;
			} catch (NamingException e) {
				throw new UncheckedNamingException(e);
			}
		});
	}

	private Answer confirm(String message) throws IOException {
		while (true) {
			System.out.println(message);

			String input =
				new BufferedReader(new InputStreamReader(System.in)).readLine();

			if (input.equalsIgnoreCase("y")) {
				return Answer.YES;
			} else if (input.equalsIgnoreCase("n")) {
				return Answer.NO;
			}
		}
	}

	private enum Answer {
		YES,
		NO
	}
}

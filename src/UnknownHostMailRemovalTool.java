import java.io.IOException;
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
import javax.mail.Store;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;

public class UnknownHostMailRemovalTool {

	/**
	 * ドメイン名のキャッシュ
	 * (Key - ドメイン名, Value - 有効なドメイン名なら true, 無効なドメイン名なら false)
	 */
	private Map<String, Boolean> domainCache = new HashMap<String, Boolean>();

	public static void main(String[] args) throws MessagingException, NamingException, IOException {
		Path path =
			args.length > 0
				? Paths.get(args[0])
				: Paths.get("Config.properties");

		Config config = new Config(path);

		Session session =
			Session.getInstance(config.getProperties(), config.getAuthenticator());
		// session.setDebug(true);

		new UnknownHostMailRemovalTool().process(session);
	}

	private void process(Session session) throws MessagingException, NamingException {
		try (AutoCloseableStore closeableStore =
			new AutoCloseableStore(session.getStore("pop3"))) {

			Store store = closeableStore.get();
			store.connect();

			try (AutoCloseableFolder closeableFolder =
				new AutoCloseableFolder(store.getFolder("INBOX"))) {

				Folder folder = closeableFolder.get();
				folder.open(Folder.READ_ONLY);

				for (Message message : folder.getMessages()) {
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

				while (true) {
					System.out.println("Are you sure you want to permanently delete? [y/n]");
					String input = System.console().readLine();

					if (input.equalsIgnoreCase("y")) {
						folder.expunge();
						break;
					} else if (input.equalsIgnoreCase("n")) {
						break;
					}
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
}

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;

public class UnknownHostMailRemovalTool {

	public static void main(String[] args) throws MessagingException, NamingException {
		if (args.length != 3) {
			System.err.println("Invalid arguments.");
			System.exit(1);
			return;
		}

		String host = args[0];
		String userName = args[1];
		String password = args[2];

		Properties props = new Properties();
		props.setProperty("mail.pop3.host", host);
		props.setProperty("mail.pop3.port", "110");
		props.setProperty("mail.pop3.connectiontimeout", "600000");
		props.setProperty("mail.pop3.timeout", "600000");

		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, password);
			}
		});
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

				int count = 0;
				for (Message message : folder.getMessages()) {
					boolean isValid = false;
					for (Address from : message.getFrom()) {
						String address = ((InternetAddress) from).getAddress().toString();
						if (address.contains("@")) {
							String domain = address.split("@")[1];

							if (isValidDomain(domain)) {
								System.out.format("'%s' is valid.\n", domain);
							} else {
								System.out.format("'%s' is invalid.\n", domain);
							}
						}
					}

					// TODO: ERASE
					if (count > 10) {
						break;
					}
				}
			}
		}
	}

	private boolean isValidDomain(String domain) throws NamingException {
		try {
			Properties props = new Properties();
			props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
			props.put(Context.PROVIDER_URL, "dns:");

			new InitialDirContext(props).getAttributes(domain);

			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}
}

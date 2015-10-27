import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

public class Config {

	private final Properties props;
	private final String username;
	private final String password;
	private final String[] ignoreDomains;

	public Config(Path configFile) throws IOException {
		this.props = new Properties();
		this.props.load(Files.newBufferedReader(configFile));

		this.username = props.getProperty("mail.username");
		this.password = props.getProperty("mail.password");
		this.ignoreDomains = props.getProperty("mail.ignore.domain", "").split(";");
	}

	public Properties getProperties() {
		return props;
	}

	public String[] getIgnoreDomains() {
		return Arrays.copyOf(ignoreDomains, ignoreDomains.length);
	}

	public Session getSession() {
		return Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
	}
}

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class Config {

	private final Properties props;
	private final String username;
	private final String password;

	public Config(Path configFile) throws IOException {
		this.props = new Properties();
		this.props.load(Files.newBufferedReader(configFile));

		this.username = props.getProperty("mail.username");
		this.password = props.getProperty("mail.password");
	}

	public Properties getProperties() {
		return props;
	}

	public Authenticator getAuthenticator() {
		return new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		};
	}
}

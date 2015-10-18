import javax.mail.Folder;
import javax.mail.MessagingException;

public class AutoCloseableFolder implements AutoCloseable {

	private final Folder folder;

	public AutoCloseableFolder(Folder folder) {
		this.folder = folder;
	}

	public Folder get() {
		return folder;
	}

	@Override
	public void close() throws MessagingException {
		folder.close(false);
	}
}

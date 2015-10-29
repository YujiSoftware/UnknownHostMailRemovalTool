import javax.mail.Folder;
import javax.mail.MessagingException;

public class AutoCloseableFolder implements AutoCloseable {

	private final Folder folder;

	private boolean expunge = false;

	public AutoCloseableFolder(Folder folder) {
		this.folder = folder;
	}

	public Folder get() {
		return folder;
	}

	public void setExpunge(boolean expunge) {
		this.expunge = expunge;
	}

	@Override
	public void close() throws MessagingException {
		folder.close(expunge);
	}
}

import javax.mail.MessagingException;
import javax.mail.Store;

public class AutoCloseableStore implements AutoCloseable {

	private final Store store;

	public AutoCloseableStore(Store store) {
		this.store = store;
	}

	public Store get() {
		return store;
	}

	@Override
	public void close() throws MessagingException {
		store.close();
	}

}

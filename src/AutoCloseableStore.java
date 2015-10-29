import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.event.ConnectionListener;
import javax.mail.event.FolderListener;
import javax.mail.event.StoreListener;

public class AutoCloseableStore implements AutoCloseable {

	private final Store store;

	public AutoCloseableStore(Store store) {
		this.store = store;
	}

	public int hashCode() {
		return store.hashCode();
	}

	public Folder getDefaultFolder() throws MessagingException {
		return store.getDefaultFolder();
	}

	public Folder getFolder(String name) throws MessagingException {
		return store.getFolder(name);
	}

	public boolean equals(Object obj) {
		return store.equals(obj);
	}

	public Folder getFolder(URLName url) throws MessagingException {
		return store.getFolder(url);
	}

	public Folder[] getPersonalNamespaces() throws MessagingException {
		return store.getPersonalNamespaces();
	}

	public void connect() throws MessagingException {
		store.connect();
	}

	public Folder[] getUserNamespaces(String user) throws MessagingException {
		return store.getUserNamespaces(user);
	}

	public void connect(String host, String user, String password) throws MessagingException {
		store.connect(host, user, password);
	}

	public Folder[] getSharedNamespaces() throws MessagingException {
		return store.getSharedNamespaces();
	}

	public void addStoreListener(StoreListener l) {
		store.addStoreListener(l);
	}

	public void removeStoreListener(StoreListener l) {
		store.removeStoreListener(l);
	}

	public void connect(String user, String password) throws MessagingException {
		store.connect(user, password);
	}

	public void addFolderListener(FolderListener l) {
		store.addFolderListener(l);
	}

	public void removeFolderListener(FolderListener l) {
		store.removeFolderListener(l);
	}

	public void connect(String host, int port, String user, String password)
		throws MessagingException {
		store.connect(host, port, user, password);
	}

	public boolean isConnected() {
		return store.isConnected();
	}

	public URLName getURLName() {
		return store.getURLName();
	}

	public void addConnectionListener(ConnectionListener l) {
		store.addConnectionListener(l);
	}

	public void removeConnectionListener(ConnectionListener l) {
		store.removeConnectionListener(l);
	}

	public String toString() {
		return store.toString();
	}

	@Override
	public void close() throws MessagingException {
		store.close();
	}

}

import java.io.IOException;
import java.io.InputStream;

import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.event.ConnectionListener;
import javax.mail.event.FolderListener;
import javax.mail.event.MessageChangedListener;
import javax.mail.event.MessageCountListener;
import javax.mail.search.SearchTerm;

import com.sun.mail.pop3.POP3Folder;

public class AutoCloseablePOP3Folder implements AutoCloseable {

	private final POP3Folder folder;

	private boolean expunge = false;

	public AutoCloseablePOP3Folder(POP3Folder folder) {
		this.folder = folder;
	}

	public int hashCode() {
		return folder.hashCode();
	}

	public boolean equals(Object obj) {
		return folder.equals(obj);
	}

	public String getName() {
		return folder.getName();
	}

	public String getFullName() {
		return folder.getFullName();
	}

	public Folder getParent() {
		return folder.getParent();
	}

	public boolean exists() {
		return folder.exists();
	}

	public Folder[] list(String pattern) throws MessagingException {
		return folder.list(pattern);
	}

	public char getSeparator() {
		return folder.getSeparator();
	}

	public int getType() {
		return folder.getType();
	}

	public boolean create(int type) throws MessagingException {
		return folder.create(type);
	}

	public boolean hasNewMessages() throws MessagingException {
		return folder.hasNewMessages();
	}

	public Folder getFolder(String name) throws MessagingException {
		return folder.getFolder(name);
	}

	public boolean delete(boolean recurse) throws MessagingException {
		return folder.delete(recurse);
	}

	public boolean renameTo(Folder f) throws MessagingException {
		return folder.renameTo(f);
	}

	public void open(int mode) throws MessagingException {
		folder.open(mode);
	}

	public void close(boolean expunge) throws MessagingException {
		folder.close(expunge);
	}

	public URLName getURLName() throws MessagingException {
		return folder.getURLName();
	}

	public Store getStore() {
		return folder.getStore();
	}

	public boolean isOpen() {
		return folder.isOpen();
	}

	public Flags getPermanentFlags() {
		return folder.getPermanentFlags();
	}

	public int getMessageCount() throws MessagingException {
		return folder.getMessageCount();
	}

	public Message getMessage(int msgno) throws MessagingException {
		return folder.getMessage(msgno);
	}

	public void appendMessages(Message[] msgs) throws MessagingException {
		folder.appendMessages(msgs);
	}

	public Message[] expunge() throws MessagingException {
		return folder.expunge();
	}

	public void fetch(Message[] msgs, FetchProfile fp) throws MessagingException {
		folder.fetch(msgs, fp);
	}

	public Folder[] listSubscribed(String pattern) throws MessagingException {
		return folder.listSubscribed(pattern);
	}

	public Folder[] list() throws MessagingException {
		return folder.list();
	}

	public String getUID(Message msg) throws MessagingException {
		return folder.getUID(msg);
	}

	public Folder[] listSubscribed() throws MessagingException {
		return folder.listSubscribed();
	}

	public int getSize() throws MessagingException {
		return folder.getSize();
	}

	public int[] getSizes() throws MessagingException {
		return folder.getSizes();
	}

	public InputStream listCommand() throws MessagingException, IOException {
		return folder.listCommand();
	}

	public boolean isSubscribed() {
		return folder.isSubscribed();
	}

	public void setSubscribed(boolean subscribe) throws MessagingException {
		folder.setSubscribed(subscribe);
	}

	public int getMode() {
		return folder.getMode();
	}

	public int getNewMessageCount() throws MessagingException {
		return folder.getNewMessageCount();
	}

	public int getUnreadMessageCount() throws MessagingException {
		return folder.getUnreadMessageCount();
	}

	public int getDeletedMessageCount() throws MessagingException {
		return folder.getDeletedMessageCount();
	}

	public Message[] getMessages(int start, int end) throws MessagingException {
		return folder.getMessages(start, end);
	}

	public Message[] getMessages(int[] msgnums) throws MessagingException {
		return folder.getMessages(msgnums);
	}

	public Message[] getMessages() throws MessagingException {
		return folder.getMessages();
	}

	public void setFlags(Message[] msgs, Flags flag, boolean value) throws MessagingException {
		folder.setFlags(msgs, flag, value);
	}

	public void setFlags(int start, int end, Flags flag, boolean value) throws MessagingException {
		folder.setFlags(start, end, flag, value);
	}

	public void setFlags(int[] msgnums, Flags flag, boolean value) throws MessagingException {
		folder.setFlags(msgnums, flag, value);
	}

	public void copyMessages(Message[] msgs, Folder folder) throws MessagingException {
		folder.copyMessages(msgs, folder);
	}

	public Message[] search(SearchTerm term) throws MessagingException {
		return folder.search(term);
	}

	public Message[] search(SearchTerm term, Message[] msgs) throws MessagingException {
		return folder.search(term, msgs);
	}

	public void addConnectionListener(ConnectionListener l) {
		folder.addConnectionListener(l);
	}

	public void removeConnectionListener(ConnectionListener l) {
		folder.removeConnectionListener(l);
	}

	public void addFolderListener(FolderListener l) {
		folder.addFolderListener(l);
	}

	public void removeFolderListener(FolderListener l) {
		folder.removeFolderListener(l);
	}

	public void addMessageCountListener(MessageCountListener l) {
		folder.addMessageCountListener(l);
	}

	public void removeMessageCountListener(MessageCountListener l) {
		folder.removeMessageCountListener(l);
	}

	public void addMessageChangedListener(MessageChangedListener l) {
		folder.addMessageChangedListener(l);
	}

	public void removeMessageChangedListener(MessageChangedListener l) {
		folder.removeMessageChangedListener(l);
	}

	public String toString() {
		return folder.toString();
	}

	public void setExpunge(boolean expunge) {
		this.expunge = expunge;
	}

	@Override
	public void close() throws MessagingException {
		folder.close(expunge);
	}
}

import java.util.Objects;

import javax.naming.NamingException;

public class UncheckedNamingException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UncheckedNamingException(String message, NamingException cause) {
		super(message, Objects.requireNonNull(cause));
	}

	public UncheckedNamingException(NamingException cause) {
		super(Objects.requireNonNull(cause));
	}

	@Override
	public NamingException getCause() {
		return (NamingException) super.getCause();
	}
}

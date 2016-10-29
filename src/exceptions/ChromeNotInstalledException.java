package exceptions;

import java.io.IOException;

public class ChromeNotInstalledException extends IOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8057778371839072577L;

	public ChromeNotInstalledException(final String message) {
		super(message);
	}
	
}

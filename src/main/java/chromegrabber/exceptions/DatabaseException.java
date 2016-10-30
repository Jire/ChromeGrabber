package chromegrabber.exceptions;

import java.sql.SQLException;

public class DatabaseException extends SQLException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1845773185335215452L;
	
	public DatabaseException(final String message) {
		
		super(message);
		
	}

}

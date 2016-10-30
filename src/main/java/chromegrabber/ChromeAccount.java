package chromegrabber;

import javafx.beans.property.SimpleStringProperty;

public final class ChromeAccount {

	private static final String DEFAULT_PROPERTY_VALUE = "";

	private final SimpleStringProperty url = new SimpleStringProperty(DEFAULT_PROPERTY_VALUE);
	private final SimpleStringProperty username = new SimpleStringProperty(DEFAULT_PROPERTY_VALUE);
	private final SimpleStringProperty password = new SimpleStringProperty(DEFAULT_PROPERTY_VALUE);

	public ChromeAccount(String Url, String username, String password) {
		setUrl(Url);
		setUsername(username);
		setPassword(password);
	}

	public ChromeAccount() {
		this(DEFAULT_PROPERTY_VALUE, DEFAULT_PROPERTY_VALUE, DEFAULT_PROPERTY_VALUE);
	}

	public String getUrl() {
		return url.get();
	}

	public String getUsername() {
		return username.get();
	}

	public String getPassword() {
		return password.get();
	}

	public void setUrl(String iUrl) {
		url.set(iUrl);
	}

	public void setUsername(String iUsername) {
		username.set(iUsername);
	}

	public void setPassword(String iPassword) {
		password.set(iPassword);
	}

}

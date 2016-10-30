package chromegrabber;
import javafx.beans.property.SimpleStringProperty;

public class ChromeAccount {
	
	private final SimpleStringProperty Url = new SimpleStringProperty("");
	private final SimpleStringProperty username = new SimpleStringProperty("");
	private final SimpleStringProperty password = new SimpleStringProperty("");
	
	public ChromeAccount(final String Url, final String username, final String password) {
		
		setUrl(Url);
		setUsername(username);
		setPassword(password);
		
	}
	
	public ChromeAccount() {
		this("","","");
	}
	
	public String getUrl() {
		return Url.get();
	}
	
	public String getUsername() {
		return username.get();
	}

	public String getPassword() {
		return password.get();
	}
	
	public void setUrl(String iUrl) {
		Url.set(iUrl);
	}
	
	public void setUsername(String iUsername) {
		username.set(iUsername);
	}
	
	public void setPassword(String iPassword) {
		password.set(iPassword);
	}

}

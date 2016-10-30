package chromegrabber;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import chromegrabber.exceptions.ChromeNotInstalledException;
import chromegrabber.exceptions.DatabaseException;
import javafx.collections.ObservableList;

public final class ChromeGrabberModel {

	private final String CHROME_PATH = System.getProperty("user.home")
			+ File.separator
			+ "AppData\\Local\\Google\\Chrome\\User Data\\";

	private final Map<String, List<ChromeAccount>> chromeAccounts = new HashMap<>();
	private final List<String> profileNames = new ArrayList<>();

	public void onGrab() throws IOException, DatabaseException {

		//  Needed fields
		Path chromeInstall = Paths.get(CHROME_PATH);
		File chromeInfo = new File(chromeInstall.toString(), "Local State");

		// Throw exception if Google Chrome installation cannot be found
		if (Files.notExists(chromeInstall)) {
			try {
				throw new ChromeNotInstalledException("Cannot find Chrome installation!");
			} catch (ChromeNotInstalledException e) {
				e.printStackTrace();
			}
		}

		// Gather all Google Chrome info so we can extract the profile name, need to fix this to just receive a String
		String[] infoLines = Files.readAllLines(
				Paths.get(chromeInfo.toURI())).toArray(new String[]{});

		// List of returned profiles
		List<ChromeProfile> profiles = readProfiles(infoLines);

		// For each profile, find the login data database and then add the resulting ChromeAccount List along with profile name to HashMap
		for (ChromeProfile profile : profiles) {
			File loginData = new File(chromeInstall.toString() + File.separator + profile.getPath(), "Login Data");
			chromeAccounts.put(profile.getName(), readDatabase(loginData));
		}

		// Profile names stored in their own List, to be displayed to the user via the Controller
		for (ChromeProfile profile : profiles) {
			profileNames.add(profile.getName());
		}
	}

	public List<String> getProfileNames() {
		return profileNames;
	}

	public Map<String, List<ChromeAccount>> getChromeAccounts() {
		return chromeAccounts;
	}

	public void refreshStructures() {
		chromeAccounts.clear();
		profileNames.clear();
	}

	public static List<ChromeProfile> readProfiles(String[] infoLines) throws IOException {

		// List of ChromeProfile Objects to be returned
		List<ChromeProfile> profiles = new ArrayList<>();

		// Reference to this string needed outside of for loop
		String profilesString = "";

		// Get rid of loop later but it removes unnecessary noise from 'infoLines'
		for (int i = 0; i < infoLines.length; i++) {
			int begin_index = infoLines[i].indexOf(",\"profile\":{\"info_cache\":{") + 27;
			int end_index = infoLines[i].indexOf(",\"last_active_profiles\":") - 2;
			profilesString = infoLines[i].substring(begin_index, end_index);
		}

		int numberOfProfiles = 0;

		// This pattern of text appears at the start of the description of a new Profile
		Pattern p = Pattern.compile(",\"Profile ");
		Matcher m = p.matcher(profilesString);

		while (m.find()) {
			numberOfProfiles += 1;
		}

		// Default profile doesn't match the string Pattern, will fix later to deal with no accounts at all
		numberOfProfiles += 1;

		// Array of Strings which each relate specifically to one Google Chrome Profile
		List<String> profileInfo = new ArrayList<>();

		// Splits the 'profilesString' String so that each profile has it's own String which is then added to 'profileInfo'
		if (numberOfProfiles == 2) {
			profileInfo.add(profilesString.substring(0, profilesString.indexOf(",\"Profile ")));
			profileInfo.add(profilesString.substring(profilesString.indexOf(",\"Profile ")));
		} else if (numberOfProfiles > 2) {
			List<Integer> indexList = new ArrayList<>();
			profileInfo.add(profilesString.substring(0, profilesString.indexOf(",\"Profile ")));
			for (int index = profilesString.indexOf(",\"Profile "); index >= 0; index = profilesString.indexOf(",\"Profile ", index + 1)) {
				indexList.add(index);
			}
			for (int i = 0; i < indexList.size() - 1; i++) {
				profileInfo.add(profilesString.substring(indexList.get(i), indexList.get(i) + 1));
			}
		} else {
			profileInfo.add(profilesString);
		}

		// For each profile in the List, trim the String to get the most meaningful profile name and then add it along with it's ID to 'profiles'
		for (int i = 0; i < profileInfo.size(); i++) {
			int firstIndex = profileInfo.get(i).indexOf(",\"user_name\":") + 14;
			int lastIndex = profileInfo.get(i).indexOf("\"", firstIndex);
			profiles.add(new ChromeProfile(i, (profileInfo.get(i)).substring(firstIndex, lastIndex)));
		}

		return profiles;
	}

	/**
	 * Uses the ChromeDatabase class to gather URL's, usernames and passwords,
	 * wrap the data as a collection of ChromeAccount's and then returns
	 */
	public static ObservableList<ChromeAccount> readDatabase(final File data) throws DatabaseException {
		ChromeDatabase db = ChromeDatabase.connect(data);
		ObservableList<ChromeAccount> accounts = db.selectAccounts();
		db.close();

		return accounts;
	}

}

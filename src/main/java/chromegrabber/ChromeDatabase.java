package chromegrabber;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteConfig.TransactionMode;

import chromegrabber.exceptions.DatabaseConnectionException;
import chromegrabber.exceptions.DatabaseException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public final class ChromeDatabase {

	private final Connection connection;

	public static ChromeDatabase connect(File database) throws DatabaseConnectionException {

		Path tempDB;

		try {
			tempDB = Files.createTempFile("CHROME_LOGIN_", null);
			FileOutputStream out = new FileOutputStream(tempDB.toFile());
			Files.copy(Paths.get(database.getPath()), out);
			out.close();
			tempDB.toFile().deleteOnExit();
		} catch (IOException ex) {
			throw new DatabaseConnectionException("Error copying database: " + ex);
		}

		Connection db;

		try {
			SQLiteConfig config = new SQLiteConfig();
			config.setReadOnly(true);

			config.setTransactionMode(TransactionMode.EXCLUSIVE);
			db = config.createConnection("jdbc:sqlite:" + tempDB);
			db.setAutoCommit(true);
		} catch (SQLException ex) {
			throw new DatabaseConnectionException("Error connecting to database: " + ex);
		}

		return new ChromeDatabase(db);
	}

	private ChromeDatabase(Connection connection) {
		this.connection = connection;
	}

	public void close() {
		try {
			connection.close();
		} catch (SQLException ex) {
			System.err.println("Error closing connection: " + ex);
		}
	}

	public ObservableList<ChromeAccount> selectAccounts() throws DatabaseException {

		// Make sure we're connected
		try {
			if (connection.isClosed()) {
				throw new DatabaseException("Database connection not opened!");
			}
		} catch (SQLException ex) {
			throw new DatabaseException("Error checking the state of database connection: " + ex);
		}

		// Store each account in the List
		ObservableList<ChromeAccount> accounts = FXCollections.observableArrayList();

		try {
			// Temporary ResultSet to get data from database
			ResultSet results = connection
					.createStatement()
					.executeQuery("SELECT action_url, username_value, password_value FROM logins");

			while (results.next()) {
				String url, username, password;
				try {
					url = results.getString("action_url");
					username = results.getString("username_value");
					password = ChromeSecurity.getChromePasswords(results.getBytes("password_value"));
					accounts.add(new ChromeAccount(url, username, password));
				} catch (DatabaseException ex) {
					throw new DatabaseException("Error processing Chrome database: " + ex);
				}
			}

			results.close();
			results.getStatement().close();
		} catch (SQLException ex) {
			throw new DatabaseException("Error reading database, is it corrupted? " + ex);
		}

		return accounts;
	}

}

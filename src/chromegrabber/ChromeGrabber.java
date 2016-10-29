package chromegrabber;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import exceptions.DatabaseException;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ChromeGrabber {
	
	private ChromeGrabberModel model;
	
	// Provides access to all FXML elements
	@FXML private TableView<ChromeAccount> valuesTable;
	@FXML private TableColumn<ChromeAccount, String> urlColumn;
	@FXML private TableColumn<ChromeAccount, String> userColumn;
	@FXML private TableColumn<ChromeAccount, String> passColumn;
	@FXML private Label textLabel;
	@FXML private Label accountLabel;
	
	public ChromeGrabber(ChromeGrabberModel model) {
		this.model = model;
	}
	
	@FXML
	public void onDetectClick() throws IOException, DatabaseException {
		
		// Delegate most logic to model, deal with UI elements only
		model.onGrab();
		
		ChoiceDialog<String> dialog = new ChoiceDialog<>(model.getProfileNames().get(0), model.getProfileNames());
		dialog.setTitle("Choose Profile");
		dialog.setHeaderText(model.getProfileNames().size() + " profiles found!");
		dialog.setContentText("Choose your profile:");
		dialog.setGraphic(new ImageView(new Image("/user.png")));
		
		Optional<String> result = dialog.showAndWait();
		String chosenName = "";
		if(result.isPresent()) {
			chosenName = result.get();
		}
		
		// Links with FXML so that when data is added to this ObservableList the TableView gets updated
		ObservableList<ChromeAccount> data = valuesTable.getItems();
		
		// Removes previous accounts if loading a different profile
		data.clear();
		
		List<ChromeAccount> newData = model.getChromeAccounts().get(chosenName.replaceAll("\\[", "").replaceAll("\\]", ""));
		
		for(ChromeAccount account : newData) {
			
			data.add(new ChromeAccount(account.getUsername(), account.getUrl(), account.getPassword()));
			
		}
	
		accountLabel.setText("Account Name: " + (model.getChromeAccounts().keySet().toString()).replaceAll("\\[", "").replaceAll("\\]", ""));
		textLabel.setText(valuesTable.getItems().size() + " saved Google Chrome Accounts found!");
		
		model.refreshStructures();
	}
	
	@FXML
	private void initialize() {
		// On execution set each TableColumn to dynamically fill 1/3 of the TableView
		urlColumn.prefWidthProperty().bind(valuesTable.widthProperty().divide(3));
		userColumn.prefWidthProperty().bind(valuesTable.widthProperty().divide(3));
		passColumn.prefWidthProperty().bind(valuesTable.widthProperty().divide(3));
	}

}

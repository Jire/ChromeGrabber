package chromegrabber;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public final class ApplicationUI extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Interface.fxml"));
		loader.setControllerFactory(t -> new ChromeGrabber(new ChromeGrabberModel()));

		primaryStage.setTitle("Chrome Password Grabber");
		primaryStage.setScene(new Scene(loader.load()));
		primaryStage.getIcons().add(new Image("/chrome.png"));
		primaryStage.show();
	}

}

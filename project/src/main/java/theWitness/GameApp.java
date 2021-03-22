package theWitness;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class GameApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent parent = FXMLLoader.load(getClass().getResource("Game.fxml"));
		primaryStage.setTitle("The Witness");
		primaryStage.getIcons().add(new Image("file:icon.png"));
		Scene scene = new Scene(parent);
		scene.getStylesheets().add(getClass().getResource("GameStyle.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(GameApp.class, args);
	}
}

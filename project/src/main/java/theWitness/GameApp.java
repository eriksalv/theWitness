package theWitness;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

//import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
//import javafx.scene.effect.ImageInput;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class GameApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent parent = FXMLLoader.load(getClass().getResource("Game.fxml"));
		primaryStage.setTitle("The Witness");
		
//		Image image = null;
//		URL imageURL = getClass().getResource("/images/theWitness.ico");
//		image = ImageIO.read();
		
		//System.out.println(getFileFromResource("images/icon.jpg").getAbsolutePath());
		
		//Image icon = ImageIO.read(getFileFromResource("/images/icon.jpg"));
		primaryStage.getIcons().add(new Image("file:" + getFileFromResource("images/icon.jpg").getAbsolutePath()));
		Scene scene = new Scene(parent);
		scene.getStylesheets().add(getClass().getResource("GameStyle.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	private File getFileFromResource(String fileName) throws URISyntaxException { //henter filer fra src/main/resources
		ClassLoader classLoader = getClass().getClassLoader();
	    URL resource = classLoader.getResource(fileName);
	    if (resource == null) {
	        throw new IllegalArgumentException("file not found! " + fileName);
	    } else {
	    		// failed if files have whitespaces or special characters
	            //return new File(resource.getFile());
          return new File(resource.toURI());
	    }

	}

	public static void main(String[] args) {
		launch(GameApp.class, args);
	}
}

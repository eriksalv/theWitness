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
		
		//Midlertidig!!!!!
		
//		FXMLLoader loader = new FXMLLoader();
//		loader.setLocation(getClass().getResource("Game.fxml"));
//		
//		Parent parent = loader.load();
//		
//		GameController controller = loader.getController();
//		controller.initData(new GameCollection("nice", new Game(5,5), new Game(8,8)));
//	        
//	    Scene tableViewScene = new Scene(parent);
//	    primaryStage.setTitle("The Witness");
//		
//		primaryStage.getIcons().add(new Image("file:" + getFileFromResource("images/icon.jpg").getAbsolutePath()));
//		Scene scene = new Scene(parent);
//		scene.getStylesheets().add(getClass().getResource("GameStyle.css").toExternalForm());
//		
//		primaryStage.setScene(scene);
//		primaryStage.show();
		
		///////////////////////////////////////
		        
		Parent parent = FXMLLoader.load(getClass().getResource("/fxml/Main.fxml"));
		primaryStage.setTitle("The Witness");
		
		primaryStage.getIcons().add(new Image("file:" + getFileFromResource("images/icon.jpg").getAbsolutePath()));
		Scene scene = new Scene(parent);
		//scene.getStylesheets().add(getClass().getResource("GameStyle.css").toExternalForm());
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

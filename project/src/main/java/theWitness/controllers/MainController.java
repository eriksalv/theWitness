package theWitness.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import theWitness.GameCollection;

public class MainController {
	@FXML private Button continueButton;
	
	@FXML private SaveGameController saveGameController;
	
	private GameCollection games;
	
	@FXML
	private void initialize() {
		
	}
	
	public void initData(GameCollection games) {
		this.games=games;
		if (games!=null) {
			continueButton.setVisible(true); //det skal ikke gå an å trykke på continue når en fil med en gamecollection ikke er satt edna.
		}
	}
	
	@FXML
	private void openLoadView(ActionEvent e) throws IOException {
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/SaveGame.fxml"));
        Parent tableViewParent = loader.load();
        
        Scene tableViewScene = new Scene(tableViewParent);
        
        //access the controller and call a method
        SaveGameController controller = loader.getController();
        controller.setLoad();
        
        //This line gets the Stage information
        Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
        
        window.setScene(tableViewScene);
        window.show();
	}
	
	@FXML
	private void handleContinue(ActionEvent e) throws IOException {
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/Game.fxml"));
        Parent tableViewParent = loader.load();
        
        Scene tableViewScene = new Scene(tableViewParent);
        
        //access the controller and call a method
        GameController controller = loader.getController();
        controller.initData(games);
        
        //This line gets the Stage information
        Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
        
        window.setScene(tableViewScene);
        window.show();
	}
	
	@FXML
	private void handleNewGame(ActionEvent e) throws IOException {
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/Game.fxml"));
        Parent tableViewParent = loader.load();
        
        Scene tableViewScene = new Scene(tableViewParent);        
        //This line gets the Stage information
        Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
        
        window.setScene(tableViewScene);
        window.show();
	}
}

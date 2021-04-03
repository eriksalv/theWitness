package theWitness.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import theWitness.GameCollection;

public class MainController {
	
	@FXML private SaveGameController saveGameController;
	
	private GameCollection games;
	
	public void initData(GameCollection games) {
		this.games=games;
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
	private void handleNewGame(MouseEvent e) throws IOException {
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

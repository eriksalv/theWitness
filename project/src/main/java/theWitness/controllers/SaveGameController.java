package theWitness.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import theWitness.GameCollection;
import theWitness.SaveHandler;

public class SaveGameController {
	private boolean isLoad;
	
	@FXML private GameController gameController;
	@FXML private MainController mainController;
	
	@FXML
	private Pane saves;
	
	@FXML private TextField fileName;
	@FXML private DialogPane saveMessage;
	@FXML private Label closeSaveMessage;
	@FXML private Label returnToGameView;
	@FXML private Label returnToMainView;
	@FXML private Label header;
	@FXML private Label fileNotFoundMessage;
	@FXML private Button createAndSave;
	@FXML private Button load;
	
	private GameCollection toSave;
	private SaveHandler saveHandler = new SaveHandler();
	   
	@FXML
	private void initialize() {
		isLoad=false;
	}
	
	public void setLoad() { //endrer gui til å vise knapper og felt for å laste fil i stedet for å lagre
		isLoad=true;
		header.setText("Load save file");
		createAndSave.setVisible(false);
		load.setVisible(true);
		returnToGameView.setVisible(false);
		returnToMainView.setVisible(true);
	}
	
	@FXML
	private void returnToMainView(MouseEvent e) throws IOException {
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/Main.fxml"));
        Parent tableViewParent = loader.load();
        
        Scene tableViewScene = new Scene(tableViewParent);
        
        //access the controller and call a method
        MainController controller = loader.getController();
        controller.initData(toSave);
        
        //This line gets the Stage information
        Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
        
        window.setScene(tableViewScene);
        window.show();
	}
	
		
	@FXML
	private void handleOpenGameView(MouseEvent e) throws IOException {
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/Game.fxml"));
        Parent tableViewParent = loader.load();
        
        Scene tableViewScene = new Scene(tableViewParent);
        
        //access the controller and call a method
        GameController controller = loader.getController();
        controller.initData(toSave);
        
        //This line gets the Stage information
        Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
        
        window.setScene(tableViewScene);
        window.show();
	}
	
	@FXML
	public void handleCreateAndSave() throws URISyntaxException {
		try {
			System.out.println(toSave.getIsGamesWon());
    		saveHandler.save(fileName.getText(), toSave);
    		saveMessage.setVisible(true);
    		fileNotFoundMessage.setVisible(false);
    	} catch (FileNotFoundException e) {
    		fileNotFoundMessage.setVisible(true);
    		System.out.println("file not found");
    	}
	}
	
	@FXML 
	public void handleLoad(MouseEvent event) throws IOException, URISyntaxException {
		try {
			toSave=saveHandler.load(fileName.getText());
			handleOpenGameView(event);
			fileNotFoundMessage.setVisible(false);
		} catch (FileNotFoundException e) {
			fileNotFoundMessage.setVisible(true);
			System.out.println("file not found");
		}
	}
	
	@FXML
	public void handleCloseSaveMessage() {
		saveMessage.setVisible(false);
	}
	
	public void initData(GameCollection games) {
		toSave=games;
	}
}

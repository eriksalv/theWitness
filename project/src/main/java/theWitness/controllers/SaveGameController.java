package theWitness.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.NoSuchElementException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import theWitness.GameCollection;
import theWitness.SaveHandler;

public class SaveGameController {
	
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
	@FXML private Button save;
	@FXML private Button load;
	
	private GameCollection toSave;
	private SaveHandler saveHandler = new SaveHandler();
	   
	@FXML
	private void initialize() {
	}
	
	public void setLoad() { //endrer gui til å vise knapper og felt for å laste fil i stedet for å lagre
		header.setText("Load save file");
		save.setVisible(false);
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
	public void handleSave() {
		try {
			System.out.println(toSave.getIsGamesWon());
    		saveHandler.save(fileName.getText(), toSave);
    		saveMessage.setVisible(true);
    		fileNotFoundMessage.setVisible(false);
    	} catch (FileNotFoundException | NullPointerException e) {
    		fileNotFoundMessage.setText("file not found");
    		fileNotFoundMessage.setVisible(true);
    		System.out.println("file not found");
    	} catch (InvalidPathException e) {
    		fileNotFoundMessage.setText("invalid filename");
    		fileNotFoundMessage.setVisible(true);
    		System.out.println("invalid filename");
    	}
	}
	
	@FXML 
	public void handleLoad(MouseEvent event) throws IOException {
		try {
			toSave=saveHandler.load(fileName.getText());
			handleOpenGameView(event);
			fileNotFoundMessage.setVisible(false);
		} catch (FileNotFoundException | NullPointerException e) {
			fileNotFoundMessage.setText("file not found");
			fileNotFoundMessage.setVisible(true);
			System.out.println("file not found");
		} catch (NoSuchElementException e) {
			fileNotFoundMessage.setText("invalid file");
			fileNotFoundMessage.setVisible(true);
			System.out.println("invalid file");
		} catch (InvalidPathException e) {
			fileNotFoundMessage.setText("invalid filename");
			fileNotFoundMessage.setVisible(true);
			System.out.println("invalid filename");
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

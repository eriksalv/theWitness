package theWitness;

import java.io.FileNotFoundException;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class SaveGameController {	
	@FXML
	private Pane saves;
	
	@FXML
	private TextField fileName;
	
	private GameCollection toSave;
	private SaveHandler saveHandler = new SaveHandler();
	   
	@FXML
	private void initialize() {
		
	}
	
	@FXML
	public void handleCreateAndSave() {
		try {
    		saveHandler.save(fileName.getText(), toSave);
    		//fileNotFoundMessage.setVisible(false);
    	} catch (FileNotFoundException e) {
    		//fileNotFoundMessage.setVisible(true);
    	}
	}
	
	public void initData(GameCollection games) {
		toSave=games;
		System.out.println(toSave.getIsGamesWon());
	}
}

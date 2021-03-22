package theWitness;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class GameController {
	
	private Game game;
	
	@FXML
	Pane board;
	
	@FXML
	private void initialize() {
		game = new Game(5,5);
		 
		 game.getTile(1, 1).setWhite();
		 game.getTile(3, 1).setWhite();
		 game.getTile(1, 3).setWhite();
		 game.getTile(3, 3).setBlack();
		 //game.getTile(5,5).setBlack();
		 game.getTile(0, game.getHeight()-1).setStart();
		 game.getTile(game.getWidth()-1, 0).setGoal();
		 
		 createBoard();
		 drawBoard();
	}
	
	private void createBoard() {
		board.getChildren().clear();
		double tileHeight = board.getPrefHeight()/game.getHeight();
		double tileWidth= board.getPrefWidth()/game.getWidth();
		for (int y=0;y<game.getHeight();y++) {
			for (int x=0;x<game.getWidth();x++) {
				//tileHeight=getTileSize(y,x)[0];
				//tileWidth=getTileSize(y,x)[1];
				Pane tile = new Pane();
				tile.setPrefHeight(tileHeight-2);
				tile.setPrefWidth(tileWidth-2);
				tile.setTranslateX((40+x*tileWidth));
				tile.setTranslateY((15+y*tileHeight));
				/*tile.setStyle("-fx-border-color: black;\n"
		                + "-fx-border-width: 1;\n"
		                + "-fx-border-style: solid;\n"
		                + "-fx-border-radius:10px;\n"
		                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0);\n"
		                + "-fx-background-color:green;");*/
				tile.getStyleClass().add("tile");
				board.getChildren().add(tile);
			}
		}
	}
	private void drawBoard() {
		for (int y = 0; y < game.getHeight(); y++) {
			for (int x = 0; x < game.getWidth(); x++) {
				board.getChildren().get(y*game.getWidth() + x).setStyle("-fx-background-color: " + getTileColor(game.getTile(x, y))[1] + ";");
				if (game.getTile(x, y).isBlack() || game.getTile(x, y).isWhite()) {
					board.getChildren().get(y*game.getWidth() + x).setStyle("-fx-background-color: " + getTileColor(game.getTile(x, y))[1] + "; -fx-border-width: " + (board.getPrefWidth()/game.getWidth())/3.5 + ";");
				}
				board.getChildren().get(y*game.getWidth()+x).getStyleClass().add(getTileColor(game.getTile(x, y))[0]);
			}
		}
		
		/*// Oppgave 10
		if(game.isGameWon()) {
			winText.setText("Du vant!");
			winText.setStyle("-fx-font-size: 40px");
			winText.setFill(Color.GREEN);
			winText.setTranslateX(160.0);
			winText.setTranslateY(200.0);
			board.getChildren().add(winText);
		} else if(game.isGameOver()) {
			loseText.setText("Game Over");
			loseText.setStyle("-fx-font-size: 40px");
			loseText.setFill(Color.RED);
			loseText.setTranslateX(160.0);
			loseText.setTranslateY(200.0);
			board.getChildren().add(loseText);
		}*/
	}
	@FXML
	public void keyListener(KeyEvent e) {
		if (e.getCode() == KeyCode.UP) {
			handleUp();
		}
		if (e.getCode() == KeyCode.DOWN) {
			handleDown();
		}
		if (e.getCode() == KeyCode.LEFT) {
			handleLeft();
		}
		if (e.getCode() == KeyCode.RIGHT) {
			handleRight();
		}
	}
	@FXML
	public void handleUp() {
		game.moveUp();
		drawBoard();
	}
	@FXML
	public void handleDown() {
		game.moveDown();
		drawBoard();
	}
	@FXML
	public void handleLeft() {
		game.moveLeft();
		drawBoard();
	}
	@FXML
	public void handleRight() {
		game.moveRight();
		drawBoard();
	}
	private String[] getTileColor(Tile tile) {
    	if (tile.isWhite()) { 
    		//return "#1db121"; //kan rendomizes
    		//return "white";
    		String[] color = {"white", "white"};
    		return color;
    	} else if(tile.isBlack()) {
    		//return "#24d628";
    		//return "black";
    		//tile.setStyle("-fx-border-width:20;");
    		String[] color = {"black", "black"};
    		return color;
    	} else if(tile.isMovedLine() || tile.isLastMovedLine()) {
    		//return "#a26f42";
    		//return "movedLine";
    		String[] color = {"movedLine", "#00bebe"};
    		return color;
    	} else if(tile.isStart()) {
    		//return "#e5303a";
    		//return "start";
    		String[] color = {"start", "#e5303a"};
    		return color;
    	} else if(tile.isGoal()) {
    		//return "#f6ec5a";
    		//return "goal";
    		String[] color = {"goal", "#f6ec5a"};
    		return color;
    	} else {
    		//return "normal";
    		String[] color = {"normal", "#00003f"};
    		return color;
    	}
    }
	/*private double[] getTileSize(int y, int x) {
		Tile tile = game.getTile(x, y);
		if (tile.isLine() || tile.isMovedLine() || tile.isLastMovedLine() || tile.isGoal() || tile.isStart()) {
			if (tile.getX()%2==0 && tile.getY()!=0 && tile.getY()!=game.getHeight()-1) {
				double[] arr = {board.getPrefHeight()/game.getHeight(), board.getPrefWidth()/game.getWidth()*0.5};
				return arr;
			}
			double[] arr = {board.getPrefHeight()/game.getHeight()*0.5, board.getPrefWidth()/game.getWidth()};
			return arr;
		}
		else {
			double[] arr = {board.getPrefHeight()/game.getHeight(), board.getPrefWidth()/game.getWidth()};
			return arr;
		}
	}*/

}

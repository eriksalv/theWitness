package theWitness.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import theWitness.Game;
import theWitness.GameCollection;
import theWitness.Tile;

public class GameController {

	@FXML
	private SaveGameController saveGameController;
	@FXML
	private MainController mainController;

	private Game game;
	private GameCollection games = null;
	private int gameIndex;

	@FXML
	Pane board;

	@FXML
	Label level;
	@FXML
	Label hasNextGame;
	@FXML
	Label hasPrevGame;
	@FXML
	Label errorMsg;
	@FXML
	Label deleteMsg;
	// @FXML Text winText = new Text();

	@FXML
	private void initialize() throws FileNotFoundException {
		try { // default games dersom initData ikke blir kalt utenfra
			initData(GameCollection.newGame());
		} catch (FileNotFoundException | URISyntaxException e) {
			System.err.println("Config files not found");
		}
	}

	public void initData(GameCollection games) {
		this.games = games;
		updateGameIndex();
		setInitialGameState();
		createBoard();
		drawBoard();
	}

	private void updateGameIndex() {
		this.gameIndex = games.getGameIndex();
		if (gameIndex < 10) {
			board.setStyle("-fx-background-color:#3d3dd4");
			// board.getStyleClass().remove("world2");
		} else if (gameIndex < 16) {
			board.setStyle("-fx-background-color:lightgreen;");
			// board.getStyleClass().add("world2");
			// board.getStyleClass().remove("world1");
		} else {
			board.setStyle("-fx-background-color:#8E72ED;");
			// board.getStyleClass().add("world2");
			// board.getStyleClass().remove("world1");
		}
	}

	private void setNextPrevLevels() {
		if (games.hasNextLevel(gameIndex)) {
			hasNextGame.setVisible(true);
		} else {
			hasNextGame.setVisible(false);
		}
		if (games.hasPrevLevel(gameIndex)) {
			hasPrevGame.setVisible(true);
		} else {
			hasPrevGame.setVisible(false);
		}
	}

	private void setInitialGameState() {
		level.setText("Level " + gameIndex);
		setNextPrevLevels();
		errorMsg.setVisible(false);

		System.out.println(games.getIsGamesWon());
		game = games.getGames().getOrDefault(gameIndex, null);
		game.clear();
		game.setGameStart(); // må sette start og goal på nytt siden movedLine overskriver disse
		game.setGameGoal();
	}

	private void createBoard() {
		board.getChildren().clear();
		double tileHeight = board.getPrefHeight() / game.getHeight();
		double tileWidth = board.getPrefWidth() / game.getWidth();
		game.getStreamFromIterator().forEach(gameTile -> {
			// tileHeight=getTileSize(y,x)[0];
			// tileWidth=getTileSize(y,x)[1];
			Pane tile = new Pane();
			tile.setPrefHeight(tileHeight);
			tile.setPrefWidth(tileWidth);
			// tile.setMaxHeight(tileHeight);
			tile.setTranslateX((40 + gameTile.getX() * tileWidth));
			tile.setTranslateY((10 + gameTile.getY() * tileHeight));
			/*
			 * tile.setStyle("-fx-border-color: black;\n"
			 * + "-fx-border-width: 1;\n"
			 * + "-fx-border-style: solid;\n"
			 * + "-fx-border-radius:10px;\n"
			 * +
			 * "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0);\n"
			 * + "-fx-background-color:green;");
			 */
			tile.getStyleClass().add("tile");
			board.getChildren().add(tile);
		});
	}

	private void drawBoard() {
		for (int y = 0; y < game.getHeight(); y++) {
			for (int x = 0; x < game.getWidth(); x++) {
				board.getChildren().get(y * game.getWidth() + x)
						.setStyle("-fx-background-color: " + getTileColor(game.getTile(x, y))[1]);
				if (game.getTile(x, y).getIsColored()) {
					board.getChildren().get(y * game.getWidth() + x)
							.setStyle("-fx-border-color:#3d3dd4; -fx-background-color: "
									+ getTileColor(game.getTile(x, y))[1] + "; -fx-border-width: "
									+ board.getPrefHeight() / game.getHeight() / 3.5 + ";");
					if (gameIndex >= 16) {
						board.getChildren().get(y * game.getWidth() + x)
								.setStyle("-fx-border-color:#8E72ED; -fx-background-color: "
										+ getTileColor(game.getTile(x, y))[1] + "; -fx-border-width: "
										+ board.getPrefHeight() / game.getHeight() / 3.5 + ";");
					} else if (gameIndex >= 10) {
						board.getChildren().get(y * game.getWidth() + x)
								.setStyle("-fx-border-color:lightgreen; -fx-background-color: "
										+ getTileColor(game.getTile(x, y))[1] + "; -fx-border-width: "
										+ board.getPrefHeight() / game.getHeight() / 3.5 + ";");
					}
				}
				if (game.getTile(x, y).getContainsDot()) {
					board.getChildren().get(y * game.getWidth() + x)
							.setStyle("-fx-border-width: " + board.getPrefHeight() / game.getHeight() / 2.5 + ";");
					// try {
					// ImageView img = new ImageView((new
					// File("src/main/resources/images/tetrisTest.png")).toURI().toURL().toExternalForm());
					// img.setFitHeight(board.getPrefHeight()/game.getHeight());
					// img.setFitWidth(board.getPrefWidth()/game.getWidth());
					// ((Pane) board.getChildren().get(y*game.getWidth() +
					// x)).getChildren().add(img);
					// } catch (MalformedURLException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
				}
				if (game.getTile(x, y).getContainsDot()
						&& (game.getTile(x, y).isMovedLine() || game.getTile(x, y).isLastMovedLine())) {
					board.getChildren().get(y * game.getWidth() + x)
							.setStyle("-fx-border-color:#87CEEB; -fx-border-width: "
									+ board.getPrefHeight() / game.getHeight() / 2.5 + ";");
				}
				if (game.getTile(x, y).getX() == 0 && game.getTile(x, y).getY() == 0) {
					board.getChildren().get(y * game.getWidth() + x).getStyleClass().add("top-left");
				} else if (game.getTile(x, y).getX() == game.getWidth() - 1 && game.getTile(x, y).getY() == 0) {
					board.getChildren().get(y * game.getWidth() + x).getStyleClass().add("top-right");
				} else if (game.getTile(x, y).getX() == 0 && game.getTile(x, y).getY() == game.getHeight() - 1) {
					board.getChildren().get(y * game.getWidth() + x).getStyleClass().add("bottom-left");
				} else if (game.getTile(x, y).getX() == game.getWidth() - 1
						&& game.getTile(x, y).getY() == game.getHeight() - 1) {
					board.getChildren().get(y * game.getWidth() + x).getStyleClass().add("bottom-right");
				}
				board.getChildren().get(y * game.getWidth() + x).getStyleClass()
						.add(getTileColor(game.getTile(x, y))[0]);
			}
		}

		if (game.getIsGameWon()) {
			if (games.hasNextLevel(gameIndex)) {
				hasNextGame.setVisible(true);
			}
			// winText.setText("✓");
			// winText.setStyle("-fx-font-size: 100px");
			// winText.setFill(Color.GREEN);
			for (int y = 0; y < game.getHeight(); y++) {
				for (int x = 0; x < game.getWidth(); x++) {
					if (game.getTile(x, y).isMovedLine()) {
						board.getChildren().get(y * game.getWidth() + x)
								.setStyle("-fx-background-color: white; -fx-border-width:0;");
					}
				}
			}
			// winText.setStyle("-fx-background-color:blue");
			// winText.setTranslateX(board.getWidth()/2);
			// winText.setTranslateY(board.getHeight()/2);
			// board.getChildren().add(winText);
		}
		if (game.isGameOver()) {
			for (int y = 0; y < game.getHeight(); y++) {
				for (int x = 0; x < game.getWidth(); x++) {
					if (game.getTile(x, y).isMovedLine() || game.getTile(x, y).isLastMovedLine()) {
						board.getChildren().get(y * game.getWidth() + x).setStyle("-fx-background-color: #e5303a;");
					}
				}
			}
			new Timer().schedule(
					new TimerTask() {

						@Override
						public void run() {
							handleReset(); // kjører handleReset etter en liten delay
						}
					}, 300);
		}
	}

	@FXML
	public void keyListener(KeyEvent e) { // håndterer tasteklikk
		if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.W) {
			handleUp();
		}
		if (e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.S) {
			handleDown();
		}
		if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.A) {
			handleLeft();
		}
		if (e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.D) {
			handleRight();
		}
		if (e.getCode() == KeyCode.R) {
			handleReset();
		}
	}

	@FXML
	public void handleNextGame() {
		deleteMsg.setVisible(false);
		if (games.hasNextLevel(gameIndex)) { // Hvis isGameWon er true for gamet man er på
			gameIndex++;
			if (gameIndex >= 16) {
				board.setStyle("-fx-background-color:#8E72ED");
			} else if (gameIndex >= 10) {
				board.setStyle("-fx-background-color:lightgreen;");
				// board.getStyleClass().remove("wordl1");
				// board.getStyleClass().add("world2");
			}
			setInitialGameState();
			createBoard();
			drawBoard();
		}
	}

	@FXML
	public void handlePrevGame() {
		deleteMsg.setVisible(false);
		if (games.hasPrevLevel(gameIndex)) {
			gameIndex--;
			System.out.println(gameIndex);
			if (gameIndex <= 9) {
				board.setStyle("-fx-background-color:#3d3dd4");
				// board.getStyleClass().add("world1");
				// board.getStyleClass().remove("world2");
			} else if (gameIndex <= 15) {
				board.setStyle("-fx-background-color:lightgreen");
			}
			setInitialGameState();
			createBoard();
			drawBoard();
		}
	}

	@FXML
	public void handleUp() {
		try {
			game.moveUp();
			drawBoard();
		} catch (IllegalArgumentException | IllegalStateException e) {
			System.out.println(e.getMessage());
		}
	}

	@FXML
	public void handleDown() {
		try {
			game.moveDown();
			drawBoard();
		} catch (IllegalArgumentException | IllegalStateException e) {
			System.out.println(e.getMessage());
		}
	}

	@FXML
	public void handleLeft() {
		try {
			game.moveLeft();
			drawBoard();
		} catch (IllegalArgumentException | IllegalStateException e) {
			System.out.println(e.getMessage());
		}
	}

	@FXML
	public void handleRight() {
		try {
			game.moveRight();
			drawBoard();
		} catch (IllegalArgumentException | IllegalStateException e) {
			System.out.println(e.getMessage());
		}
	}

	@FXML
	public void handleReset() {
		// if (board.getChildren().contains(winText)) {
		// board.getChildren().remove(winText);
		// }
		setInitialGameState();
		drawBoard();
	}

	@FXML
	public void handleRemoveGame() {
		try {
			games.removeGame(gameIndex);
			initData(games);
			deleteMsg.setVisible(true);
		} catch (IllegalStateException e) {
			System.out.println(e.getMessage());
			errorMsg.setVisible(true);
			deleteMsg.setVisible(false);
		}
	}

	@FXML
	public void openSaveView(ActionEvent e) throws IOException { // åpner nytt vindu for lagring
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/fxml/SaveGame.fxml"));
		Parent tableViewParent = loader.load();

		Scene tableViewScene = new Scene(tableViewParent);

		// access the controller and call a method
		SaveGameController controller = loader.getController();
		controller.initData(games);

		// This line gets the Stage information
		Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();

		window.setScene(tableViewScene);
		window.show();
	}

	@FXML
	private void returnToMainView(MouseEvent e) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/fxml/Main.fxml"));
		Parent tableViewParent = loader.load();

		Scene tableViewScene = new Scene(tableViewParent);

		// access the controller and call a method
		MainController controller = loader.getController();
		controller.initData(games);

		// This line gets the Stage information
		Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();

		window.setScene(tableViewScene);
		window.show();
	}

	private String[] getTileColor(Tile tile) {
		if (tile.isWhite()) {
			// return "#1db121"; //kan rendomizes
			// return "white";
			String[] color = { "white", "white" };
			return color;
		} else if (tile.isBlack()) {
			// return "#24d628";
			// return "black";
			// tile.setStyle("-fx-border-width:20;");
			String[] color = { "black", "black" };
			return color;
		} else if (tile.isPink()) {
			String[] color = { "pink", "#ff42b7" };
			return color;
		} else if (tile.isCyan()) {
			String[] color = { "cyan", "cyan" };
			return color;
		} else if (tile.isMovedLine() || tile.isLastMovedLine()) {
			// return "#a26f42";
			// return "movedLine";
			String[] color = { "movedLine", "#87CEEB" };
			return color;
		} else if (tile.getContainsDot()) { // viktig at denne kommer etter movedLine-skjekken
			String[] color = { "dot", "purple" };
			return color;
		} else if (tile.isStart()) {
			// return "#e5303a";
			// return "start";
			String[] color = { "start", "#87CEEB" };
			return color;
		} else if (tile.isGoal()) {
			// return "#f6ec5a";
			// return "goal";
			String[] color = { "goal", "#e5303a" };
			return color;
		} else if (tile.isBlank()) {
			String[] color = { "blank", "black" };
			return color;
		} else {
			// return "normal";
			String[] color = { "normal", "#0e0e47" };
			return color;
		}
	}
	/*
	 * private double[] getTileSize(int y, int x) {
	 * Tile tile = game.getTile(x, y);
	 * if (tile.isLine() || tile.isMovedLine() || tile.isLastMovedLine() ||
	 * tile.isGoal() || tile.isStart()) {
	 * if (tile.getX()%2==0 && tile.getY()!=0 && tile.getY()!=game.getHeight()-1) {
	 * double[] arr = {board.getPrefHeight()/game.getHeight(),
	 * board.getPrefWidth()/game.getWidth()*0.5};
	 * return arr;
	 * }
	 * double[] arr = {board.getPrefHeight()/game.getHeight()*0.5,
	 * board.getPrefWidth()/game.getWidth()};
	 * return arr;
	 * }
	 * else {
	 * double[] arr = {board.getPrefHeight()/game.getHeight(),
	 * board.getPrefWidth()/game.getWidth()};
	 * return arr;
	 * }
	 * }
	 */

}

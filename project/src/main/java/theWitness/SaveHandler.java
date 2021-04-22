package theWitness;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class SaveHandler implements ISaveHandler {
	
	//public final static String SAVE_FOLDER = "src/main/resources/saves/";
	public final static String SAVE_FOLDER = "saves/";
	
	protected Path getFilePathFromResource() throws URISyntaxException { //henter filer fra src/main/resources
		return getFilePathFromResource(SAVE_FOLDER);

	}
	protected Path getFilePathFromResource(String filename) throws URISyntaxException {
		ClassLoader classLoader = getClass().getClassLoader();
	    URL resource = classLoader.getResource(filename);
	    if (resource == null) {
	        throw new IllegalArgumentException("file not found! " + filename);
	    } else {
          return (new File(resource.toURI())).toPath();
	    }
	}
	public static final String[] getSaveFiles() throws URISyntaxException {
		File toList = (new SaveHandler()).getFilePathFromResource().toFile();
		FilenameFilter filter = new FilenameFilter() { //filtrerer bort alle filer som ikke ender med .txt
	        @Override
	        public boolean accept(File f, String name) {
	            return name.endsWith(".txt");
	        }
	    };
		return toList.list(filter);
	}	
	public void save(String filename, GameCollection games) throws FileNotFoundException, URISyntaxException {
		try (PrintWriter writer = new PrintWriter(getFilePath(filename))) {
			games.getGames().keySet().forEach(level -> {
				Game game = games.getGames().getOrDefault(level, null); //finner hvilket game som hører til hver level
				writer.println(game.getWidth());
				writer.println(game.getHeight());
				game.forEach(tile -> {
					if (tile.getContainsDot()) {
						writer.print('.');
					} else {
						writer.print(tile.getType());
					}
				});
				writer.println();
				writer.println(game.getGameStart()[0]);
				writer.println(game.getGameStart()[1]);
				writer.println(game.getGameGoal()[0]);
				writer.println(game.getGameGoal()[1]);
				writer.println(games.getIsGamesWon().getOrDefault(level, null));
			});
		}

	}
	
	public GameCollection load(String filename) throws FileNotFoundException, URISyntaxException {
		try (Scanner scanner = new Scanner(new File(getFilePath(filename)))) {
			GameCollection games = new GameCollection(filename);
			while (scanner.hasNext()) {
				int width = scanner.nextInt();
				int height = scanner.nextInt();
				Game game = new Game(width, height);
				
				scanner.nextLine();
				
				String board = scanner.next();
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						char symbol = board.charAt(y * width + x);
						if (!Character.toString(symbol).matches("[-|=0]")) { //trenger ikke å sette symbol på "autogenererte" tiles
							if (symbol=='.' ) {
								game.getTile(x, y).setDot();
							} else {
								game.getTile(x, y).setType(symbol); 
							}
						}
					}
				}
				scanner.nextLine();
				
				game.setGameStart(scanner.nextInt(), scanner.nextInt());
				game.setGameGoal(scanner.nextInt(),scanner.nextInt());
				
				scanner.nextLine();
				
				games.addGame(game, scanner.nextBoolean());
			}
			return games;
		}

	}

	public static final String getFilePath(String filename) throws URISyntaxException {
		return (new SaveHandler()).getFilePathFromResource().toString() + "\\" + filename + ".txt";
	}
	
	public static void main(String[] args) throws FileNotFoundException, URISyntaxException {
//		GameCollection gc = new GameCollection("nice",new Game(5,5), new Game(10,10), new Game(4,4));
//		System.out.println(gc.getGames());
//		System.out.println(gc.getIsGamesWon());
//		SaveHandler sh = new SaveHandler();
//		sh.save("test",gc);
//		System.out.println(sh.getSaveFiles());
		System.out.println(Arrays.asList(getSaveFiles()));
//		SaveHandler s = new SaveHandler();
//		System.out.println(s.load("save_1").getIsGamesWon());
//		System.out.println(s.load("save_1").getGames());
		System.out.println(getFilePath("level15"));
	}

}

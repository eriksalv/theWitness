package theWitness;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class SaveHandler implements ISaveHandler {
	
	public final static String SAVE_FOLDER = "src/main/resources/saves/";
	private Set<String> saveFiles = new HashSet<String>();
	
	public Set<String> getSaveFiles() {
		return saveFiles;
	}	
	public void save(String filename, GameCollection games) throws FileNotFoundException {
		try (PrintWriter writer = new PrintWriter(getFilePath(filename))) {
//			List<Integer> sortedGamesList = new ArrayList<Integer>(games.getIsGamesWon().keySet());
//			Collections.sort(sortedGamesList);
			games.getGames().keySet().forEach(level -> {
				Game game = games.getGames().getOrDefault(level, null); //finner hvilket game som hører til hver level
				writer.println(game.getWidth());
				writer.println(game.getHeight());
				game.forEach(tile -> writer.print(tile.getType()));
				writer.println();
				writer.println(games.getIsGamesWon().getOrDefault(level, null));
			});
			saveFiles.add(filename);
		}

	}
	
	public void load(String filename) throws FileNotFoundException {
		try (Scanner scanner = new Scanner(new File(getFilePath(filename)))) {
			while (scanner.hasNextLine()) {
				System.out.println(scanner.nextLine());
			}
		}
	}

	private static String getFilePath(String filename) {
		return SAVE_FOLDER + filename + ".txt";
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		GameCollection gc = new GameCollection("nice",new Game(5,5), new Game(10,10), new Game(4,4));
		System.out.println(gc.getGames());
		System.out.println(gc.getIsGamesWon());
		SaveHandler sh = new SaveHandler();
		sh.save("test",gc);
		System.out.println(sh.getSaveFiles());
	}

}

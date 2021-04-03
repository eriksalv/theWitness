package theWitness;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public enum LevelEnumerator implements LevelSettings, ISaveHandler {
	
	EMPTY, INTEGRATIONTEST, LEVEL_1, LEVEL_2, LEVEL_3, LEVEL_4, LEVEL_5, LEVEL_6, LEVEL_7, LEVEL_8, LEVEL_9;
	
	public final static String SAVE_FOLDER = "src/main/resources/config/";

	@Override
	public Game startingTiles() throws FileNotFoundException{ //gjør om gamecollection til ett game, siden det bare kan være 1 level i hver level-fil.
		//this.name() refererer til navnet på enum konstanten som startingTiles() blir kalt på. I config-mappen ligger det filer med samme navn + .level
		return load(this.name() + ".level").getGames().get(1); 
	}
	
	private static String getFilePath(String filename) {
		return SAVE_FOLDER + filename + ".txt";
	}

	@Override
	public void save(String filename, GameCollection games) throws FileNotFoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public GameCollection load(String filename) throws FileNotFoundException {
		try (Scanner scanner = new Scanner(new File(getFilePath(filename)))) {
			int width = scanner.nextInt();
			int height = scanner.nextInt();
			Game game = new Game(width, height);
							
			game.setGameStart(scanner.nextInt(), scanner.nextInt());
			game.setGameGoal(scanner.nextInt(),scanner.nextInt());
			
			while (scanner.hasNext()) {
				game.getTile(scanner.nextInt(), scanner.nextInt()).setType(scanner.next().charAt(0));
			}				
			GameCollection games = new GameCollection(filename,game);
			return games;
		}
	}
	public static void main(String[] args) throws FileNotFoundException {
		System.out.println(INTEGRATIONTEST.startingTiles());
		System.out.println(INTEGRATIONTEST.ordinal());
		System.out.println(Arrays.asList(LevelEnumerator.values()));
		System.out.println(LEVEL_9.startingTiles());
	}

}

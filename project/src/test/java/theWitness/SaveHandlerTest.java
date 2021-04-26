package theWitness;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;


public class SaveHandlerTest {
	
	private Game game;
    private SaveHandler saveHandler = new SaveHandler();
    
	private void createGame() {
		game = new Game(5,5);
		game.setGameStart(0, 4);
		game.setGameGoal(4, 0);
		game.getTile(1, 1).setWhite();
		game.getTile(3, 1).setWhite();
		game.getTile(1,3).setWhite();
		game.getTile(3, 3).setBlack();
	}

	@BeforeEach
	public void setup() {
		createGame();
	}
	@Test
	public void testLoad() {
		GameCollection savedNewGame; 
		try {
			savedNewGame = saveHandler.load("testsave");
		} catch (FileNotFoundException e) {
			fail("Could not load saved file");
			return;
		}
		//game som er opprettet i createGame skal være lik gamet i test-save
		assertEquals(game.toString(), savedNewGame.getGames().get(1).toString()); 
		assertFalse(game.isGameOver() && game.getIsGameWon());
		assertFalse(savedNewGame.getIsGamesWon().get(1));
	}
	@Test
	public void testLoadNonExistingFile() {
		assertThrows(
			NullPointerException.class, 
			() -> game = saveHandler.load("foo").getGames().get(1), 
			"NullPointerException kastes når filen ikke eksisterer"
		);
	}
	@Test
	public void testLoadInvalidFile() {
		assertThrows(
			Exception.class, 
			() -> game = saveHandler.load("invalid-save").getGames().get(1), 
			"Exception kastes når innholdet i save-fil er ugyldig"
		);
	}
	@Test
	public void testSave() {
		try {
			saveHandler.save("testsavenew", new GameCollection("save-test",game));
		} catch (FileNotFoundException e) {
			fail("Could not save file");
		}
		
		byte[] testFile = null, newFile = null;
		
		try {
			testFile = Files.readAllBytes(Path.of(saveHandler.getFilePath("testsave")
					.substring(saveHandler.getFilePath("testsave").lastIndexOf(":")+1)));
			// ":" i "/C:" eller "/D:" skaper problemer for nio path
		} catch (IOException e) {
			fail("Could not load test file");
		}

		try {
			newFile = Files.readAllBytes(Path.of(saveHandler.getFilePath("testsavenew")
					.substring(saveHandler.getFilePath("testsave").lastIndexOf(":")+1)));
		} catch (IOException e) {
			fail("Could not load saved file");
		}
		assertNotNull(testFile);
		assertNotNull(newFile);
		assertTrue(Arrays.equals(testFile, newFile));

	}
	@Test
	public void testSaveToNonExistingFile() {
		assertThrows(
				NullPointerException.class,
				() -> saveHandler.save("foo", new GameCollection("file does not exists", game)),
				"NullPointerException skal kastes når man prøver å lagre spillet til en fil som ikke eksisterer"
				);
	}
	@AfterAll
	static void teardown() throws IOException, URISyntaxException {
		PrintWriter newTestSaveFile = new PrintWriter(new SaveHandler().getFilePath("testsavenew"));
		newTestSaveFile.print(""); //fjerner alt innhold i testsavenew
		newTestSaveFile.close();
	}
	
}

package theWitness;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class LevelEnumeratorTest {
	
	private Game game;
	
	private void createGame() { //lager LEVEL_4 fra enum
		game = new Game(5,5);
		game.setGameStart(0, 4);
		game.setGameGoal(4, 0);
		game.getTile(1, 1).setBlack();
		game.getTile(3, 1).setBlack();
		game.getTile(1,3).setBlack();
		game.getTile(3, 3).setWhite();
	}

	@BeforeEach
	public void setup() {
		createGame();
	}
	@Test
	@DisplayName("Tester startingTiles() og load() metodene til LevelEnumerator")
	public void testStartingTiles() {
		Game level4; 
		try {
			level4 = LevelEnumerator.LEVEL_4.startingTiles();
		} catch (FileNotFoundException | URISyntaxException e) {
			fail("Could not load config file");
			return;
		}
		assertEquals(game.toString(),level4.toString());		
	}
	@Test
	public void testSave() {
		assertThrows(
				UnsupportedOperationException.class, 
				() -> LevelEnumerator.EMPTY.save("enumSaveTest", new GameCollection("enumSaveTest",game)), 
				"UnsupportedOperation kastes når man prøver å lagre et nivå til config mappa"
			);
	}
}

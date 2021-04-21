package theWitness;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GameCollectionTest {
	
	private GameCollection games;
	private Game game1;
	private Game game2;
	
	
	private void createGames() {
		game1 = new Game(3,5);
		game1.setGameStart(0, 0);
		game1.setGameGoal(0,1);
		
		game2 = new Game(3,5);
		game2.setGameStart(0,0);
		game2.setGameGoal(1, 0);
		
		games = new GameCollection("GameCollectionTest",game1,game2);
	}
	
	@BeforeEach
	public void setup() {
		createGames();
	}
	@Test
	public void testConstructor() {
		assertTrue(games.getGames().size()==games.getIsGamesWon().size() && games.getGames().size()==2);
		
		assertEquals(games.getGames().get(1),game1);
		assertEquals(games.getGames().get(2),game2);
		assertEquals(game1.getGameCollection(),games);
		assertEquals(game2.getGameCollection(),games);
		
		assertEquals(game1.getLevel(),1);
		assertEquals(game2.getLevel(),2);
		
		assertEquals(new ArrayList<Integer>(games.getGames().keySet()),Arrays.asList(1,2));
	}
	@Test
	@DisplayName("tester at games holder styr på når et game er vunnet eller ikke, og hvilken level et game er på")
	public void testLevelsAndGameStateChanged() {
		assertEquals(games.getGameIndex(),1);
		
		assertTrue(!games.hasNextLevel(1) && !games.hasPrevLevel(1));
		assertTrue(!games.hasNextLevel(2) && games.hasPrevLevel(2));
		
		game1.moveDown(); //game1 er nå vunnet
		assertTrue(games.hasNextLevel(1) && !games.hasPrevLevel(1));
		assertEquals(games.getGameIndex(),2);
		
		game2.moveRight(); //game2 er nå vunnet
		assertFalse(games.hasNextLevel(2)); //hasNextLevel skal returnere false allikevel, siden det ikke er lagt inn et til game
		assertEquals(games.getGameIndex(),1);
	}
	@Test
	public void testNewGame() {
		GameCollection newGame = null;
		try {
			newGame = GameCollection.newGame();
		} catch (FileNotFoundException e) {
			fail("Could not load config files");
		} catch (URISyntaxException e) {
			fail("Could not load config files");
		}
		
		List<LevelEnumerator> levelList = Arrays.asList(LevelEnumerator.values());
		assertEquals(newGame.getGames().size(),levelList.size()-2); //må trekke 2 fra levelList, siden enumen bruker to ekstra konstanter som ikke skal bli levels
		assertEquals(newGame.getGameIndex(),1);
	}
	@Test
	public void testAddGame() {
		Game game3 = new Game(3,5); 
		games.addGame(game3, false);
		
		assertEquals(game3.getLevel(),3);
		assertEquals(game3.getGameCollection(),games);
		
		Game overwriteGame2 = new Game(2,2);
		games.addGame(overwriteGame2, 2); 
		assertEquals(new ArrayList<Game>(games.getGames().values()),Arrays.asList(game1,overwriteGame2,game3));
		
		assertThrows(
				IllegalStateException.class,
				() -> games.addGame(new Game(5,5), 5),
				"IllegalState skal kastes når et game med for høy level legges til i GameCollection"
		);
	}
	@Test
	public void testRemoveGame() {
		game1.moveDown(); //game1 er vunnet
		games.removeGame(1); //fjerner level 1
		assertEquals(games.getGames().get(1),game2);
		assertEquals(game2.getLevel(),1);
		assertFalse(games.getIsGamesWon().get(1));
		assertNull(game1.getGameCollection());
		
		assertThrows(
				IllegalStateException.class,
				() -> games.removeGame(2),
				"IllegalState skal kastes når et level som ikke eksisterer fjernes"
		);
		assertThrows(
				IllegalStateException.class,
				() -> games.removeGame(1),
				"IllegalState skal kastes når level som fjernes er det eneste igjen i collection"
		);
	}
}

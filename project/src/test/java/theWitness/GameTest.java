package theWitness;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GameTest {
	
	private Game game;
	
	private void createGame() {
		game = new Game(3,5);
		game.setGameGoal(2,0);
		game.setGameStart(0,4);
		game.getTile(2, 3).setDot();
		game.getTile(1, 1).setBlack();
		game.getTile(1, 3).setWhite();
	}
	@BeforeEach
	public void setup() {
		createGame();
	}
	@Test
	public void testConstructor() { //trenger ikke teste height/width siden konstruktøren til Grid brukes for dette
		assertFalse(game.isGameOver());
		assertFalse(game.getIsGameWon());
	}
	@Test 
	@DisplayName("tester moveX-metodene og rekkefølgen av trekk")
	public void testMove() {
		game.moveUp();
		game.moveUp();
		game.moveRight();
		game.moveRight();
		game.moveLeft(); //tester at å gå tilbake fungerer som det skal
		
		List<String> moveOrder = new ArrayList<String>(game.getMoves().values());  
		assertEquals(moveOrder,Arrays.asList("Up","Up","Up","Right"));
		
		List<Tile> tileOrder = new ArrayList<Tile>(game.getMoves().keySet());
		assertEquals(tileOrder,
				Arrays.asList(game.getTile(0, 4), game.getTile(0, 3), game.getTile(0, 2), game.getTile(1, 2))
		);
	}
	@Test
	public void testInvalidMove() {
		assertThrows(
				IllegalStateException.class, 
				() -> game.moveLeft(), 
				"IllegalState skal kastes når man prøver å gå inn i veggen"
			);
		
		game.moveUp();
		
		assertThrows(
				IllegalStateException.class, 
				() -> game.moveRight(), 
				"IllegalState skal kastes når man prøver å gå inn i en tile med kollisjon"
			);
		
		game.moveUp();
		game.moveRight();
		game.moveRight();
		game.moveDown();
		game.moveDown();
		game.moveLeft();
		
		assertThrows(
				IllegalStateException.class, 
				() -> game.moveLeft(), 
				"IllegalState skal kastes når man prøver å gå inn i en tile der man har gått fra før (og som ikke er lastMovedLine)"
			);
	}
	@Test
	@DisplayName("Tester resetting av game (clear())")
	public void testGameClear() {
		game.moveUp();
		game.moveUp();
		game.moveRight();
		
		assertEquals(game.getMoves().size(),4);
		
		game.clear();
		
		assertEquals(game.getMoves().size(),0);
		
		game.setGameStart();
		game.setGameGoal();
		Game game2 = game;
		createGame();
		
		assertEquals(game2.toString(),game.toString()); //game2 skal nå være lik game etter createGame()
	}
	@Test
	public void testGameOver() {
		game.moveRight();
		game.moveRight();
		game.moveUp();
		game.moveUp();
		game.moveUp();
		game.moveUp();
		
		assertTrue(game.isGameOver() && !game.getIsGameWon());
		
		assertThrows(
				IllegalStateException.class, 
				() -> game.moveUp(), 
				"IllegalState skal kastes når man prøver å bevege seg mens spillet er over"
			);
		
		game.clear();
		game.setGameGoal();
		game.setGameStart();
		game.moveUp();
		game.moveUp();
		game.moveRight();
		game.moveRight();
		game.moveUp();
		game.moveUp();
		
		assertTrue(game.isGameOver() && !game.getIsGameWon());
	}
	@Test
	public void testGameWon() {
		game.moveRight();
		game.moveRight();
		game.moveUp();
		game.moveUp();
		game.moveLeft();
		game.moveLeft();
		game.moveUp();
		game.moveUp();
		game.moveRight();
		game.moveRight();
		
		assertTrue(game.getIsGameWon() && !game.isGameOver());
		
		assertThrows(
				IllegalStateException.class, 
				() -> game.moveUp(), 
				"IllegalState skal kastes når man prøver å bevege seg mens spillet er vunnet"
			);
	}
	@Test
	public void testRuleList() {
		game.moveUp();
		
		assertEquals(game.getRuleList(),Arrays.asList(true,true));
		
		game.clear();
		game.setGameStart();
		game.setGameGoal();
		game.getTile(2, 3).removeDot(); //fjerner dot
		game.moveUp();
		
		assertEquals(game.getRuleList(), Arrays.asList(true,false));
		
		game.clear();
		game.setGameStart();
		game.setGameGoal();
		game.getTile(1, 1).setBlank();
		game.getTile(1, 3).setBlank();
		game.moveUp();
		
		assertEquals(game.getRuleList(), Arrays.asList(false,false));
	}
}

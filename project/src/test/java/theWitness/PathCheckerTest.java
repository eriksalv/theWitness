package theWitness;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PathCheckerTest {
	
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
	public void testCheckDots() {
		assertFalse(PathChecker.checkDots(game));
		game.moveRight();
		game.moveRight();
		game.moveUp();
		assertTrue(PathChecker.checkDots(game));
		game.moveDown();
		assertFalse(PathChecker.checkDots(game));

	}
	@Test
	public void testIsNotBorder() {
		assertTrue(PathChecker.isNotBorder(game, 0, 4)); //start-tile er ikke en border
		game.moveRight();
		assertFalse(PathChecker.isNotBorder(game, 0, 4));
		assertTrue(PathChecker.isNotBorder(game, 1, 1)); //svart/hvit-tile er ikke border
	}
	@Test
	public void testIsSameColor() {
		Tile white = new Tile(4,2);
		white.setWhite();
		Tile black = new Tile(6,9);
		black.setBlack();
		
		assertTrue(PathChecker.isSameColor(white, game.getTile(1, 3)));
		assertTrue(PathChecker.isSameColor(black, game.getTile(1, 1)));
		assertFalse(PathChecker.isSameColor(white, black));
	}
	@Test
	public void testFindSurroundingTiles() {
		assertEquals(PathChecker.findSurroundingTiles(game, game.getTile(1, 1)),
				PathChecker.findSurroundingTiles(game,game.getTile(1, 3))
				);
		game.moveUp();
		game.moveUp();
		game.moveRight();
		game.moveRight();
		game.moveUp();
		game.moveUp();
		assertEquals(PathChecker.findSurroundingTiles(game, game.getTile(1, 3)),
				new HashSet<Tile>(Arrays.asList(game.getTile(2, 4),game.getTile(1, 4), game.getTile(1, 3), game.getTile(2, 3)))
				);
		assertEquals(PathChecker.findSurroundingTiles(game, game.getTile(1, 1)),
				new HashSet<Tile>(Arrays.asList(game.getTile(0, 0),game.getTile(1, 0), game.getTile(0, 1), game.getTile(1, 1)))
				);		
	}
	@Test
	public void testCheckColorsSeparated() {
		assertFalse(PathChecker.checkColorsSeparated(game));
		game.moveUp();
		game.moveUp();
		game.moveRight();
		assertFalse(PathChecker.checkColorsSeparated(game));
		game.moveRight();
		game.moveUp();
		game.moveUp();
		assertTrue(PathChecker.checkColorsSeparated(game));
	}
	@Test
	public void testCheckPath() {
		game.moveRight();
		game.moveRight();
		game.moveUp();
		assertFalse(PathChecker.checkPath(game));
		game.moveUp();
		game.moveLeft();
		game.moveLeft();
		game.moveUp();
		game.moveUp();
		game.moveRight();
		game.moveRight();
		assertTrue(PathChecker.checkPath(game));
	}
}

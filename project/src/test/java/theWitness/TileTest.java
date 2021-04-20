package theWitness;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class TileTest {

	private Tile tile;
	
	@BeforeEach
	public void setup() {
		tile = new Tile(0,0);
	}
	@Test
	@DisplayName("tester konstruktør")
	public void testConstructor() {
		assertEquals(0, tile.getX());
		assertEquals(0, tile.getY());
		
		Tile tile2 = new Tile(23,2);
		assertEquals(23,tile2.getX());
		assertEquals(2,tile2.getY());
		
		assertThrows(
				IllegalArgumentException.class,
				() -> new Tile(-3,2),
				"IllegalArgument skal kastes når et tile med negativ x- eller y-verdi initialiseres"
		);
	}
	@Test
	@DisplayName("Test at setType() fungerer riktig")
	public void testSetValidType() {
		tile.setType('=');
		assertTrue(tile.isMovedLine());
		
		tile.setType('<');
		assertTrue(tile.isWhite() && !tile.isMovedLine());
		
		assertThrows(
			IllegalArgumentException.class, 
			() -> tile.setType('?'), 
			"IllegalArgument skal kastes når man setter Tile til ugyldig verdi"
		);
	}
	@Test
	@DisplayName("Test at tiles med dot fungerer som de skal")
	public void testDot() {
		tile.setType('=');
		assertTrue(tile.isMovedLine());
		
		tile.setType('.');
		assertTrue(tile.isMovedLine() && tile.getContainsDot());
		
		tile.setLine();
		assertTrue(tile.isLine() && tile.getContainsDot() && !tile.isMovedLine());
	}
	@Test 
	@DisplayName("Tester tiles med kollisjon")
	public void testCollision() {
		assertTrue(tile.hasCollision());
		
		tile.setGoal();
		assertFalse(tile.hasCollision());
		
		tile.setBlack();
		assertTrue(tile.hasCollision());		
	}
}

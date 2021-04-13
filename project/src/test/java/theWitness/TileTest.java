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
	@DisplayName("Test at setType() fungerer riktig")
	public void testSetValidType() {
		tile.setType('=');
		assertTrue(tile.isMovedLine());
		tile.setType('.');
		assertTrue(tile.isMovedLine() && tile.getContainsDot());
		assertThrows(
			IllegalArgumentException.class, 
			() -> tile.setType('?'), 
			"IllegalArgument skal kastes når man setter Tile til ugyldig verdi!"
		);
	}
}

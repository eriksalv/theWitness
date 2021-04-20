package theWitness;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GridTest {
	
	private Grid grid;
	
	private void createGrid() {
		grid = new Grid(3,5);
		grid.forEach(tile -> System.out.println(tile));
	}	
	@BeforeEach
	public void setup() {
		createGrid();
	}
	@Test
	public void testConstructor() {
		grid = new Grid(3,5);
		
		assertEquals(grid.getWidth(), 3);
		assertEquals(grid.getHeight(),5);
		
		grid.forEach(tile -> {
			assertTrue(tile.isLine());
		});
		
		assertThrows(
				IllegalArgumentException.class,
				() -> new Grid(-3,2),
				"IllegalArgument skal kastes når et Grid med negativ bredde eller høyde initialiseres"
		);
	}
	@Test
	public void iteratorTest() { //Tester egen implementasjon av iterator for Grid. 
		Stream<Tile> itStream = grid.getStreamFromIterator();
			
	}
	
}

package theWitness;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GridTest {
	
	private Grid grid;
	
	private void createGrid() {
		grid = new Grid(3,5);
		grid.getTile(0, 4).setStart();
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
		
		grid.forEach(tile -> { //tester at alle tiles i grid er lines
			assertTrue(tile.isLine());
		});
		
		assertThrows(
				IllegalArgumentException.class,
				() -> new Grid(-3,2),
				"IllegalArgument skal kastes når et Grid med negativ bredde eller høyde initialiseres"
		);
	}
	@Test 
	public void testIsTile() {
		assertTrue(grid.isTile(0,1));
		assertFalse(grid.isTile(3, 5));
		assertTrue(grid.getTile(0, 4).isStart());
	}
	@Test
	@DisplayName("Tester egen implementasjon av iterator og stream for Grid.")
	public void iteratorToStreamTest() {
		Stream<Tile> itStream = grid.getStreamFromIterator();
		List<Tile> tileList = itStream.collect(Collectors.toList());
		
		assertEquals(tileList.size(),15);
		
		//skjekker at iteratoren går gjennom grid i riktig rekkefølge, altså at den går gjennom rad for rad.
		for (int y=0;y<grid.getHeight();y++) {
			for (int x=0;x<grid.getWidth();x++) {
				assertEquals(tileList.get(y*grid.getWidth() + x), grid.getTile(x, y));
			}
		}
	}
	
}

package theWitness;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Grid implements Iterable<Tile>{
	private int height;
	private int width;
	private List<List<Tile>> grid;	
	
	public Grid(int width, int height) {
		if (width<=0 || height<=0) {
			throw new IllegalArgumentException("grid must have positive height and width");
		}
		this.height = height;
		this.width = width;
		
        grid = new ArrayList<>(width);
		
		for (int y=0;y<height;y++) {
			grid.add(new ArrayList<Tile>(width));
			for (int x=0;x<width;x++) {
				grid.get(y).add(new Tile(x,y));
				grid.get(y).get(x).setLine();
			}
		}
	}
	
	public boolean isTile(int x, int y) {
		return x >= 0 && y >= 0 && x < getWidth() && y < getHeight();
	}
	
	public Tile getTile(int x, int y) {
		if (!isTile(x, y)) {
			throw new IllegalArgumentException("Coordinates out of bounds");
		}
		return this.grid.get(y).get(x);
	}

	public int getHeight() {
		return height;
	}


	public int getWidth() {
		return width;
	}
	
	public List<List<Tile>> getGrid() {
		return grid;
	}
	
	@Override
	public Iterator<Tile> iterator() { //itererer gjennom grid rad for rad
		Iterator<Tile> it = new Iterator<Tile>() {

            private int yIndex=0;
            private int xIndex=0;

            @Override
            public boolean hasNext() {
            	while (yIndex<getHeight() && xIndex<getWidth()) {
        			return true;
        		}
        		return false;
            }

            @Override
            public Tile next() {
            	if (!hasNext()) {
        			throw new NoSuchElementException("There is no next element");
        		}
            	while (!getTile(xIndex, yIndex).equals(getTile(getWidth()-1, yIndex))) {
        			return getTile(xIndex++, yIndex); //inkrementerer xIndex så lenge iterator ikke er på den siste kolonnen i grid
        		}
        		int reset = xIndex; //når xIndex kommer til den siste kolonnen i grid, skal den resettes til 0, mens yIndex økes med 1
        		xIndex=0;
        		return getTile(reset, yIndex++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        return it;
	}
	
	public <T> Stream<Tile> getStreamFromIterator() {  
        //konverterer iterator til spliterator
        Spliterator<Tile> spliterator = Spliterators.spliteratorUnknownSize(iterator(), 0);  
        
        //returnerer en stream av spliteratoren
        return StreamSupport.stream(spliterator, false);
    }
	
	@Override
	public String toString() {
		String boardString = "";
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				boardString += getTile(x, y);
			}
			boardString += "\n";
		}
		return boardString;
	}
	
	public static void main(String[] args) {
		Game game = new Game(5, 5);
		System.out.println(game);
		System.out.println(game.getTile(0, 0));
	}

}

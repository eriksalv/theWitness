package theWitness;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public final class PathChecker {
		
	public static final boolean checkPath(Game game) {
		return checkBlackWhiteNew(game) && checkDots(game);
	}
	
	private static boolean checkDots(Game game) {
		//hvis en vanlig linje inneholder en dot, betyr det at man ikke har beveget seg gjennom den.
		if (game.getStreamFromIterator().anyMatch(tile -> tile.getContainsDot() && tile.isLine())) { 
			return false;
		}
		return true;
	}
	
	private static boolean checkBlackWhiteNew(Game game) {
		AtomicBoolean gameWon = new AtomicBoolean(true); //wrapper klasse for å endre verdi i lambda uttrykk
		game.getStreamFromIterator().filter(tile -> tile.isBlack() || tile.isWhite()).forEach(tile -> {
			Tile startingTile = game.getTile(tile.getX(), tile.getY());
			Set<Tile> surroundingTiles = findSurroundingTiles(game,startingTile);
			surroundingTiles.add(startingTile);
			if (surroundingTiles.stream().anyMatch(t -> t.isBlack()) && surroundingTiles.stream().anyMatch(t -> t.isWhite())) {
				gameWon.set(false); //dersom det både er anymatch for både black og white i samme område settes gameWon til false
			}
			System.out.println(surroundingTiles);
		});
		return gameWon.get();
	}
	
	private static Set<Tile> findSurroundingTiles(Game game, Tile startingTile) { //veldig treg algoritme, men den funker.. :)
		
		AtomicInteger x = new AtomicInteger(startingTile.getX());
		AtomicInteger y = new AtomicInteger(startingTile.getY());
		Set<Tile> surroundingTiles = new HashSet<Tile>();
		
		//Går mot høyre
		while (isNotBorder(game,x.get()+1,y.get())) { 
			surroundingTiles.add(game.getTile(x.incrementAndGet(), y.get()));
			//går ned
			while (isNotBorder(game,x.get(),y.get()+1)) {
				surroundingTiles.add(game.getTile(x.get(), y.incrementAndGet()));
			}
			y.set(startingTile.getY()); //resetter y-verdi
			//går opp
			while (isNotBorder(game,x.get(),y.get()-1)) {
				surroundingTiles.add(game.getTile(x.get(), y.decrementAndGet()));
			}
			y.set(startingTile.getY());
		}
		x.set(startingTile.getX());
		//går mot venstre
		while (isNotBorder(game,x.get()-1,y.get())) {
			surroundingTiles.add(game.getTile(x.decrementAndGet(), y.get()));
			//går ned
			while (isNotBorder(game,x.get(),y.get()+1)) {
				surroundingTiles.add(game.getTile(x.get(), y.incrementAndGet()));
			}
			y.set(startingTile.getY()); //resetter y-verdi
			//går opp
			while (isNotBorder(game,x.get(),y.get()-1)) {
				surroundingTiles.add(game.getTile(x.get(), y.decrementAndGet()));
			}
			y.set(startingTile.getY());
		}
		x.set(startingTile.getX());
		//Går opp
		while (isNotBorder(game,x.get(),y.get()-1)) { 
			surroundingTiles.add(game.getTile(x.get(), y.decrementAndGet()));
			//går mot høyre
			while (isNotBorder(game,x.get()+1,y.get())) {
				surroundingTiles.add(game.getTile(x.incrementAndGet(), y.get()));
			}
			x.set(startingTile.getX()); //resetter x-verdi
			//går mot venstre
			while (isNotBorder(game,x.get()-1,y.get())) {
				surroundingTiles.add(game.getTile(x.decrementAndGet(), y.get()));
			}
			x.set(startingTile.getX());
		}
		y.set(startingTile.getY());
		//går ned
		while (isNotBorder(game,x.get(),y.get()+1)) {
			surroundingTiles.add(game.getTile(x.get(), y.incrementAndGet()));
			//går mot høyre
			while (isNotBorder(game,x.get()+1,y.get())) {
				surroundingTiles.add(game.getTile(x.incrementAndGet(), y.get()));
			}
			x.set(startingTile.getX()); //resetter y-verdi
			//går mot venstre
			while (isNotBorder(game,x.get()-1,y.get())) {
				surroundingTiles.add(game.getTile(x.decrementAndGet(), y.get()));
			}
			x.set(startingTile.getX());
		}
		y.set(startingTile.getY());
		return surroundingTiles;
	}
	private static boolean isNotBorder(Game game, int x, int y) { //skjekker om en tile er en del av linjen som er tegnet
		return game.isTile(x, y) && "0@=".indexOf(game.getTile(x, y).getType())==-1;
	}
	private static boolean checkBlackWhite(Game game) {
		if (game.getRuleList().get(0)==false) { //hvis gamet ikke inneholder svarte/hvite ruter, skal metoden returnere true med en gang
			return true;
		}
		List<Tile> movedLine = new ArrayList<Tile>(game.getMoves().keySet());
    	List<String> moveOrder = new ArrayList<String>(game.getMoves().values());
    	System.out.println(movedLine);
    	System.out.println(moveOrder);
    	    	
    	HashSet<Character> sector1 = new HashSet<Character>();
    	HashSet<Character> sector2 = new HashSet<Character>();
    	
    	for (int i=1;i<movedLine.size();i++) {
    		if (moveOrder.get(i).equals("Up")) {
    			AtomicInteger nextX1 = new AtomicInteger(movedLine.get(i).getX()-1);
    			AtomicInteger nextX2 = new AtomicInteger(movedLine.get(i).getX()+1);
    			AtomicInteger nextY1 = new AtomicInteger(movedLine.get(i).getY());
    			AtomicInteger nextY2 = new AtomicInteger(movedLine.get(i).getY());
    			Tile nextTile1 = new Tile(nextX1.get(), nextY1.get());
    			Tile nextTile2 = new Tile(nextX2.get(),nextY2.get());
    			
    			while (game.isTile(nextTile1.getX(),nextTile1.getY()) && !game.getTile(nextTile1.getX(),nextTile1.getY()).isMovedLine()) {
    				//System.out.println(nextTile1.getX() + ", " + nextTile1.getY());
    				if (game.getTile(nextTile1.getX(),nextTile1.getY()).isBlack() || game.getTile(nextTile1.getX(),nextTile1.getY()).isWhite()) {
    					sector1.add(game.getTile(nextTile1.getX(),nextTile1.getY()).getType());
    				}
    				nextX1.getAndDecrement();
    				nextTile1 = new Tile(nextX1.get(),nextY1.get());
    			}
    			while (game.isTile(nextTile2.getX(),nextTile2.getY()) && !game.getTile(nextTile2.getX(),nextTile2.getY()).isMovedLine()) {
    				System.out.println(nextTile2.getX() + ", " + nextTile2.getY());
    				if (game.getTile(nextTile2.getX(),nextTile2.getY()).isBlack() || game.getTile(nextTile2.getX(),nextTile2.getY()).isWhite()) {
    					sector2.add(game.getTile(nextTile2.getX(),nextTile2.getY()).getType());
    				}
    				nextX2.getAndIncrement();
    				nextTile2 = new Tile(nextX2.get(),nextY2.get());
    			}
    		}
    		if (moveOrder.get(i).equals("Down")) {
    			AtomicInteger nextX1 = new AtomicInteger(movedLine.get(i).getX()+1);
    			AtomicInteger nextX2 = new AtomicInteger(movedLine.get(i).getX()-1);
    			AtomicInteger nextY1 = new AtomicInteger(movedLine.get(i).getY());
    			AtomicInteger nextY2 = new AtomicInteger(movedLine.get(i).getY());
    			Tile nextTile1 = new Tile(nextX1.get(), nextY1.get());
    			Tile nextTile2 = new Tile(nextX2.get(),nextY2.get());
    			
    			while (game.isTile(nextTile1.getX(),nextTile1.getY()) && !game.getTile(nextTile1.getX(),nextTile1.getY()).isMovedLine()) {
    				//System.out.println(nextTile1.getX() + ", " + nextTile1.getY());
    				if (game.getTile(nextTile1.getX(),nextTile1.getY()).isBlack() || game.getTile(nextTile1.getX(),nextTile1.getY()).isWhite()) {
    					sector1.add(game.getTile(nextTile1.getX(),nextTile1.getY()).getType());
    				}
    				nextX1.getAndIncrement();
    				nextTile1 = new Tile(nextX1.get(),nextY1.get());
    			}
    			while (game.isTile(nextTile2.getX(),nextTile2.getY()) && !game.getTile(nextTile2.getX(),nextTile2.getY()).isMovedLine()) {
    				System.out.println(nextTile2.getX() + ", " + nextTile2.getY());
    				if (game.getTile(nextTile2.getX(),nextTile2.getY()).isBlack() || game.getTile(nextTile2.getX(),nextTile2.getY()).isWhite()) {
    					sector2.add(game.getTile(nextTile2.getX(),nextTile2.getY()).getType());
    				}
    				nextX2.getAndDecrement();
    				nextTile2 = new Tile(nextX2.get(),nextY2.get());
    			}
    		}
    		if (moveOrder.get(i).equals("Left")) {
    			AtomicInteger nextX1 = new AtomicInteger(movedLine.get(i).getX());
    			AtomicInteger nextX2 = new AtomicInteger(movedLine.get(i).getX());
    			AtomicInteger nextY1 = new AtomicInteger(movedLine.get(i).getY()+1);
    			AtomicInteger nextY2 = new AtomicInteger(movedLine.get(i).getY()-1);
    			Tile nextTile1 = new Tile(nextX1.get(), nextY1.get());
    			Tile nextTile2 = new Tile(nextX2.get(),nextY2.get());
    			
    			while (game.isTile(nextTile1.getX(),nextTile1.getY()) && !game.getTile(nextTile1.getX(),nextTile1.getY()).isMovedLine()) {
    				//System.out.println(nextTile1.getX() + ", " + nextTile1.getY());
    				if (game.getTile(nextTile1.getX(),nextTile1.getY()).isBlack() || game.getTile(nextTile1.getX(),nextTile1.getY()).isWhite()) {
    					sector1.add(game.getTile(nextTile1.getX(),nextTile1.getY()).getType());
    				}
    				nextY1.getAndIncrement();
    				nextTile1 = new Tile(nextX1.get(),nextY1.get());
    			}
    			while (game.isTile(nextTile2.getX(),nextTile2.getY()) && !game.getTile(nextTile2.getX(),nextTile2.getY()).isMovedLine()) {
    				System.out.println(nextTile2.getX() + ", " + nextTile2.getY());
    				if (game.getTile(nextTile2.getX(),nextTile2.getY()).isBlack() || game.getTile(nextTile2.getX(),nextTile2.getY()).isWhite()) {
    					sector2.add(game.getTile(nextTile2.getX(),nextTile2.getY()).getType());
    				}
    				nextY2.getAndDecrement();
    				nextTile2 = new Tile(nextX2.get(),nextY2.get());
    			}
    		}
    		if (moveOrder.get(i).equals("Right")) { 
    			AtomicInteger nextX1 = new AtomicInteger(movedLine.get(i).getX());
    			AtomicInteger nextX2 = new AtomicInteger(movedLine.get(i).getX());
    			AtomicInteger nextY1 = new AtomicInteger(movedLine.get(i).getY()-1);
    			AtomicInteger nextY2 = new AtomicInteger(movedLine.get(i).getY()+1);
    			Tile nextTile1 = new Tile(nextX1.get(), nextY1.get());
    			Tile nextTile2 = new Tile(nextX2.get(),nextY2.get());
    			
    			while (game.isTile(nextTile1.getX(),nextTile1.getY()) && !game.getTile(nextTile1.getX(),nextTile1.getY()).isMovedLine()) {
    				//System.out.println(nextTile1.getX() + ", " + nextTile1.getY());
    				if (game.getTile(nextTile1.getX(),nextTile1.getY()).isBlack() || game.getTile(nextTile1.getX(),nextTile1.getY()).isWhite()) {
    					sector1.add(game.getTile(nextTile1.getX(),nextTile1.getY()).getType());
    				}
    				nextY1.getAndDecrement();
    				nextTile1 = new Tile(nextX1.get(),nextY1.get());
    			}
    			while (game.isTile(nextTile2.getX(),nextTile2.getY()) && !game.getTile(nextTile2.getX(),nextTile2.getY()).isMovedLine()) {
    				System.out.println(nextTile2.getX() + ", " + nextTile2.getY());
    				if (game.getTile(nextTile2.getX(),nextTile2.getY()).isBlack() || game.getTile(nextTile2.getX(),nextTile2.getY()).isWhite()) {
    					sector2.add(game.getTile(nextTile2.getX(),nextTile2.getY()).getType());
    				}
    				nextY2.getAndIncrement();
    				nextTile2 = new Tile(nextX2.get(),nextY2.get());
    			}
    		}
    	}
    	System.out.println(sector1);
    	System.out.println(sector2);
    	if (sector1.size()>1 || sector2.size()>1 || sector1.size()+sector2.size()==1) { //hvis det er mer enn 1 element i hvert sektor-set betyr det at det er en hvit og en svart i en sektor
    		return false;
    	}
    	return true;
	}
}

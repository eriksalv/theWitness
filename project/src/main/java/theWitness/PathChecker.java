package theWitness;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public final class PathChecker {
		
	public static final boolean checkPath(Game game) {
		List<Boolean> satisfiedRules = new ArrayList<Boolean>();
		for (int i=0;i<game.getRuleList().size();i++) {
			satisfiedRules.add(true);
		}	
		
		if (game.getRuleList().get(0))
			satisfiedRules.set(0, checkColorsSeparated(game));
		if (game.getRuleList().get(1)) 
			satisfiedRules.set(1, checkDots(game));	
		
		for (Boolean b : satisfiedRules) {
			if (!b) 
				return false;
		}
		return true;
	}
	
	public static final boolean checkDots(Game game) {
		//hvis en vanlig linje inneholder en dot, betyr det at man ikke har beveget seg gjennom den.
		if (game.getStreamFromIterator().anyMatch(tile -> tile.getContainsDot() && tile.isLine())) { 
			return false;
		}
		return true;
	}
	
	public static final boolean checkColorsSeparated(Game game) {
		LinkedHashMap<Tile,Set<Tile>> surroundingTilesList = new LinkedHashMap<Tile,Set<Tile>>();
		boolean gameWon = true; 
		
		game.getStreamFromIterator().filter(tile -> tile.getIsColored()).forEach(tile -> { //for hver tile som er svart/hvit:
			Tile startingTile = game.getTile(tile.getX(), tile.getY());
			Set<Tile> surroundingTiles = findSurroundingTiles(game,startingTile);
			surroundingTilesList.put(startingTile,surroundingTiles);
		});
		
		for (int i=0;i<surroundingTilesList.size()-1;i++) { //skjekker at surroundingtiles-settene til de fargede tilene er disjunkte
			for (int j=0;j<surroundingTilesList.size();j++) {
				if (i==j || //skal ikke sammenligne et set med seg selv
						(isSameColor(new ArrayList<Tile>(surroundingTilesList.keySet()).get(i),
								new ArrayList<Tile>(surroundingTilesList.keySet()).get(j)))) //skal heller ikke sammenligne to set med samme start-tile farge
				{
					continue;
				}
				else if (!Collections.disjoint(new ArrayList<Set<Tile>>(surroundingTilesList.values()).get(i),
						new ArrayList<Set<Tile>>(surroundingTilesList.values()).get(j))) 
				{
					gameWon=false; //hvis det finnes to set med forskjellig farge på start-tile, som ikke er disjunkte, er spillet ikke vunnet
				}
			}
		}
		return gameWon;
	}
	
	//finner alle tiles fra startingTile som ikke er avskilt av en movedLine eller andre kanter
	public static final Set<Tile> findSurroundingTiles(Game game, Tile startingTile) {
		
		AtomicInteger x = new AtomicInteger(startingTile.getX());
		AtomicInteger y = new AtomicInteger(startingTile.getY());
		Set<Tile> surroundingTiles = new HashSet<Tile>();
		surroundingTiles.add(startingTile);
		
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
	public static final boolean isNotBorder(Game game, int x, int y) { //skjekker om en tile er en del av linjen som er tegnet
		return game.isTile(x, y) && "0@=".indexOf(game.getTile(x, y).getType())==-1;
	}
	public static final boolean isSameColor(Tile tile1, Tile tile2) { //skjekker om to tiles har samme farge
		//(tile1.isBlack() && tile2.isBlack()) || (tile1.isWhite() && tile2.isWhite()) || (tile1.isPink() && tile2.isPink()) || (tile1.isCyan() && tile2.isCyan())
    	if ((tile1.getIsColored() && tile2.getIsColored()) && tile1.getType()==tile2.getType()) {
    		return true;
    	}
    	return false;
    }
	public static void main(String[] args) {
		Game game = null;
		try {
			game=LevelEnumerator.LEVEL_16.startingTiles();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		checkColorsSeparated(game);
		System.out.println(checkColorsSeparated(game));
		game.moveUp();
		game.moveUp();
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
		checkColorsSeparated(game);
		System.out.println(checkColorsSeparated(game));
	}
}

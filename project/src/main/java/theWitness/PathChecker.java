package theWitness;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class PathChecker {
		
	public static boolean checkPath(Game game) {
		return checkBlackWhite(game);
	}
	
	private static boolean checkBlackWhite(Game game) {
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
    	if (sector1.size()>1 || sector2.size()>1) { //hvis det er mer enn 1 element i hvert sektor-set betyr det at det er en hvit og en svart i en sektor
    		return false;
    	}
    	return true;
	}
}

package theWitness;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
//import java.util.Arrays;

public class Game extends Grid {
	private int level;
	private int[] start = new int[2];
	private int[] goal = new int[2];
	private List<Boolean> ruleList = new ArrayList<Boolean>(Arrays.asList(false,false)); 
	
	private GameCollection gameCollection;
	
	//Bruker LinkedHashMap for å ta vare på rekkefølgen elementer blir satt inn, som er viktig for PathChecker-klassen
    private Map<Tile,String> moves = new LinkedHashMap<Tile,String>(); 
    private boolean isGameWon;
    private boolean isCorrectPath;
    private boolean firstMove;
    private boolean isGameOver;
	
	public Game(int width, int height) {
		super(width,height);
		isGameWon=false;
		isCorrectPath=false;
		firstMove=true;
		isGameOver=false;
	}
	
	public Game(Game game) {
		this(game.getWidth(),game.getHeight());
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level=level;
	}

//	public ArrayList<Tile> getMovedLine() {
//		return movedLine;
//	}
	
	public Map<Tile,String> getMoves() {
		return moves;
	}
	
	public boolean getIsGameWon() {
		return isGameWon;
	}
	
	public boolean isGameOver() {
		return isGameOver;
	}
	
	public void setGameGoal() {
		getTile(goal[0],goal[1]).setGoal();
	}
	public void setGameGoal(int x,int y) { //game skal huske på hvor start- og goaltile er
		if (!isTile(x,y)) {
			throw new IllegalArgumentException("goal coordinates out of bounds");
		}
		goal[0]=x;
		goal[1]=y;
		getTile(x,y).setGoal();
	}
	
	public int[] getGameGoal() {
		return goal;
	}
	
	public void setGameStart() {
		getTile(start[0],start[1]).setStart();
	}
	
	public void setGameStart(int x, int y) {
		if (!isTile(x,y)) {
			throw new IllegalArgumentException("start coordinates out of bounds" + x + y);
		}
		start[0]=x;
		start[1]=y;
		getTile(x,y).setStart();
	}
	
	public int[] getGameStart() {
		return start;
	}
	
	public void addGameCollection(GameCollection gameCollection) {
		this.gameCollection = gameCollection;
	}
	
	public void removeGameCollection(GameCollection gameCollection) {
		gameCollection=null;
	}
	
	public GameCollection getGameCollection() {
		return gameCollection;
	}
	
	public void clear() {
		getGrid()
		.forEach(list -> list.stream()
				.filter(tile -> !Character.toString(tile.getType()).matches("[S@><_]")) //tile typer som ikke skal resettes
				.forEach(tile -> tile.setLine()));
		isGameWon=false;
		isCorrectPath=false;
		firstMove=true;
		isGameOver=false;
		moves.clear();
		
	}
		
	private void drawLine(int dx, int dy, String move) {		
		//hvis spillet ikke har startet enda, må start-ruten endre seg
		if (firstMove) { 
			setRuleList(); // setter reglene for dette gamet
			getStreamFromIterator().filter(tile -> tile.isStart()).forEach(tile -> {
				tile.setMovedLine();
				moves.put(tile, move); //legger til en moveOrder for start-tile. Det er ikke så farlig at denne ikke er riktig, bare at den er der.
			});
			firstMove=false;
		}
		
//		int targetX = movedLine.get(movedLine.size()-1).getX() + dx;
//		int targetY = movedLine.get(movedLine.size()-1).getY() + dy;
		//Lager en arraylist av settet av keys i moves for å hente ut den siste Tile som ble lagt til
		List<Tile> movesList = new ArrayList<Tile>(moves.keySet());
		int targetX = movesList.get(moves.size()-1).getX() + dx;
		int targetY = movesList.get(moves.size()-1).getY() + dy;
		
		//validerer trekket før det gjøres (store) endringer i tilstanden
		validateMove(targetX, targetY, true); 
		
		Tile targetTile = getTile(targetX,targetY);
		
		if (movesList.size()>=2 && targetTile==movesList.get(movesList.size()-2)) { //Hvis man går tilbake
			if (movesList.size()>=3) {
				movesList.get(movesList.size()-3).setLastMovedLine();
			}
			//grid[targetY-dy][targetX-dx].setLine();
			getTile(targetX-dx, targetY-dy).setLine(); //setter tilbake til vanlig rute
			targetTile.setMovedLine();
			moves.remove(movesList.get(movesList.size()-1)); //Fjerner siste element fra moves
		}
		else {
			moves.put(targetTile, move);
			movesList.add(targetTile);
			if (targetTile.isGoal()) {
				if (checkCorrectPath()) {
					isGameWon=true;
					if (gameCollection!=null) {
						gameCollection.gameStateChanged(this, isGameWon); //informerer gameCollection om at isGameWon er true
					}
				} else {
					isGameOver=true;
				}
			}
			targetTile.setMovedLine();
			//grid[targetY-dy][targetX-dx].setLastMovedLine();
			getTile(targetX-dx, targetY-dy).setLastMovedLine();
			if (movesList.size()>=3) {
				movesList.get(movesList.size()-3).setMovedLine(); //ruten to steg bak den siste skal bli "vanlig" igjen slik at den får kollisjon
			}
		}
		if (isGameWon) {
			movesList.forEach(tile -> tile.setMovedLine());
			System.out.println(moves.values());
		}
	}

    public void moveUp() {
        drawLine(0, -1, "Up");
	    System.out.println(this);
    }

    public void moveDown() {
        drawLine(0, 1,"Down");
	    System.out.println(this);
    }

    public void moveLeft() {
        drawLine(-1, 0,"Left");
	    System.out.println(this);
    }

    public void moveRight() {
        drawLine(1, 0,"Right");
	    System.out.println(this);
    }
	
	// Oppgave 4 a)
	/*private boolean canMoveTo(int dx, int dy) {
		
		// Snake head coordinates
		int targetX = snake.get(0).getX() + dx;
		int targetY = snake.get(0).getY() + dy;
		
		if(!isTile(targetX, targetY)) {
			return false;
		}
		
		Tile targetTile = getTile(targetX, targetY);
		boolean tileIsSnakeTail = (targetTile == snake.get(snake.size()-1));
		
		return !targetTile.hasCollision() || tileIsSnakeTail;
	}*/
    
    public void setRuleList() {
    	if (getStreamFromIterator().anyMatch(tile -> tile.isBlack() || tile.isWhite())) {
    		ruleList.set(0, true);
    	} else {
    		ruleList.set(0, false);
    	}
    	if (getStreamFromIterator().anyMatch(tile -> tile.getContainsDot())) {
    		ruleList.set(1, true);
    	} else {
    		ruleList.set(1, false);
    	}
    	
    }
    
    public List<Boolean> getRuleList() {
    	return ruleList;
    }
    
    private boolean checkCorrectPath() {                                       //WTF?????
    	return PathChecker.checkPath(this);    	
    }
    
    private boolean validateMove(int x, int y, boolean throwException) {
    	if (isGameWon || isGameOver) {
			if (throwException) {
				throw new IllegalStateException("Game is already completed");
			}
			return false;
		}
    	else if (!isTile(x, y)) {
			if (throwException) {
				throw new IllegalStateException("Invalid move: coordinates out of bounds");
			}
			return false;
		}
    	else if (getTile(x,y).hasCollision()) {
    		if (throwException) {
    			throw new IllegalStateException("Invalid move: target tile has collision");
    		}
    		return false;
		}
		return true;
    }
    
    @Override
    public String toString() {
    	String boardString = super.toString();
		if (isGameWon) {
			boardString += "\n\nGame won!";
		}
		return boardString;
    }
	
	public static void main(String[] args) {
		 Game game = new Game(5, 5);
		 GameCollection games = new GameCollection("", game);
		 
		 game.setGameStart(0, 2);
		 game.setGameGoal(4,2);
		 game.getTile(1, 1).setBlack();
		 game.getTile(3, 1).setWhite();
		 game.getTile(2, 2).setDot();
		 
		 System.out.println(game);
		 
		 game.moveRight();
		 game.moveRight();
		 game.moveUp();
		 game.moveUp();
		 game.moveRight();
		 game.moveRight();
		 game.moveDown();
		 game.moveDown();
		 
//		 game.moveDown();
//		 game.moveDown();
//		 game.moveRight();
//		 game.moveRight();
//		 game.moveRight();
//		 game.moveRight();
//		 game.moveUp();
//		 game.moveUp();
		 
		 
		 /*game.moveDown();
		 game.moveDown();
		 game.moveDown();
		 game.moveDown();
		 game.moveLeft();
		 game.moveRight();*/
		/*Game game = new Game(3, 7);
		 
		 game.getTile(1, 1).setWhite();
		 game.getTile(1,3).setBlack();
		 game.getTile(1, 5).setWhite();
		 game.getTile(0, 2).setStart();
		 game.getTile(game.getWidth()-1, 2).setGoal();
		 System.out.println(game);
		 game.moveRight();
		 game.moveRight();

		 for (int i=0;i<game.getMovedLine().size();i++) {
			 System.out.println(game.getMovedLine().get(i).getX() + "," + game.getMovedLine().get(i).getY());
		 }
		 System.out.println(game.getMovedLine());*/
	}

}

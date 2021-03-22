package theWitness;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
//import java.util.Arrays;

public class Game {		
	
	private int[] gridSize = new int[2];
	private Tile[][] grid;
	
	protected static int[] start = {0,0};
	
    private ArrayList<Tile> movedLine = new ArrayList<Tile>();
    private ArrayList<String> moveOrder = new ArrayList<String>();
    private boolean isGameWon = false;
    private boolean isCorrectPath = false;
    private boolean firstMove = true;
	
	public Game(int width, int height) {
		this.gridSize[0] = height;
		this.gridSize[1] = width;
		
		this.grid = new Tile[height][width];
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				grid[y][x] = new Tile(x, y);
				grid[y][x].setLine();
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
		return this.grid[y][x];
	}
	
	public int[] getGridSize() {
		return gridSize;
	}

	public int getHeight() {
		return gridSize[0];
	}


	public int getWidth() {
		return gridSize[1];
	}
	
	public ArrayList<Tile> getMovedLine() {
		return movedLine;
	}
	
	public void drawLine(int dx, int dy) {
		if (firstMove) { //optimalisering
			for (int y = 0; y < getHeight(); y++) {
				for (int x = 0; x < getWidth(); x++) {
					if (getTile(x,y).getType()=='S') {
						grid[y][x].setMovedLine();
						movedLine.add(grid[y][x]);
					}
				}
			}
			firstMove=false;
		}
		
		int targetX = movedLine.get(movedLine.size()-1).getX() + dx;
		int targetY = movedLine.get(movedLine.size()-1).getY() + dy;
		Tile targetTile = getTile(targetX,targetY);
		
		validateMove(targetX, targetY, targetTile, true);
		
		if (targetTile.isGoal()) {
			if (checkCorrectPath()) {
				isGameWon=true;
			}
		}
		
		if (movedLine.size()>=2 && targetTile==movedLine.get(movedLine.size()-2)) { //Hvis man går tilbake
			if (movedLine.size()>=3) {
				movedLine.get(movedLine.size()-3).setLastMovedLine();
			}
			grid[targetY-dy][targetX-dx].setLine();
			targetTile.setMovedLine();
			movedLine.remove(movedLine.size()-1); //Fjerner siste element
		}
		else {
			targetTile.setMovedLine();
			grid[targetY-dy][targetX-dx].setLastMovedLine();
			movedLine.add(targetTile);
			if (movedLine.size()>=3) {
				movedLine.get(movedLine.size()-3).setMovedLine(); //ruten to steg bak den siste skal bli "vanlig" igjen slik at den får kollisjon
			}
		}
		if (isGameWon) {
			for (int i=0;i<movedLine.size();i++) {
				movedLine.get(i).setMovedLine();
			}
		}
	}

    public void moveUp() {
    	moveOrder.add("Up"); //Må kanskje fikses senere
        drawLine(0, -1);
	    System.out.println(this);
    }

    public void moveDown() {
    	moveOrder.add("Down");
        drawLine(0, 1);
	    System.out.println(this);
    }

    public void moveLeft() {
    	moveOrder.add("Left");
        drawLine(-1, 0);
	    System.out.println(this);
    }

    public void moveRight() {
    	moveOrder.add("Right");
        drawLine(1, 0);
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
    
    private boolean checkCorrectPath() {                                       //WTF?????
    	List<Set<Tile>> partitions = new ArrayList<Set<Tile>>();    	
    	partitions.add(new HashSet<Tile>());
    	partitions.add(new HashSet<Tile>());
    	
    	for (int i=0;i<movedLine.size();i++) {
    		AtomicInteger nextX1 = new AtomicInteger(0); //AtomicInteger hjelper med objektreferanser
        	AtomicInteger nextY1 = new AtomicInteger(0);
        	AtomicInteger nextX2 = new AtomicInteger(0);
        	AtomicInteger nextY2 = new AtomicInteger(0);
    		AtomicInteger counterObject = new AtomicInteger(0);
    		//int counter = 0;
    		//Tile nextTile = new Tile();
    		//(movedLine.get(i+1).getX()-movedLine.get(i).getX()!=1 && movedLine.get(i+1).getY()-movedLine.get(i).getY()!=1) ||
    		if (moveOrder.get(i).equals("Up")) {
    			nextX1.set(-1);   				
    			nextX2.set(1);
    			nextY1=counterObject;
    			nextY2=counterObject;
    		}
    		if (moveOrder.get(i).equals("Down")) {
    			nextX1.set(-1);    				
    			nextX2.set(1);
    			nextY1=counterObject;
    			nextY2=counterObject;
    		}
    		if (moveOrder.get(i).equals("Left")) {
    			//nextX1=counterObject;    				
    			//nextX2=counterObject;
    			nextY1.set(-1);
    			nextY2.set(1);
    		}
    		if (moveOrder.get(i).equals("Right")) { //Funker bare rett ved siden av
    			//nextX1=counterObject;    				
    			//nextX2=counterObject;
    			nextY1.set(1);
    			nextY2.set(-1);
    		}
    		//Tile[][] nextTile = new Tile[movedLine.get(i).getX()+nextX1.get()][movedLine.get(i).getY()-nextY1.get()];
    		//System.out.println(nextTile);
    		while (isTile(movedLine.get(i).getX()+nextX1.get(),movedLine.get(i).getY()-nextY1.get()) && !getTile(movedLine.get(i).getX()+nextX1.get(),movedLine.get(i).getY()-nextY1.get()).isMovedLine()) { 
    			if (getTile(movedLine.get(i).getX()+nextX1.get(),movedLine.get(i).getY()-nextY1.get()).isWhite() || getTile(movedLine.get(i).getX()+nextX1.get(),movedLine.get(i).getY()-nextY1.get()).isBlack()) {
    				partitions.get(0).add(getTile(movedLine.get(i).getX()+nextX1.get(),movedLine.get(i).getY()-nextY1.get()));
    			}
    			//counter++;
    			counterObject.getAndIncrement();
    		}
    		while (isTile(movedLine.get(i).getX()+nextX2.get(),movedLine.get(i).getY()-nextY2.get()) && !getTile(movedLine.get(i).getX()+nextX2.get(),movedLine.get(i).getY()-nextY2.get()).isMovedLine()) {
    			if (getTile(movedLine.get(i).getX()+nextX2.get(),movedLine.get(i).getY()-nextY2.get()).isWhite() || getTile(movedLine.get(i).getX()+nextX2.get(),movedLine.get(i).getY()-nextY2.get()).isBlack()) {
    				partitions.get(1).add(getTile(movedLine.get(i).getX()+nextX2.get(),movedLine.get(i).getY()-nextY2.get()));
    			}
    			//counter++;
    			counterObject.getAndIncrement();
    		}
    	}
    	System.out.println(partitions.get(0));
    	System.out.println(partitions.get(1));
    	if (partitions.get(0).size()>=2 || partitions.get(1).size()>=2) {
    		return false;
    	}
    	return true;
    }
    
    private boolean validateMove(int x, int y, Tile targetTile, boolean throwException) {		
		if (!isTile(x, y)) {
			if (throwException) {
				throw new IllegalArgumentException("Invalid move");
			}
			return false;
		}
		if (isGameWon) {
			if (throwException) {
				throw new IllegalStateException("Game is already completed");
			}
			return false;
		}
		if (targetTile.hasCollision()) {
			throw new IllegalArgumentException("Invalid move");
		}
		return true;
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
		// Oppgave 5b
		if (isGameWon) {
			boardString += "\n\nGame won!";
		}
		return boardString;
    }
	
	public static void main(String[] args) {
		 Game game = new Game(7, 7);
		 
		 game.getTile(1, 1).setWhite();
		 game.getTile(5,5).setBlack();
		 game.getTile(0, game.getHeight()-1).setStart();
		 game.getTile(game.getWidth()-1, 0).setGoal();
		 System.out.println(game);
		 game.moveUp();
		 game.moveUp();
		 game.moveRight();
		 game.moveRight();
		 game.moveRight();
		 game.moveRight();
		 game.moveRight();
		 //game.moveRight();
		 game.moveUp();
		 game.moveUp();
		 //game.moveLeft();
		 game.moveLeft();
		 game.moveLeft();
		 game.moveLeft();
		 game.moveLeft();
		 game.moveLeft();
		 game.moveUp();
		 game.moveUp();
		 game.moveRight();
		 game.moveRight();
		 game.moveRight();
		 game.moveRight();
		 game.moveRight();
		 game.moveRight();
		 game.moveRight();
		 game.moveRight();
		 System.out.println(game.isGameWon);
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

package theWitness;

public class Tile {
	
	private char type = ' ';
    private int x;
    private int y;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void setType(char symbol) {
    	if ("|-=0#<>S@_".indexOf(symbol) == -1) {
    		throw new IllegalArgumentException("Not a valid tile-type");
    	}
		type=symbol;		
	}

    public void setLine() {
    	if (x%2==0) {
    		type = '|';
    	}
    	else {
    		type = '-';
    	}
    }
    
    public char getType() {
    	return type;
    }

    public void setMovedLine() {
        type = '=';
    }
    
    public void setLastMovedLine() {
    	type = '0';
    }

    public void setBlock() {
        type = '#';
    }
    
    public void setWhite() {
    	type = '<';
    }
    
    public void setBlack() {
    	type = '>';
    }
    
    public void setBlank() {
    	type = '_';
    }

    public void setStart() {
        type = 'S';
    }

    public void setGoal() {
        type = '@';
    }

    public boolean isLine() {
        return type == '|';
    }

    public boolean isMovedLine() {
        return type == '=';
    }
    
    public boolean isLastMovedLine() {
    	return type == '0';
    }

    public boolean isBlock() {
        return type == '#';
    }
    
    public boolean isWhite() {
    	return type == '<';
    }
    
    public boolean isBlack() {
    	return type == '>';
    }

    public boolean isBlank() {
        return type == '_';
    }
    
    public boolean isStart() {
    	return type == 'S';
    }

    public boolean isGoal() {
        return type == '@';
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean hasCollision() {
        return isMovedLine() || isBlock() || isBlank() || isWhite() || isBlack();
    }

    @Override
    public String toString() {
        return Character.toString(type);
    }
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

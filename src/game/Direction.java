package game;

import java.awt.Point;

public enum Direction {
	UP,DOWN,LEFT,RIGHT;

	// Bug corrected, was always returning the same value
	public Direction randomSuccessor() {
	 // get random value between 1 and 3
		int increment=(int)(Math.random()*3)+1;	
		return values()[(ordinal()+increment)%values().length];
	}
	public Direction opposite(){
		switch(this){
		case UP: return DOWN;
		case DOWN: return UP;
		case LEFT: return RIGHT;
		case RIGHT: return LEFT;
		}
		// Will never happen
		return null;
	}
	
}

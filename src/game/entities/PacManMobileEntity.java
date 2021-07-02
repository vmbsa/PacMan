package game.entities;

import java.awt.Image;
import java.awt.Point;

import game.Board;

public abstract class PacManMobileEntity extends PacManEntity{
	public PacManMobileEntity(Board board, int x, int y) {
		super(board, x, y);
	}

	public void setLocation(Point p) {
		location=p;
	}
	
	public Point getLocation(Point p) {
		return p;
	}
	
}

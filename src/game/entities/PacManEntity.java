package game.entities;

import java.awt.Image;
import java.awt.Point;

import game.Board;

public abstract class PacManEntity extends Thread{
	public Board board;

	protected Board getBoard() {
		return board;
	}

	public Point getLocation() {
		return location;
	}

	protected Point location;

	public PacManEntity(Board board, int x, int y) {
		super();
		this.board = board;
		location=new Point(x, y);
	}

	public abstract Image getImage();
	
	
}

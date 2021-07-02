package game.entities;

import java.awt.Image;

import javax.swing.ImageIcon;

import game.Board;
import game.Direction;

public class Wall extends PacManEntity{

	private static final Image image = new ImageIcon("obstacle.png").getImage();
	
	@Override
	public Image getImage() {
		return image;
	}
	
	public Wall(Board board, int x, int y) {
		super(board, x, y);
	}	
}

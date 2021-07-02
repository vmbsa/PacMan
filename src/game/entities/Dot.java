package game.entities;

import game.Board;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Dot extends FloorEntity {

	private static final Image image = new ImageIcon("dot.png").getImage();
	public Dot(Board board, int x, int y) {
		super(board, x, y);
	}

	@Override
	public Image getImage() {
		return image;
	}

}

package game.entities;

import game.Board;

import java.awt.Image;

import javax.swing.ImageIcon;

public class InactivateGhosts extends FloorEntity {

	private static final Image image = new ImageIcon("target.png").getImage();
	public InactivateGhosts(Board board, int x, int y) {
		super(board, x, y);
	}

	@Override
	public Image getImage() {
		return image;
	}

}

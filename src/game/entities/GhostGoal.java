package game.entities;

import game.Board;

import java.awt.Image;

import javax.swing.ImageIcon;

public class GhostGoal extends FloorEntity {

	private static final Image image = new ImageIcon("ghostGoal.png").getImage();
	public GhostGoal(Board board, int x, int y) {
		super(board, x, y);
	}

	@Override
	public Image getImage() {
		return image;
	}

}

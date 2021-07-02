package game.entities;

import game.Board;

import java.awt.Image;

import javax.swing.ImageIcon;

public class GhostPlacement extends FloorEntity {

	private static final Image image = new ImageIcon("ghostPlacement.png").getImage();
	public GhostPlacement(Board board, int x, int y) {
		super(board, x, y);
	}

	@Override
	public Image getImage() {
		return image;
	}

}

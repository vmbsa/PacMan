package game.entities;

import game.Board;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Food extends FloorEntity {

	private static final Image image = new ImageIcon("food.png").getImage();
	public Food(Board board, int x, int y) {
		super(board, x, y);
	}

	@Override
	public Image getImage() {
		return image;
	}

}

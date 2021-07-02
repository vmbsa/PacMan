package game.entities;

import java.awt.Image;

import game.Board;
import game.entities.PacManEntity;

public abstract class FloorEntity extends PacManEntity {

	public FloorEntity(Board board, int x, int y) {
		super(board, x, y);
	}

}

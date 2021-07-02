package game.entities;

import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import game.Board;
import game.Direction;
import game.Servidor.GhostComLigacaoRemota;

public class Ghost extends PacManMobileEntity {
	public static final long UPDATE_INTERVAL = 500; // ms
	private static final int GHOST_PLACEMENT_TIME_INTERVAL = 2000;
	private static final Image image = new ImageIcon("240px-Pac-Man_Cutscene.svg.png").getImage();
	private static final Image imageInactive = new ImageIcon("ghost.png").getImage();
	private boolean isInactive = false;
	private boolean isDead = false;
	private GhostComLigacaoRemota gr;

	@Override
	public Image getImage() {
		if (isInactive)
			return imageInactive;
		return image;
	}

	public Ghost(Board board, GhostComLigacaoRemota gr) {
		super(board, -1, -1);
		this.gr = gr;
	}

	@Override
	public void run() {
		try {
			super.getBoard().placeGhost(this);
			Thread.sleep(GHOST_PLACEMENT_TIME_INTERVAL);
			while (!isDead()) {
				while (!isInactive()) {
					super.getBoard().moveTo(this, getPossibleDirectionsAndPickOne());
					Thread.sleep(UPDATE_INTERVAL);
				}

				while (isInactive) {
					barrier(super.getBoard().getBarrier());
					super.getBoard().moveTo(this, getPossibleDirectionsAndPickOne());
					Thread.sleep(UPDATE_INTERVAL + 100);
				}
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	private Direction getPossibleDirectionsAndPickOne(){
		List<Direction> list = new ArrayList<Direction>();
		for (Direction d : Direction.values()) {
			if (super.getBoard().canMoveTo(this, d))
				list.add(d);
		}
		try {
			return gr.sendDirections(list);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public void barrier(Barrier b) {
        if (super.getBoard().getFloor(this.getLocation().x, this.getLocation().y) instanceof GhostGoal) {
        try {
            b.barrierWait();
            super.getBoard().setAllGhostsActive();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        }
	}
	
	
	public boolean isDead() {
		return isDead;
	}

	public void setInactive() {
		isInactive = true;
	}

	public boolean isInactive() {
		return isInactive;
	}

	public void setActive() {
		isInactive = false;
	}
	

	public Thread getThread() {
		return currentThread();
	}

}

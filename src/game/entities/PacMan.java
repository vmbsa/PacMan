package game.entities;

import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import game.Board;
import game.Direction;

public class PacMan extends PacManMobileEntity {
	public static final long UPDATE_INTERVAL = 200; // ms
	private static final Image image = new ImageIcon("240px-Pacman_HD.png").getImage();
	private boolean isDead = false;
	private boolean won = false;
	private Direction direction;

	public boolean isDead() {
		return isDead;
	}

	public void setDead() {
		this.isDead = true;
	}

	public boolean won() {
		return won;
	}

	public void winGame() {
		this.won = true;
	}

	public int eatDots(FloorEntity[][] floor, int dots) {
		Point ps = this.getLocation();
		if (floor[ps.y][ps.x] instanceof Dot) {
			floor[ps.y][ps.x] = null;
			dots--;
		}
		if (dots == 0) {
			winGame();
		}
		return dots;
	}

	public void eatPower(FloorEntity[][] floor, ArrayList<Ghost> ghosts) {
		Point ps = this.getLocation();
		if (floor[ps.y][ps.x] instanceof InactivateGhosts) {
			floor[ps.y][ps.x] = null;
			for (Ghost g : ghosts) {
				g.setInactive();
			}
		}
	}

	@Override
	public Image getImage() {
		return image;
	}

	public PacMan(Board board, int x, int y) {
		super(board, x, y);
	}

	@Override
	public void run() {
		while (!isDead() && !won()) {
			try {
				super.getBoard().moveTo(this, direction);
				Thread.sleep(UPDATE_INTERVAL);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		if(isDead())
			System.out.println("GAME OVER");
		else if(won()) {
			System.out.println("YOU WON CHAMP");
		}
		return;
	}
	
	
	public void eatGhost(PacManEntity[][] board, ArrayList<Ghost> ghosts) {
		for (Ghost g : ghosts) {
			if(g.isInactive() && this.getLocation().equals(g.getLocation())) {
				g.setLocation(new Point(19,5));
				board[5][19] = g;
				g.setActive();
			}
		}
		
	}
	

	public void setDirection(Direction dir) {
		this.direction = dir;
	}

}

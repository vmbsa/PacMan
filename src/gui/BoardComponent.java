package gui;
import game.Board;
import game.Direction;
import game.entities.FloorEntity;
import game.entities.Ghost;
import game.entities.PacMan;
import game.entities.PacManEntity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public class BoardComponent extends JComponent implements KeyListener {

	private static final int CELL_SIZE = 20;
	private Board board;


	public BoardComponent(Board board) {
		super();
		this.board = board;
		setFocusable(true);
		addKeyListener(this);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		//DEBUG
		//		System.err.println("Repainting.");

		for (int x = 0; x < board.getWidth(); x++) {
			for (int y = 0; y < board.getHeight(); y++) {
				FloorEntity floor = board.getFloor(x, y);
				Image image = null;
				if(floor!=null)
					image=floor.getImage();
				PacManEntity entity = board.getEntity(x, y);
				if(entity!=null)
					image=entity.getImage();

				if(image!=null){
					g.drawImage(image, x * CELL_SIZE, y	* CELL_SIZE, CELL_SIZE,
							CELL_SIZE, null);
				}
			}
			//			g.drawLine(x * CELL_SIZE, 0, x * CELL_SIZE,
			//					getHeight());
		}
		//		for (int y = 1; y < board.getHeight(); y++) {
		//			g.drawLine(0, y * CELL_SIZE, getWidth(), y
		//					* CELL_SIZE);
		//		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_LEFT :
			board.changePacmanDirection(Direction.LEFT);
			break;
		case KeyEvent.VK_RIGHT:
			board.changePacmanDirection(Direction.RIGHT);
			break;
		case KeyEvent.VK_UP:
			board.changePacmanDirection(Direction.UP);
			break;
		case KeyEvent.VK_DOWN:
			board.changePacmanDirection(Direction.DOWN);
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode()!=KeyEvent.VK_LEFT && e.getKeyCode()!=KeyEvent.VK_RIGHT && 
				e.getKeyCode()!=KeyEvent.VK_UP && e.getKeyCode()!=KeyEvent.VK_DOWN ) 
			return; // ignore
		board.changePacmanDirection(null);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		// Ignored...
	}


}

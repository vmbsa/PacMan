package gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;
import game.Board;

import javax.swing.JFrame;

public class GameWindow implements Observer {
	public static final int CELL_SIZE = 20;
	private JFrame frame = new JFrame("PacMan");
	private BoardComponent boardComponent;
	private Board board;

	public GameWindow(Board board) {
		super();
		this.board=board;
		board.addObserver(this);
		buildGui();
	}
	

	private void buildGui() {
		boardComponent = new BoardComponent(board);
		frame.add(boardComponent);

		frame.setSize((board.getWidth()) * CELL_SIZE+17, (board.getHeight())* CELL_SIZE+40);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		
	}

	

	@Override
	public void update(Observable o, Object arg) {
		if(boardComponent!=null){
			boardComponent.repaint();
		}
	}

	
}

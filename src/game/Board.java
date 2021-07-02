package game;

import game.entities.Barrier;
import game.entities.Dot;
import game.entities.FloorEntity;
import game.entities.Food;
import game.entities.Ghost;
import game.entities.GhostGoal;
import game.entities.GhostPlacement;
import game.entities.InactivateGhosts;
import game.entities.PacMan;
import game.entities.PacManEntity;
import game.entities.PacManMobileEntity;
import game.entities.Wall;
import gui.BoardComponent;
import gui.GameWindow;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Board extends Observable {
	
	public static final int NUM_GHOSTS_TO_ACTIVATE_ALL = 2;
	public static final boolean SHOW_DOTS = true;
	private Barrier b = new Barrier( NUM_GHOSTS_TO_ACTIVATE_ALL);

	private int dimX;
	private int dimY;
	private List<String> lines = new LinkedList<String>();
	
	private int numRemainingDots = 0;
	
	private PacManEntity[][] board;
	
	private FloorEntity[][] floor;

	private Point ghostInitialPlacementCell;
	private PacMan pacman;
	private ArrayList<Ghost> ghosts = new ArrayList<Ghost>();
	private BoardComponent boardComponent = new BoardComponent(this);

	public Board(String boardDefinition) throws IOException {
		processDefinitionInFile(boardDefinition);

	}

	private void processDefinitionInFile(String boardDefinition) throws IOException {
		File file = new File(boardDefinition);
		if (!file.exists())
			throw new InvalidParameterException("Definition file does not exist");

		BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader(new FileReader(file));

			dimX = -1;
			// read to a string list to know number of lines and columns
			for (String strCurrentLine = bufferedReader
					.readLine(); strCurrentLine != null; strCurrentLine = bufferedReader.readLine()) {
				if (dimX == -1)// first line
					dimX = strCurrentLine.length();
				else if (strCurrentLine.length() != dimX)
					throw new InvalidParameterException("Definition file has lines with different lengths");
				lines.add(strCurrentLine);
				dimY++;
			}
			// First index of arrays is Y, seems more intuitive
			board = new PacManEntity[dimY][dimX];
			floor = new FloorEntity[dimY][dimX];

			for (int y = 0; y != dimY; y++)
				for (int x = 0; x != lines.get(y).length(); x++)
					if (lines.get(y).charAt(x) == 'W')// Wall
						board[y][x] = new Wall(this, x, y);
					else if (lines.get(y).charAt(x) == 'P') {// PacMan
						// only one pacman possible
						if (pacman != null)
							throw new InvalidParameterException("Duplicate Pacman");
						pacman = new PacMan(this, x, y);
						board[y][x] = pacman;
						Thread p = new Thread(pacman);
						p.start();
					} else if (lines.get(y).charAt(x) == 'M') {// GhostArea
						if (ghostInitialPlacementCell != null)// duplicate value
							throw new InvalidParameterException("Duplicate ghost placement");
						ghostInitialPlacementCell = new Point(x, y);
						floor[y][x] = new GhostPlacement(this, x, y);
					} else if (lines.get(y).charAt(x) == 'F')// food
						floor[y][x] = new Food(this, x, y);
					else if (lines.get(y).charAt(x) == 'K')// Inactivate Ghosts
						floor[y][x] = new InactivateGhosts(this, x, y);
					else if (lines.get(y).charAt(x) == 'G')// Ghost goal
						floor[y][x] = new GhostGoal(this, x, y);
					else {// dot
						if (SHOW_DOTS) {
							floor[y][x] = new Dot(this, x, y);
							numRemainingDots++;
						}
					}
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			// Will never happen, is checked beforehand
		}
	}

	public int getWidth() {
		return dimX;
	}

	public int getHeight() {
		return dimY;
	}

	public boolean isOccupied() {
		for (Ghost g : ghosts) {
			if (g.getLocation().equals(ghostInitialPlacementCell)) {
				return true;
			}
		}
		return false;
	}

	public synchronized void placeGhost(Ghost ghost) throws InterruptedException {
		while (isOccupied()) {
			wait();
		}
		addToListOfGhosts(ghost);
		board[5][19] = ghost;
		ghost.setLocation(new Point(19, 5));
	}

	public boolean canMoveTo(PacManMobileEntity entity, Direction direction) {
		// TODO
		Point p = entity.getLocation();
		Point destino = p;
		if (direction == Direction.UP)
			destino = new Point(p.x, p.y - 1);
		if (direction == Direction.DOWN)
			destino = new Point(p.x, p.y + 1);
		if (direction == Direction.LEFT)
			destino = new Point(p.x - 1, p.y);
		if (direction == Direction.RIGHT)
			destino = new Point(p.x + 1, p.y);

		if (entity instanceof PacMan) {
			if (lines.get(destino.y).charAt(destino.x) == 'W' || lines.get(destino.y).charAt(destino.x) == 'G'
					|| lines.get(destino.y).charAt(destino.x) == 'M')
				return false;
		}
		if (entity instanceof Ghost) {
			if (((Ghost) entity).isInactive() == false
					&& (lines.get(destino.y).charAt(destino.x) == 'W' || lines.get(destino.y).charAt(destino.x) == 'G'))
				return false;
			if (((Ghost) entity).isInactive() == true) {
				if (lines.get(destino.y).charAt(destino.x) == 'W')
					return false;
				if (lines.get(destino.y).charAt(destino.x) == 'G' && board[destino.y][destino.x] instanceof Ghost) {
					return false;
				}
			}
		}
		return true;
	}

	
	public synchronized void moveTo(PacManMobileEntity entity, Direction direction) {
		// TODO
		Point actual = entity.getLocation();
		if (canMoveTo(entity, direction)) {
			board[actual.y][actual.x] = null;
			if (direction == Direction.UP)
				actual.y -= 1;
			if (direction == Direction.DOWN)
				actual.y += 1;
			if (direction == Direction.LEFT)
				actual.x -= 1;
			if (direction == Direction.RIGHT)
				actual.x += 1;

			if (entity instanceof PacMan) {
				entity.setLocation(actual);
				board[actual.y][actual.x] = pacman;
				numRemainingDots = pacman.eatDots(floor, numRemainingDots);
				pacman.eatPower(floor, ghosts);
				pacman.eatGhost(board, ghosts);
			}

			if (entity instanceof Ghost) {
				entity.setLocation(actual);
				board[actual.y][actual.x] = entity;
				notifyAll();
			}
			killPacman();
		}
		setChanged();
		notifyObservers();
	}
	
	public void killPacman() {
		for (Ghost ghost : ghosts) {
			if (pacman.getLocation().equals(ghost.getLocation()) && !ghost.isInactive()) 
				pacman.setDead();
		}
	}

	public void changePacmanDirection(Direction dir) {
		pacman.setDirection(dir);
	}

	public boolean gameFinished() {
		return (SHOW_DOTS && numRemainingDots == 0) || pacman.isDead();
	}

	public FloorEntity getFloor(int x, int y) {
		return floor[y][x];
	}

	public PacManEntity getEntity(int x, int y) {
		return board[y][x];
	}

	public void addToListOfGhosts(Ghost g) {
		ghosts.add(g);
	}
	
	public Barrier getBarrier() {
		return b;
	}
	
	public void setAllGhostsActive(){
		for(Ghost g : ghosts) 
			g.setActive();
	}
	

}

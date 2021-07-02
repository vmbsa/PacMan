package game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import game.entities.Ghost;

public class Servidor {

	private ObjectInputStream in;
	private ObjectOutputStream out;
	public static final int PORTO = 8080;
	private Board board;

	public Servidor(Board board) {
		this.board = board;
	}

	
	
	public class GhostComLigacaoRemota extends Thread {
		private ObjectInputStream in;
		private ObjectOutputStream out;

		public GhostComLigacaoRemota(Socket socket) throws IOException {
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			
			Ghost ghost = new Ghost(board, this);
			ghost.start();
		}

		@Override
		public void run() {
			
		}
		
		public Direction sendDirections(List<Direction> list) throws IOException {
			out.writeObject(list);
			return recieveDirection();
		}
		
		public Direction recieveDirection() {
			Direction d = null;
			try {
				d = (Direction)in.readObject();
				System.out.println("ghost " + Ghost.currentThread().getId() + " is going " + d);
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return d;
		}
		
	}
	

	
	
	

	public void startServing() throws IOException {
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(PORTO);
			Socket socket = null;
			while (true) {
				socket = ss.accept();
				new GhostComLigacaoRemota(socket).start();
			}
		} finally {
			ss.close();
		}
	}

}

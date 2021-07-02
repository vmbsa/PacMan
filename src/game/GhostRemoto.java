package game;

import java.io.*;
import java.net.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GhostRemoto {
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Socket socket;
	List<Direction> aux = new ArrayList<Direction>() {
		{
			add(Direction.UP);
		}
	};
	Direction d = Direction.UP;

	public static void main(String[] args) {
		new GhostRemoto().runClient();
	}

	public void runClient() {
		try {
			connectToServer();
			recieveDirections();
		} catch (IOException e) {// ERRO...
		} finally {// a fechar...
			try {
				socket.close();
			} catch (IOException e) {// ...
			}
		}
	}

	void connectToServer() throws IOException {
		InetAddress endereco = InetAddress.getByName(null);
		socket = new Socket(endereco, Servidor.PORTO);
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
	}

	public void recieveDirections() {
		while (true) {
			try {
				List<Direction> lista = (List<Direction>) in.readObject();
				sendDirection(lista);
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Game was closed. Worten sempre");
				break;
			}
		}
	}

	public void sendDirection(List<Direction> list) {
		if (list.equals(aux) && list.contains(d)) {

		} else {
			aux = list;
			int x = (int) (Math.random() * list.size());
			d = list.get(x);
		}
		try {
			out.writeObject(d);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

package gui;

import game.Board;
import game.GhostRemoto;
import game.Servidor;
import game.entities.Barrier;
import game.entities.Ghost;

import java.io.IOException;

public class Main {
    private static final String boardDefinition = "boardDefinition.txt";

    public static void main(String[] args) {

        try {
            final Board board= new Board(boardDefinition); 
            GameWindow gw = new GameWindow(board);
            Servidor servidor = new Servidor(board);
            servidor.startServing();

        } catch (IOException e) {
            System.out.println("Problem launching game.");
            e.printStackTrace();
        }
    }
}
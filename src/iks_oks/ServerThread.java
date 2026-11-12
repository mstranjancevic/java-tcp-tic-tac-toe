package iks_oks;

import java.io.*;
import java.net.*;

public class ServerThread extends Thread {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private server gameServer;
    private int playerId;
    private char znakIgraca;

    public ServerThread(Socket socket, server gameServer, int playerId) {
        this.socket = socket;
        this.gameServer = gameServer;
        this.playerId = playerId;
        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setZnakIgraca(char znak) {
        this.znakIgraca = znak;
        if (out != null) {
            out.println("Dobrodošli! Igrate kao " + znak);
        }
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String input = in.readLine();
                if (input == null) {
                    break;
                }
                gameServer.processMove(playerId, input);
            }
        } catch (IOException e) {
            System.out.println("Igrač " + playerId + " se diskonektovao.");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            gameServer.playerDisconnected(playerId);
        }
    }
}
package iks_oks;

import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.Scanner;

public class server {
    private static final int PORT = 2010;
    private char[][] tabela = {{' ', ' ', ' '}, {' ', ' ', ' '}, {' ', ' ', ' '}};
    private ServerThread[] igraci = new ServerThread[2];
    private char[] znakovi = new char[2];
    private int naPotezu;
    private boolean gameStarted = false;

    public static void main(String[] args) {
        new server().startServer();
    }

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server je pokrenut na portu " + PORT);

            for (int i = 0; i < 2; i++) {
                Socket socket = serverSocket.accept();
                igraci[i] = new ServerThread(socket, this, i);
                System.out.println("Igrač " + (i + 1) + " se povezao.");
                igraci[i].start();
            }

            Random rand = new Random();
            int first = rand.nextInt(2);
            znakovi[first] = 'X';
            znakovi[1 - first] = 'O';
            naPotezu = first;

            igraci[first].setZnakIgraca('X');
            igraci[1 - first].setZnakIgraca('O');

            new Thread(this::handleServerCommands).start();

            System.out.println("Unesite 'Start' da započnete igru:");
            Scanner scanner = new Scanner(System.in);
            while (!scanner.nextLine().equalsIgnoreCase("Start")) {
                System.out.println("Unesite 'Start' da pokrenete igru.");
            }

            startGame();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleServerCommands() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine();
            broadcastMessage("[SERVER] " + command);
        }
    }

    public synchronized void processMove(int playerId, String move) {
        if (!gameStarted || playerId != naPotezu) {
            igraci[playerId].sendMessage("Nije vaš red za igru!");
            return;
        }

        String[] parts = move.split(",");
        if (parts.length != 2) {
            igraci[playerId].sendMessage("Nepravilan unos! Format: i,j");
            igraci[playerId].sendMessage("Vaš potez (red, kolona):");
            return;
        }

        int row, col;
        try {
            row = Integer.parseInt(parts[0].trim());
            col = Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException e) {
            igraci[playerId].sendMessage("Nepravilan unos! Koristite brojeve 0-2.");
            igraci[playerId].sendMessage("Vaš potez (red, kolona):");
            return;
        }

        if (row < 0 || row > 2 || col < 0 || col > 2 || tabela[row][col] != ' ') {
            igraci[playerId].sendMessage("Potez nije validan. Pokušajte ponovo.");
            igraci[playerId].sendMessage("Vaš potez (red, kolona):");
            return;
        }

        tabela[row][col] = znakovi[playerId];
        sendBoardToPlayers();

        if (checkWin(znakovi[playerId])) {
            igraci[playerId].sendMessage("Čestitamo! Pobedili ste!");
            igraci[1 - playerId].sendMessage("Kraj igre! Pobedio je protivnik.");
            gameStarted = false;
            return;
        }

        if (isBoardFull()) {
            broadcastMessage("Kraj igre! Nerešeno.");
            gameStarted = false;
            return;
        }

        naPotezu = 1 - naPotezu;
        igraci[naPotezu].sendMessage("Vaš potez (red, kolona):");
    }

    private void startGame() {
        gameStarted = true;
        broadcastMessage("start");
        sendBoardToPlayers();
        igraci[naPotezu].sendMessage("Vaš potez (red, kolona):");
    }

    private void sendBoardToPlayers() {
        broadcastMessage("Trenutno stanje table:");
        for (char[] row : tabela) {
            broadcastMessage("| " + row[0] + " | " + row[1] + " | " + row[2] + " |");
        }
    }

    private void broadcastMessage(String message) {
        for (ServerThread player : igraci) {
            if (player != null) {
                player.sendMessage(message);
            }
        }
    }

    public void playerDisconnected(int playerId) {
        System.out.println("Igrač " + (playerId + 1) + " se diskonektovao.");
        int otherPlayer = 1 - playerId;
        if (igraci[otherPlayer] != null) {
            igraci[otherPlayer].sendMessage("Protivnik se diskonektovao. Igra je prekinuta.");
        }
        gameStarted = false;
    }

    private boolean checkWin(char symbol) {
        for (int i = 0; i < 3; i++) {
            if ((tabela[i][0] == symbol && tabela[i][1] == symbol && tabela[i][2] == symbol) ||
                    (tabela[0][i] == symbol && tabela[1][i] == symbol && tabela[2][i] == symbol)) {
                return true;
            }
        }
        return (tabela[0][0] == symbol && tabela[1][1] == symbol && tabela[2][2] == symbol) ||
                (tabela[0][2] == symbol && tabela[1][1] == symbol && tabela[2][0] == symbol);
    }

    private boolean isBoardFull() {
        for (char[] row : tabela) {
            for (char cell : row) {
                if (cell == ' ') {
                    return false;
                }
            }
        }
        return true;
    }
}
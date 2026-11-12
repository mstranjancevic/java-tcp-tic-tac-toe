package iks_oks;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class igrac {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 2010;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Povezani ste na server. Čekanje drugog igrača...");

            String playerSymbol = in.readLine();
            System.out.println(playerSymbol);

            while (true) {
                String serverMessage = in.readLine();
                if (serverMessage == null) break;

                if (serverMessage.startsWith("[SERVER]")) {
                    System.out.println(serverMessage);
                    continue;
                }

                if ("Start".equalsIgnoreCase(serverMessage)) {
                    System.out.println("Igra počinje!");
                    continue;
                }

                if (serverMessage.startsWith("Trenutno stanje table:")) {
                    System.out.println(serverMessage);
                    for (int i = 0; i < 3; i++) {
                        System.out.println(in.readLine());
                    }
                    continue;
                }

                System.out.println(serverMessage);

                if (serverMessage.contains("Vaš potez")) {
                    System.out.print("Unesite potez (format i,j): ");
                    String move = scanner.nextLine();
                    out.println(move);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
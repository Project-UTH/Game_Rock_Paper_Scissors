package com.rpsgame;

import java.io.*;
import java.net.*;
import java.util.*;

public class RpsServer {
    private static final int PORT = 12345;
    private static List<ClientHandler> clients = new ArrayList<>();
    private static final Object lock = new Object();

    public static void main(String[] args) {
        System.out.println("Rock-Paper-Scissors Server started on port " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                // Accept two clients for a game
                if (clients.size() < 2) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New client connected: " + clientSocket.getInetAddress());
                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    clients.add(clientHandler);
                    clientHandler.start();
                }
                // When two clients are connected, start the game
                synchronized (lock) {
                    if (clients.size() == 2) {
                        playGame();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void playGame() {
        // Notify clients that the game starts
        broadcast("Game starts! Choose: ROCK, PAPER, or SCISSORS");

        // Wait for choices (handled in ClientHandler)
    }

    private static void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String choice;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Received from client: " + message);
                    choice = message.toUpperCase();
                    checkIfBothChose();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                clients.remove(this);
            }
        }

        public void sendMessage(String message) {
            out.println(message);
        }

        private void checkIfBothChose() {
            synchronized (lock) {
                boolean bothChose = true;
                for (ClientHandler client : clients) {
                    if (client.choice == null) {
                        bothChose = false;
                        break;
                    }
                }
                if (bothChose) {
                    determineWinner();
                    // Reset for next game
                    for (ClientHandler client : clients) {
                        client.choice = null;
                    }
                    broadcast("Choose again for next round: ROCK, PAPER, or SCISSORS");
                }
            }
        }

        private void determineWinner() {
            ClientHandler player1 = clients.get(0);
            ClientHandler player2 = clients.get(1);
            String choice1 = player1.choice;
            String choice2 = player2.choice;

            String result;
            if (choice1.equals(choice2)) {
                result = "It's a tie! Both chose " + choice1;
            } else if (
                (choice1.equals("ROCK") && choice2.equals("SCISSORS")) ||
                (choice1.equals("SCISSORS") && choice2.equals("PAPER")) ||
                (choice1.equals("PAPER") && choice2.equals("ROCK"))
            ) {
                result = "Player 1 wins! " + choice1 + " beats " + choice2;
            } else {
                result = "Player 2 wins! " + choice2 + " beats " + choice1;
            }

            broadcast(result);
        }
    }
}
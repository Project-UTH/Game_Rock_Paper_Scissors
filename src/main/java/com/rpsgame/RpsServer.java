package com.rpsgame;

import java.io.*;
import java.net.*;
import java.util.*;

public class RpsServer {
    private static final int PORT = 12345;
    private static final Queue<ClientHandler> waitingClients = new LinkedList<>();
    private static final Object lock = new Object();

    public static void main(String[] args) {
        System.out.println("Rock-Paper-Scissors Server started on port " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.start();  // Khoi dong thread de lang nghe tu client

                // Them vao hang doi va kiem tra ghep doi
                synchronized (lock) {
                    waitingClients.add(clientHandler);
                    if (waitingClients.size() >= 2) {
                        ClientHandler player1 = waitingClients.poll();
                        ClientHandler player2 = waitingClients.poll();
                        // Bat dau game rieng cho cap nay trong thread moi
                        new Thread(new Game(player1, player2)).start();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Lop Game moi: Xu ly game cho mot cap client (vo han round nhu cu)
    private static class Game implements Runnable {
        private ClientHandler player1;
        private ClientHandler player2;
        private final Object gameLock = new Object();  // Dong bo hoa cho game nay

        public Game(ClientHandler p1, ClientHandler p2) {
            this.player1 = p1;
            this.player2 = p2;
            p1.setGame(this);
            p2.setGame(this);
        }

        @Override
        public void run() {
            broadcast("Game starts! Choose: ROCK, PAPER, or SCISSORS");
            // Game loop chay o day, nhung logic cho lua chon nam o ClientHandler.run()
            // (se goi checkIfBothChose() khi nhan choice)
        }

        public void checkIfBothChose(ClientHandler sender) {
            synchronized (gameLock) {
                if (player1.getChoice() != null && player2.getChoice() != null) {
                    determineWinner();
                    // Reset choice cho round tiep theo
                    player1.resetChoice();
                    player2.resetChoice();
                    broadcast("Choose again for next round: ROCK, PAPER, or SCISSORS");
                }
            }
        }

        private void determineWinner() {
            String choice1 = player1.getChoice();
            String choice2 = player2.getChoice();

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

        public void broadcast(String message) {
            if (player1 != null) player1.sendMessage(message);
            if (player2 != null) player2.sendMessage(message);
        }

        public void clientDisconnected(ClientHandler disconnected) {
            broadcast("Opponent disconnected. Game over.");
            if (disconnected == player1) {
                if (player2 != null) player2.close();
            } else {
                if (player1 != null) player1.close();
            }
        }
    }

    // ClientHandler sua de lien ket voi Game cu the
    private static class ClientHandler extends Thread {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String choice;
        private Game game;  // Game ma client nay thuoc ve

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void setGame(Game game) {
            this.game = game;
        }

        public String getChoice() {
            return choice;
        }

        public void resetChoice() {
            choice = null;
        }

        public void sendMessage(String message) {
            if (out != null) {
                out.println(message);
            }
        }

        public void close() {
            try {
                if (socket != null && !socket.isClosed()) socket.close();
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
                    if (game != null) {
                        game.checkIfBothChose(this);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (game != null) {
                    game.clientDisconnected(this);
                }
                close();
            }
        }
    }
}
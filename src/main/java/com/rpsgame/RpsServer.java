package com.rpsgame;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import com.rpsgame.game.ClientHandler;
import com.rpsgame.game.Game;

public class RpsServer {
    private static final int PORT = 12345;
    private static final List<ClientHandler> readyClients = new ArrayList<>(); // Danh sách client sẵn sàng
    private static final Object lock = new Object();

    public static void main(String[] args) {
        System.out.println("Rock-Paper-Scissors Server started on port " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Phương thức ghép đôi khi nhận READY
    public static void addReadyClient(ClientHandler client) {
        synchronized (lock) {
            readyClients.add(client);
            client.sendMessage("Đang tìm đối thủ..."); // Gửi thông báo chờ
            matchClients(); // Kiểm tra ghép đôi ngay lập tức
        }
    }

    private static void matchClients() {
        synchronized (lock) {
            if (readyClients.size() >= 2) {
                ClientHandler player1 = readyClients.remove(0);
                ClientHandler player2 = readyClients.remove(0);
                // Gán vai trò và bắt đầu game
                player1.setRole("Player 1");
                player2.setRole("Player 2");
                player1.sendMessage("Bạn là Player 1");
                player2.sendMessage("Bạn là Player 2");
                new Thread(new Game(player1, player2)).start();
            }
        }
    }
}
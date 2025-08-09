package com.rpsgame.game;

import java.io.*;
import java.net.Socket;

import com.rpsgame.RpsServer;

public class ClientHandler extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String choice;
    private Game game;
    private String role;

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

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
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
                System.out.println("Received from " + (role != null ? role : "client") + ": " + message);
                if (message.equals("READY") && game == null) {
                    RpsServer.addReadyClient(this); // Thêm vào danh sách sẵn sàng
                } else if (game != null) {
                    choice = message.toUpperCase();
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
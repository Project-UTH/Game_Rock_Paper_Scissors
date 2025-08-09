package com.rpsgame.model;

import java.io.*;
import java.net.*;

public class RpsClientModel {
    private static final String HOST = "localhost";
    private static final int PORT = 12345;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private MessageListener listener;  // Callback để thông báo message mới

    public interface MessageListener {
        void onMessageReceived(String message);
        void onError(String error);
    }

    public void setMessageListener(MessageListener listener) {
        this.listener = listener;
    }

    public void connect() {
        try {
            socket = new Socket(HOST, PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Client connected to " + HOST + ":" + PORT);

            new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        if (listener != null) {
                            listener.onMessageReceived(message);
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Client error: " + e.getMessage());
                    if (listener != null) {
                        listener.onError("Connection lost: " + e.getMessage());
                    }
                } finally {
                    cleanup(); // Đóng kết nối khi thread kết thúc
                }
            }).start();
        } catch (IOException e) {
            if (listener != null) {
                listener.onError("Could not connect to server: " + e.getMessage());
            }
        }
    }

    public void sendChoice(String choice) {
        if (out != null) {
            System.out.println("Client sending: " + choice);
            out.println(choice);
        }
    }

    public void sendReadySignal() {
        if (out != null) {
            System.out.println("Client sending: READY");
            out.println("READY");
        }
    }

    public void disconnect() {
        System.out.println("Client initiating disconnect...");
        cleanup();
        System.out.println("Client disconnected.");
    }

    private void cleanup() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
            System.out.println("Socket and streams closed.");
        } catch (IOException e) {
            System.out.println("Error during cleanup: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
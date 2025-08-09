package com.rpsgame;

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

            // Thread lắng nghe message từ server
            new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        if (listener != null) {
                            listener.onMessageReceived(message);
                        }
                    }
                } catch (IOException e) {
                    if (listener != null) {
                        listener.onError("Connection lost: " + e.getMessage());
                    }
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
            out.println(choice);
        }
    }

    public void disconnect() {
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
package com.rpsgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

public class RpsClientGui {
    private static final String HOST = "localhost";
    private static final int PORT = 12345;

    private JFrame frame;
    private JTextArea textArea;
    private JButton rockButton;
    private JButton paperButton;
    private JButton scissorsButton;

    private PrintWriter out;
    private BufferedReader in;

    public RpsClientGui() {
        frame = new JFrame("Rock-Paper-Scissors Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        rockButton = new JButton("ROCK");
        paperButton = new JButton("PAPER");
        scissorsButton = new JButton("SCISSORS");

        buttonPanel.add(rockButton);
        buttonPanel.add(paperButton);
        buttonPanel.add(scissorsButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        rockButton.addActionListener(new ChoiceListener("ROCK"));
        paperButton.addActionListener(new ChoiceListener("PAPER"));
        scissorsButton.addActionListener(new ChoiceListener("SCISSORS"));

        frame.setVisible(true);

        connectToServer();
    }

    private void connectToServer() {
        try {
            Socket socket = new Socket(HOST, PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        textArea.append(message + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Could not connect to server.");
            System.exit(1);
        }
    }

    private class ChoiceListener implements ActionListener {
        private String choice;

        public ChoiceListener(String choice) {
            this.choice = choice;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            out.println(choice);
            textArea.append("You chose: " + choice + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RpsClientGui::new);
    }
}

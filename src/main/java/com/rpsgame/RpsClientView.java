package com.rpsgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class RpsClientView extends JFrame {
    private JTextArea textArea;
    private JButton rockButton;
    private JButton paperButton;
    private JButton scissorsButton;

    public RpsClientView() {
        setTitle("Rock-Paper-Scissors Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        rockButton = new JButton("ROCK");
        paperButton = new JButton("PAPER");
        scissorsButton = new JButton("SCISSORS");

        buttonPanel.add(rockButton);
        buttonPanel.add(paperButton);
        buttonPanel.add(scissorsButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void addChoiceListener(ActionListener rockListener, ActionListener paperListener, ActionListener scissorsListener) {
        rockButton.addActionListener(rockListener);
        paperButton.addActionListener(paperListener);
        scissorsButton.addActionListener(scissorsListener);
    }

    public void appendMessage(String message) {
        textArea.append(message + "\n");
    }

    public void showError(String error) {
        JOptionPane.showMessageDialog(this, error);
    }
}
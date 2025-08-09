package com.rpsgame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RpsClientController implements RpsClientModel.MessageListener {
    private RpsClientModel model;
    private RpsClientView view;

    public RpsClientController() {
        model = new RpsClientModel();
        view = new RpsClientView();

        model.setMessageListener(this);

        // Listener cho từng button
        view.addChoiceListener(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    sendChoiceAndAppend("ROCK");
                }
            },
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    sendChoiceAndAppend("PAPER");
                }
            },
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    sendChoiceAndAppend("SCISSORS");
                }
            }
        );

        view.setVisible(true);
        model.connect();
    }

    private void sendChoiceAndAppend(String choice) {
        model.sendChoice(choice);
        view.appendMessage("You chose: " + choice);
    }

    @Override
    public void onMessageReceived(String message) {
        view.appendMessage(message);
    }

    @Override
    public void onError(String error) {
        view.showError(error);
        System.exit(1);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RpsClientController::new);
    }
}
package com.rpsgame.controller;

import com.rpsgame.model.RpsClientModel;
import com.rpsgame.view.RpsClientView;
import com.rpsgame.view.ReadyPanel;
import com.rpsgame.view.WaitingPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class RpsClientController implements RpsClientModel.MessageListener {
    private RpsClientModel model;
    private RpsClientView view;
    private ReadyPanel readyPanel;
    private WaitingPanel waitingPanel;
    private String playerRole; // Lưu vai trò của người chơi

    public RpsClientController() {
        model = new RpsClientModel();
        view = new RpsClientView();

        model.setMessageListener(this);

        readyPanel = view.getReadyPanel();
        waitingPanel = view.getWaitingPanel();

        readyPanel.addReadyListener(e -> {
            model.sendReadySignal();
            view.showWaitingScreen();
        });

        setupGameListeners();

        view.showReadyScreen();
        view.setVisible(true);
        model.connect();
    }

    private void setupGameListeners() {
        view.addChoiceListener(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    sendChoiceAndAppend("BÚA");
                }
            },
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    sendChoiceAndAppend("GIẤY");
                }
            },
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    sendChoiceAndAppend("KÉO");
                }
            }
        );

        // Thêm listener cho nút thoát với xác nhận
        view.addExitListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc muốn thoát?", "Xác nhận thoát", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.out.println("Exit button pressed, initiating disconnect...");
                model.disconnect();
                view.dispose(); // Đóng cửa sổ trước khi thoát
                SwingUtilities.invokeLater(() -> {
                    System.out.println("Exiting application...");
                    System.exit(0); // Đảm bảo thoát ứng dụng
                });
            }
        });
    }

    private void sendChoiceAndAppend(String choice) {
        model.sendChoice(choice);
        view.appendMessage("Bạn chọn: " + choice);
        view.setButtonsEnabled(false);
    }

    @Override
    public void onMessageReceived(String message) {
        SwingUtilities.invokeLater(() -> {
            System.out.println("Client received: [" + message + "], playerRole: [" + playerRole + "]"); // Log debug
            view.appendMessage(message);

            if (message.contains("Trò chơi bắt đầu!")) {
                view.showGameScreen();
            } else if (message.contains("thắng!")) {
                // Phân tích tin nhắn để xác định ai thắng
                String[] parts = message.split(" ");
                String winner = parts[0] + " " + parts[1]; // Lấy "Player 1" hoặc "Player 2"
                System.out.println("Determined winner: [" + winner + "], comparing with playerRole: [" + playerRole + "]"); // Log debug
                boolean isWin = playerRole != null && playerRole.equals(winner);
                view.showResultDialog(message, isWin); // Hiển thị dialog tạm thời
                view.setButtonsEnabled(true); // Kích hoạt lại nút để tiếp tục chơi
            } else if (message.contains("Hòa!")) {
                // Xử lý trường hợp hòa
                view.showResultDialog(message, false); // Hiển thị dialog với isWin = false
                view.setButtonsEnabled(true); // Kích hoạt lại nút để tiếp tục chơi
            } else if (message.contains("Chọn lại")) {
                view.setButtonsEnabled(true); // Đảm bảo nút luôn sẵn sàng sau mỗi round
            } else if (message.contains("Bạn là")) {
                // Cập nhật vai trò từ "Bạn là Player 1" hoặc "Bạn là Player 2"
                playerRole = message.replace("Bạn là ", "").trim(); // Lấy "Player 1" hoặc "Player 2" và loại bỏ khoảng trắng thừa
                view.updateStatus("Bạn là " + playerRole);
                System.out.println("Updated playerRole to: [" + playerRole + "]"); // Log debug
            } else if (message.contains("Đang tìm đối thủ...")) {
                waitingPanel.updateWaitingMessage(message);
            }
        });
    }

    @Override
    public void onError(String error) {
        SwingUtilities.invokeLater(() -> {
            view.showError(error);
            System.exit(1);
        });
    }
}
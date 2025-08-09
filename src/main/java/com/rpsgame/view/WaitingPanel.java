package com.rpsgame.view;

import javax.swing.*;
import java.awt.*;

public class WaitingPanel extends JPanel {
    private JLabel waitingLabel;
    private RpsClientView view;

    public WaitingPanel(RpsClientView view) {
        this.view = view;
        setBackground(RpsClientView.BACKGROUND_COLOR);
        setLayout(new GridBagLayout());
        
        waitingLabel = new JLabel("⏳ Đang tìm đối thủ...");
        waitingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        waitingLabel.setForeground(RpsClientView.ACCENT_COLOR);
        
        add(waitingLabel);
    }
    
    public void updateWaitingMessage(String message) {
        waitingLabel.setText(message);
    }
    
    // Phương thức để chuyển sang GamePanel (có thể gọi từ controller)
    public void startGame() {
        view.showGameScreen();
    }
}
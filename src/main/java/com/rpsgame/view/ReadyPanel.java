package com.rpsgame.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ReadyPanel extends JPanel {
    private JButton readyButton;
    private JLabel titleLabel;
    
    public ReadyPanel(RpsClientView view) {
        setBackground(RpsClientView.BACKGROUND_COLOR);
        setLayout(new BorderLayout(20, 20));
        
        titleLabel = new JLabel("🎮 Sẵn sàng chơi Kéo Búa Giấy?", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(RpsClientView.ACCENT_COLOR);
        
        readyButton = new JButton("✅ Sẵn sàng!");
        readyButton.setFont(new Font("Arial", Font.BOLD, 16));
        readyButton.setBackground(RpsClientView.BUTTON_COLOR);
        readyButton.setForeground(RpsClientView.TEXT_COLOR);
        readyButton.setPreferredSize(new Dimension(200, 50));
        readyButton.setBorder(BorderFactory.createRaisedBevelBorder());
        readyButton.setFocusPainted(false);
        
        // Hover effect
        readyButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                readyButton.setBackground(RpsClientView.BUTTON_HOVER_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                readyButton.setBackground(RpsClientView.BUTTON_COLOR);
            }
        });
        
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setBackground(RpsClientView.BACKGROUND_COLOR);
        centerPanel.add(readyButton);
        
        add(titleLabel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.SOUTH);
        
        // Thêm action listener mặc định để chuyển sang Waiting
        readyButton.addActionListener(e -> view.showWaitingScreen());
    }
    
    // Có thể thêm listener tùy chỉnh từ controller
    public void addReadyListener(ActionListener listener) {
        readyButton.addActionListener(listener);
    }
}
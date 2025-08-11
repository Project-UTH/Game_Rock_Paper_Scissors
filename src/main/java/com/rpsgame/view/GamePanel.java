package com.rpsgame.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GamePanel extends JPanel {
    private JTextArea textArea;
    private JButton rockButton;
    private JButton paperButton;
    private JButton scissorsButton;
    private JButton exitButton;
    private JLabel titleLabel;
    private JLabel statusLabel;
    
    public GamePanel() {
        setBackground(RpsClientView.BACKGROUND_COLOR);
        setLayout(new BorderLayout(10, 10));
        
        createComponents();
        layoutComponents();
        styleComponents();
    }

    private void createComponents() {
        titleLabel = new JLabel("🎯 KÉO BÚA GIẤY", JLabel.CENTER);
        
        statusLabel = new JLabel("Sẵn sàng để chơi!", JLabel.CENTER);
        
        textArea = new JTextArea(10, 40);
        textArea.setEditable(false);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        textArea.setBackground(RpsClientView.PANEL_COLOR);
        textArea.setForeground(RpsClientView.TEXT_COLOR);
        textArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        
        rockButton = createGameButton("✊", "BÚA", "Búa - Mạnh mẽ và bền vững!");
        paperButton = createGameButton("✋", "GIẤY", "Giấy - Linh hoạt và thông minh!");
        scissorsButton = createGameButton("✂️", "KÉO", "Kéo - Sắc bén và quyết đoán!");
        
        // Nút thoát
        exitButton = new JButton("🚪 Thoát");
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));
        exitButton.setBackground(new Color(200, 50, 50)); // Màu đỏ để nổi bật
        exitButton.setForeground(RpsClientView.TEXT_COLOR);
        exitButton.setPreferredSize(new Dimension(100, 40));
        exitButton.setBorder(BorderFactory.createRaisedBevelBorder());
        exitButton.setFocusPainted(false);
        
        // Hover effect cho nút thoát
        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                exitButton.setBackground(new Color(255, 100, 100));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                exitButton.setBackground(new Color(200, 50, 50));
            }
        });
    }

    private JButton createGameButton(String emoji, String text, String tooltip) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        
        JLabel emojiLabel = new JLabel(emoji, JLabel.CENTER);
        emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30)); // Kích thước emoji
        
        JLabel textLabel = new JLabel(text, JLabel.CENTER);
        textLabel.setFont(new Font("Arial", Font.BOLD, 12)); // Kích thước chữ
        textLabel.setForeground(RpsClientView.TEXT_COLOR);
        
        button.add(emojiLabel, BorderLayout.CENTER);
        button.add(textLabel, BorderLayout.SOUTH);
        
        button.setToolTipText(tooltip);
        button.setPreferredSize(new Dimension(100, 100)); // Chuyển thành hình vuông 100x100
        button.setBackground(RpsClientView.BUTTON_COLOR);
        button.setForeground(RpsClientView.TEXT_COLOR);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(RpsClientView.BUTTON_HOVER_COLOR);
                button.setBorder(BorderFactory.createLineBorder(RpsClientView.ACCENT_COLOR, 2));
                emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 35));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(RpsClientView.BUTTON_COLOR);
                button.setBorder(BorderFactory.createRaisedBevelBorder());
                emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
            }
        });
        
        return button;
    }

    private void layoutComponents() {
        // Panel trên cùng (tiêu đề và nút thoát)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(RpsClientView.BACKGROUND_COLOR);
        topPanel.setBorder(new EmptyBorder(20, 20, 10, 20));
        
        titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        topPanel.add(titleLabel, BorderLayout.CENTER); // Căn giữa tiêu đề
        
        JPanel exitPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        exitPanel.setBackground(RpsClientView.BACKGROUND_COLOR);
        exitPanel.add(exitButton);
        topPanel.add(exitPanel, BorderLayout.EAST);
        
        // Panel giữa (trạng thái và nhật ký)
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(RpsClientView.BACKGROUND_COLOR);
        centerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        statusLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        centerPanel.add(statusLabel, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(RpsClientView.ACCENT_COLOR, 1), 
            "📋 Nhật ký trò chơi", 
            0, 0, 
            new Font("Arial", Font.BOLD, 12), 
            RpsClientView.ACCENT_COLOR
        ));
        scrollPane.setBackground(RpsClientView.PANEL_COLOR);
        scrollPane.getViewport().setBackground(RpsClientView.PANEL_COLOR);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel dưới (các nút lựa chọn)
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 15, 15)); // 1 hàng, 3 cột, khoảng cách 15px
        buttonPanel.setBackground(RpsClientView.BACKGROUND_COLOR);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 20, 0)); // Loại bỏ padding ngang để căn giữa
        
        JLabel choiceLabel = new JLabel("🎲 Chọn nước đi của bạn:", JLabel.CENTER);
        choiceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        choiceLabel.setForeground(RpsClientView.ACCENT_COLOR);
        choiceLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        buttonPanel.add(rockButton);
        buttonPanel.add(paperButton);
        buttonPanel.add(scissorsButton);
        
        // Sử dụng GridBagLayout để căn giữa toàn bộ buttonPanel
        JPanel buttonContainer = new JPanel(new GridBagLayout());
        buttonContainer.setBackground(RpsClientView.BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER; // Căn giữa
        buttonContainer.add(choiceLabel, gbc);
        
        gbc.gridy = 1;
        buttonContainer.add(buttonPanel, gbc); // Thêm buttonPanel vào giữa
        
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonContainer, BorderLayout.SOUTH);
    }

    private void styleComponents() {
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(RpsClientView.ACCENT_COLOR);
        
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        statusLabel.setForeground(RpsClientView.TEXT_COLOR);
    }

    public void addChoiceListener(ActionListener rockListener, ActionListener paperListener, ActionListener scissorsListener) {
        rockButton.addActionListener(rockListener);
        paperButton.addActionListener(paperListener);
        scissorsButton.addActionListener(scissorsListener);
    }

    public void addExitListener(ActionListener exitListener) {
        exitButton.addActionListener(exitListener);
    }

    public void appendMessage(String message) {
        textArea.append("🕐 " + getCurrentTime() + " - " + message + "\n");
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    public void updateStatus(String status) {
        statusLabel.setText(status);
        statusLabel.repaint();
    }
    
    public void setButtonsEnabled(boolean enabled) {
        rockButton.setEnabled(enabled);
        paperButton.setEnabled(enabled);
        scissorsButton.setEnabled(enabled);
        
        if (!enabled) {
            updateStatus("⏳ Đang chờ phản hồi...");
        } else {
            updateStatus("✅ Sẵn sàng chơi tiếp!");
        }
    }
    
    private String getCurrentTime() {
        return java.time.LocalTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")
        );
    }
    
    public void showWelcomeMessage() {
        appendMessage("🎉 Chào mừng bạn đến với game Kéo-Búa-Giấy online!");
        appendMessage("💡 Hướng dẫn: Chọn một trong ba nước đi và chiến thắng đối thủ!");
        appendMessage("🏆 Kéo thắng Giấy, Giấy thắng Búa, Búa thắng Kéo");
    }
}
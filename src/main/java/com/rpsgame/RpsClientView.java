package com.rpsgame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RpsClientView extends JFrame {
    private JTextArea textArea;
    private JButton rockButton;
    private JButton paperButton;
    private JButton scissorsButton;
    private JLabel titleLabel;
    private JLabel statusLabel;
    
    // Colors
    private final Color BACKGROUND_COLOR = new Color(25, 25, 35);
    private final Color PANEL_COLOR = new Color(35, 35, 50);
    private final Color TEXT_COLOR = new Color(255, 255, 255);
    private final Color BUTTON_COLOR = new Color(70, 130, 180);
    private final Color BUTTON_HOVER_COLOR = new Color(100, 149, 237);
    private final Color ACCENT_COLOR = new Color(255, 215, 0);

    public RpsClientView() {
        setupMainWindow();
        createComponents();
        layoutComponents();
        styleComponents();
        setVisible(true);
    }

    private void setupMainWindow() {
        setTitle("🎮 Rock Paper Scissors - Online Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Set dark theme
        getContentPane().setBackground(BACKGROUND_COLOR);
    }

    private void createComponents() {
        // Title
        titleLabel = new JLabel("🎯 ROCK PAPER SCISSORS", JLabel.CENTER);
        
        // Status label
        statusLabel = new JLabel("Sẵn sàng để chơi!", JLabel.CENTER);
        
        // Text area for game messages
        textArea = new JTextArea(10, 40);
        textArea.setEditable(false);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        textArea.setBackground(PANEL_COLOR);
        textArea.setForeground(TEXT_COLOR);
        textArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        
        // Game buttons with emojis
        rockButton = createGameButton("✊", "ROCK", "Đá - Mạnh mẽ và bền vững!");
        paperButton = createGameButton("✋", "PAPER", "Giấy - Linh hoạt và thông minh!");
        scissorsButton = createGameButton("✂️", "SCISSORS", "Kéo - Sắc bén và quyết đoán!");
    }

    private JButton createGameButton(String emoji, String text, String tooltip) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        
        // Emoji label
        JLabel emojiLabel = new JLabel(emoji, JLabel.CENTER);
        emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        
        // Text label
        JLabel textLabel = new JLabel(text, JLabel.CENTER);
        textLabel.setFont(new Font("Arial", Font.BOLD, 14));
        textLabel.setForeground(TEXT_COLOR);
        
        button.add(emojiLabel, BorderLayout.CENTER);
        button.add(textLabel, BorderLayout.SOUTH);
        
        button.setToolTipText(tooltip);
        button.setPreferredSize(new Dimension(120, 100));
        button.setBackground(BUTTON_COLOR);
        button.setForeground(TEXT_COLOR);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Hover effects
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(BUTTON_HOVER_COLOR);
                button.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 2));
                emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 45));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(BUTTON_COLOR);
                button.setBorder(BorderFactory.createRaisedBevelBorder());
                emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
            }
        });
        
        return button;
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Top panel with title and status
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.setBorder(new EmptyBorder(20, 20, 10, 20));
        
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(statusLabel, BorderLayout.SOUTH);
        
        // Center panel with text area
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(BACKGROUND_COLOR);
        centerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR, 1), 
            "📋 Game Log", 
            0, 0, 
            new Font("Arial", Font.BOLD, 12), 
            ACCENT_COLOR
        ));
        scrollPane.setBackground(PANEL_COLOR);
        scrollPane.getViewport().setBackground(PANEL_COLOR);
        
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Bottom panel with buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(new EmptyBorder(10, 20, 20, 20));
        
        // Add title for button section
        JPanel buttonContainer = new JPanel(new BorderLayout());
        buttonContainer.setBackground(BACKGROUND_COLOR);
        
        JLabel choiceLabel = new JLabel("🎲 Chọn nước đi của bạn:", JLabel.CENTER);
        choiceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        choiceLabel.setForeground(ACCENT_COLOR);
        choiceLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        buttonPanel.add(rockButton);
        buttonPanel.add(paperButton);
        buttonPanel.add(scissorsButton);
        
        buttonContainer.add(choiceLabel, BorderLayout.NORTH);
        buttonContainer.add(buttonPanel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonContainer, BorderLayout.SOUTH);
    }

    private void styleComponents() {
        // Title styling
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(ACCENT_COLOR);
        titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        // Status styling
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        statusLabel.setForeground(TEXT_COLOR);
    }

    public void addChoiceListener(ActionListener rockListener, ActionListener paperListener, ActionListener scissorsListener) {
        rockButton.addActionListener(rockListener);
        paperButton.addActionListener(paperListener);
        scissorsButton.addActionListener(scissorsListener);
    }

    public void appendMessage(String message) {
        textArea.append("🕐 " + getCurrentTime() + " - " + message + "\n");
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    public void showError(String error) {
        JOptionPane.showMessageDialog(this, 
            "❌ " + error, 
            "Lỗi", 
            JOptionPane.ERROR_MESSAGE);
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
        appendMessage("🎉 Chào mừng bạn đến với game Kéo-Búa-Bao online!");
        appendMessage("💡 Hướng dẫn: Chọn một trong ba nước đi và chiến thắng đối thủ!");
        appendMessage("🏆 Kéo thắng Giấy, Giấy thắng Đá, Đá thắng Kéo");
    }
}
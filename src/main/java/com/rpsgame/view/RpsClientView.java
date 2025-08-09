package com.rpsgame.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class RpsClientView extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    // Các panel riêng biệt
    private ReadyPanel readyPanel;
    private WaitingPanel waitingPanel;
    private GamePanel gamePanel;

    // Lưu tham chiếu đến gamePanel để gọi các phương thức
    private GamePanel gamePanelRef;

    // Colors (giữ nguyên để dùng chung)
    public static final Color BACKGROUND_COLOR = new Color(25, 25, 35);
    public static final Color PANEL_COLOR = new Color(35, 35, 50);
    public static final Color TEXT_COLOR = new Color(255, 255, 255);
    public static final Color BUTTON_COLOR = new Color(70, 130, 180);
    public static final Color BUTTON_HOVER_COLOR = new Color(100, 149, 237);
    public static final Color ACCENT_COLOR = new Color(255, 215, 0);

    public RpsClientView() {
        setupMainWindow();
        createPanels();
        layoutComponents();
        setVisible(true);
    }

    private void setupMainWindow() {
        setTitle("🎮 Kéo Búa Giấy - Trò chơi trực tuyến");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 550); // Tăng kích thước khung lên 700x550
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(BACKGROUND_COLOR);
    }

    private void createPanels() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        readyPanel = new ReadyPanel(this);
        waitingPanel = new WaitingPanel(this);
        gamePanel = new GamePanel();

        // Lưu tham chiếu đến gamePanel
        gamePanelRef = gamePanel;

        mainPanel.add(readyPanel, "READY");
        mainPanel.add(waitingPanel, "WAITING");
        mainPanel.add(gamePanel, "GAME");
    }

    private void layoutComponents() {
        add(mainPanel, BorderLayout.CENTER);
        showReadyScreen();
    }
    
    // Phương thức chuyển màn hình
    public void showReadyScreen() {
        cardLayout.show(mainPanel, "READY");
    }
    
    public void showWaitingScreen() {
        cardLayout.show(mainPanel, "WAITING");
    }
    
    public void showGameScreen() {
        cardLayout.show(mainPanel, "GAME");
        gamePanelRef.showWelcomeMessage(); // Hiển thị thông điệp chào mừng
    }
    
    // Cập nhật showResultDialog để hỗ trợ trạng thái hòa
    public void showResultDialog(String resultMessage, boolean isWin) {
        String displayMessage;
        Color color;
        
        if (resultMessage.contains("Hòa!")) {
            displayMessage = "🤝 " + resultMessage; // Thêm biểu tượng hòa
            color = new Color(192, 192, 192); // Màu xám cho hòa
        } else {
            displayMessage = isWin ? "🏆 Bạn thắng! " + resultMessage : "😔 Bạn thua! " + resultMessage;
            color = isWin ? new Color(0, 255, 0) : new Color(255, 0, 0); // Xanh cho thắng, đỏ cho thua
        }
        
        JLabel label = new JLabel(displayMessage);
        label.setForeground(color);
        
        JOptionPane pane = new JOptionPane(label, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = pane.createDialog(this, "Kết quả");
        dialog.setBackground(BACKGROUND_COLOR);
        dialog.getContentPane().setBackground(BACKGROUND_COLOR);

        // Tự động đóng dialog sau 3 giây
        javax.swing.Timer timer = new javax.swing.Timer(3000, e -> dialog.dispose());
        timer.setRepeats(false); // Chỉ chạy một lần
        timer.start();
        dialog.setVisible(true);
    }
    
    // Proxy methods để tương tác với GamePanel
    public void addChoiceListener(ActionListener rockListener, ActionListener paperListener, ActionListener scissorsListener) {
        gamePanelRef.addChoiceListener(rockListener, paperListener, scissorsListener);
    }
    
    public void appendMessage(String message) {
        gamePanelRef.appendMessage(message);
    }
    
    public void updateStatus(String status) {
        gamePanelRef.updateStatus(status);
    }
    
    public void setButtonsEnabled(boolean enabled) {
        gamePanelRef.setButtonsEnabled(enabled);
    }
    
    public void addExitListener(ActionListener exitListener) {
        gamePanelRef.addExitListener(exitListener); // Proxy đến GamePanel
    }
    
    // Thêm phương thức showError
    public void showError(String error) {
        JOptionPane.showMessageDialog(this, "❌ " + error, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
    
    // Thêm phương thức để lấy ReadyPanel và WaitingPanel nếu cần
    public ReadyPanel getReadyPanel() {
        return readyPanel;
    }
    
    public WaitingPanel getWaitingPanel() {
        return waitingPanel;
    }
}
package com.rpsgame.game;

public class Game implements Runnable {
    private ClientHandler player1;
    private ClientHandler player2;
    private final Object gameLock = new Object();

    public Game(ClientHandler p1, ClientHandler p2) {
        this.player1 = p1;
        this.player2 = p2;
        p1.setGame(this);
        p2.setGame(this);
    }

    @Override
    public void run() {
        broadcast("Trò chơi bắt đầu! Chọn: BÚA, GIẤY, hoặc KÉO");
    }

    public void checkIfBothChose(ClientHandler sender) {
        synchronized (gameLock) {
            if (player1.getChoice() != null && player2.getChoice() != null) {
                determineWinner();
                player1.resetChoice();
                player2.resetChoice();
                broadcast("Chọn lại cho vòng tiếp theo: BÚA, GIẤY, hoặc KÉO");
            }
        }
    }

    private void determineWinner() {
        String choice1 = player1.getChoice();
        String choice2 = player2.getChoice();

        String result;
        if (choice1.equals(choice2)) {
            result = "Hòa! Cả hai chọn " + choice1;
        } else if (
            (choice1.equals("BÚA") && choice2.equals("KÉO")) ||
            (choice1.equals("KÉO") && choice2.equals("GIẤY")) ||
            (choice1.equals("GIẤY") && choice2.equals("BÚA"))
        ) {
            result = player1.getRole() + " thắng! " + choice1 + " đánh bại " + choice2;
        } else {
            result = player2.getRole() + " thắng! " + choice2 + " đánh bại " + choice1;
        }

        broadcast(result);
    }

    public void broadcast(String message) {
        if (player1 != null) player1.sendMessage(message);
        if (player2 != null) player2.sendMessage(message);
    }

    public void clientDisconnected(ClientHandler disconnected) {
        broadcast("Đối thủ ngắt kết nối. Trò chơi kết thúc.");
        if (disconnected == player1) {
            if (player2 != null) player2.close();
        } else {
            if (player1 != null) player1.close();
        }
    }
}
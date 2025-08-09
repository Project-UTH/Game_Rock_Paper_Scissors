package com.rpsgame;

import com.rpsgame.controller.RpsClientController;
import javax.swing.SwingUtilities;

public class RpsClientRun {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(RpsClientController::new);
    }
}
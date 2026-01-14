package smartfarm;

import smartfarm.controller.MenuController;
import smartfarm.view.MainMenuFrame;

import javax.swing.*;

public class Main {
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Cannot set Look and Feel " + e.getMessage());
        }
        
        SwingUtilities.invokeLater(() -> {
            openMainMenu();
            System.out.println("Smart Farm has started successfully!"); 
        });
    }

    public static void openMainMenu() {
        MenuController controller = new MenuController();
        MainMenuFrame view = new MainMenuFrame(controller); 
        controller.setView(view);
        view.setVisible(true);
    }
}
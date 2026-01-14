// Responsibility: Handle screen transition events (enter game, view tutorial) or exit the application
package smartfarm.controller;

import javax.swing.JOptionPane;

import smartfarm.view.FarmFrame;
import smartfarm.view.HelpFrame;
import smartfarm.view.MainMenuFrame;

public class MenuController {
    
    private MainMenuFrame view; // Reference to the Menu interface

    public void setView(MainMenuFrame view) {
        this.view = view;
    }

    public void startNewGame() {
        FarmFrame farmFrame = new FarmFrame();
        farmFrame.setVisible(true);// Display the game screen
        if (view != null) view.dispose();
    }

    public void openHelp() {
        // Open the Help screen and close the Menu
        HelpFrame helpFrame = new HelpFrame();
        helpFrame.setVisible(true);
        if (view != null) view.dispose();
    }

    public void quitGame() {
        // Handle the application exit event
        int choice = JOptionPane.showConfirmDialog(
            view,
            "Are you sure you want to quit?",
            "Confirm Quit",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
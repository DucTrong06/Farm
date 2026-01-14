// Main responsibility is to handle navigation from the Help screen back to the main Menu
package smartfarm.controller;

import smartfarm.view.HelpFrame;


public class HelpController {
    
    private HelpFrame view; // Reference to the current HelpFrame interface

    public void setView(HelpFrame view) {
        this.view = view;
    }
    // Handle the event when the user clicks the "Back to Menu" button
    public void backToMenu() {
        smartfarm.Main.openMainMenu(); 
        
        if (view != null) view.dispose();
    }
}
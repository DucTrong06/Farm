// Responsibility: Handle user input (View), process business logic, update the Model, and update the UI accordingly.

package smartfarm.controller;

import smartfarm.exception.GameException;
import smartfarm.model.GameState;
import smartfarm.model.plant.*;
import smartfarm.util.Constants;
import smartfarm.view.FarmFrame;

import javax.swing.JOptionPane;

public class FarmController {
    private GameState gameState; // Contains all game data
    private FarmFrame view;// Main interface

    public FarmController(GameState gameState, FarmFrame view) {
        this.gameState = gameState;
        this.view = view;
    }


    public void plantCrop(int row, int col, String cropType) {
        // Check whether the user has selected a cell
        if (row < 0 || col < 0) {
            view.setMessage("Please select a cell first!");
            return;
        }

        Plant plant = null;
        if (cropType.startsWith("Wheat")) plant = new Wheat();
        else if (cropType.startsWith("Corn")) plant = new Corn();
        else if (cropType.startsWith("Tomato")) plant = new Tomato();
        else if (cropType.startsWith("Potato")) plant = new Potato();

        if (plant == null) return;

        try {
            // Execute the seed purchase and plant the crop
            gameState.buySeed(plant);
            gameState.getFarm().plantCrop(row, col, plant);
            updateView("Success!");
        } catch (GameException e) {
            // Display an error if there is insufficient money or the land already has a plant
            view.showError("Error", e.getMessage());
        }
    }

    public void waterCrop(int row, int col) {
    // Handle the action of watering the plant at the specified position
    // Use the helper function performAction to reuse error-handling logic
        performAction(row, col, () -> {
            gameState.useWater(Constants.WATER_AMOUNT_PER_USE);
            gameState.getFarm().getCell(row, col).water(Constants.WATER_AMOUNT_PER_USE);
            return "Success!";
        });
    }

    public void fertilizeCrop(int row, int col) {
        // Action of fertilizing the plant
        performAction(row, col, () -> {
            gameState.useFertilizer(Constants.FERTILIZER_AMOUNT_PER_USE);
            gameState.getFarm().getCell(row, col).fertilize(Constants.FERTILIZER_AMOUNT_PER_USE);
            return "Success!";
        });
    }

    public void treatPest(int row, int col) {
        // Action of spraying pesticide to treat plant diseases
        performAction(row, col, () -> {
            gameState.usePesticide();
            gameState.getFarm().getCell(row, col).treatPest();
            return "Success!";
        });
    }

    public void harvestCrop(int row, int col) {
        // Handle the action of harvesting the crop when it is mature
        performAction(row, col, () -> {
            int value = gameState.getFarm().getCell(row, col).harvest(); // Get the harvest value
            gameState.earnMoney(value); // Add money
            return "Harvested successfully! +$" + value;
        });
    }

    public void buyResource(String type, int quantity) {
        try {
            int cost = 0;
            switch(type) {
                case "WATER": cost = quantity * Constants.WATER_PRICE; break;
                case "FERTILIZER": cost = quantity * Constants.FERTILIZER_PRICE; break;
                case "PESTICIDE": cost = quantity * Constants.PESTICIDE_PRICE; break;
            }
            gameState.buyResource(type, quantity, cost);// Deduct money and add to inventory
            updateView("Success!");
        } catch (GameException e) {
            view.showError("Error", e.getMessage());// Report an error if there is not enough money

        }
    }

    public void advanceToNextDay() {
        gameState.advanceDay();
     
        StringBuilder message = new StringBuilder();
        message.append("Day ").append(gameState.getDay()).append("\n");
        // Handle weather
        if (gameState.getFarm().shouldChangeWeather()) {
            gameState.getFarm().updateWeatherRandomly();
            message.append("Weather changed!");
            message.append(" (").append(gameState.getFarm().getCurrentWeather().getName()).append(")\n");
        } else {
            message.append("Weather: ").append(gameState.getFarm().getCurrentWeather().getName()).append("\n");
        }
        // Handle pests and diseases
        if (gameState.checkPestAttack()) {
            int infected = gameState.executePestAttack();
            if (infected > 0) {
                message.append("\nWARNING: Pest attack! ").append(infected).append(" crops infected.");
            }
        }
        // Check for Game Over
        if (gameState.getMoney() < 5 && gameState.getFarm().getTotalCrops() == 0) {
            message.append("\n\nGAME OVER! No money & no crops left.");
        }
        // Display a notification dialog
        JOptionPane.showMessageDialog(view, message.toString(), "New Day Events", JOptionPane.INFORMATION_MESSAGE);
        updateView(null); // Refresh the interface
    }

    @FunctionalInterface
    interface ActionDelegate {
        String run() throws GameException;
    }

    private void performAction(int row, int col, ActionDelegate action) {
        if (row < 0 || col < 0) {
            view.setMessage("Please select a cell first!");
            return;
        }
        try {
            String result = action.run();
            updateView(result);
        } catch (GameException e) {
            view.showError("Error", e.getMessage());
        }
    }
    // Update the entire user interface
    // Called after the Model data changes
    private void updateView(String message) {
        if (message != null) view.setMessage(message);
        view.refreshUI(); // Redraw the land cells, money, day, etc.
        view.updateCellInfo(); 
    }

    public void backToMenu() {
        int choice = JOptionPane.showConfirmDialog(view, 
            "Return to main menu?\nProgress lost.", 
            "Confirm", 
            JOptionPane.YES_NO_OPTION);
            
        if (choice == JOptionPane.YES_OPTION) {
            view.dispose(); // Close the FarmFrame window
            smartfarm.Main.openMainMenu(); 
        }
    }
}
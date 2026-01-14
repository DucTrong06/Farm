// Class representing a specific land cell on the farm grid
// Each cell manages a plant (if any) and the Soil Quality attribute
// Soil quality directly affects the harvest yield
package smartfarm.model;

import smartfarm.exception.GameException;
import smartfarm.model.plant.Plant;

public class Cell {
    private int row;
    private int col;
    private Plant plant;
    private int soilQuality;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.plant = null;
        this.soilQuality = 80;
    }

    public void plant(Plant newPlant) throws GameException {
        if (!isEmpty()) {
            throw new GameException("Cell already has a crop!");
        }
        if (soilQuality < 20) {
            throw new GameException("Soil quality too low (" + soilQuality + "%). Fertilize to >20% first!");
        }
        
        this.plant = newPlant;
        // Planting a crop immediately reduces soil fertility
        this.soilQuality = Math.max(0, soilQuality - 10);
    }

    public void water(int amount) throws GameException {
        if (isEmpty()) throw new GameException("No plant to water!");
        plant.water(amount);
    }

    public void fertilize(int amount) throws GameException {
        // Fertilize the plant AND improve the soil
        if (isEmpty()) throw new GameException("No plant to fertilize!");
        plant.fertilize(amount);
        this.soilQuality = Math.min(100, soilQuality + 5);
    }

    public int harvest() throws GameException {
        if (isEmpty()) throw new GameException("No crop to harvest!");
        if (plant.getStage() != Plant.Stage.HARVEST) {
            throw new GameException("Crop is not ready to harvest!");
        }
        
        int value = plant.getHarvestValue();
        // Bonus/Penalty mechanism based on soil quality
        if (soilQuality >= 80) {
            value = (int)(value * 1.2); 
        } else if (soilQuality < 40) {
            value = (int)(value * 0.8); 
        }
        
        this.plant = null;
        return value;
    }

    public void treatPest() throws GameException {
        if (isEmpty()) throw new GameException("No plant to treat!");
        if (!plant.isPestInfected()) throw new GameException("Plant is not infected!");
        plant.treatPest();
    }

    public void updateDaily() {
        if (plant != null) {
            boolean alive = plant.updateDaily();
            if (!alive) {
                // Dead plant contaminates the soil
                soilQuality = Math.max(0, soilQuality - 15);
                plant = null;
            }
        } else {
            // Fallow land naturally restores nutrients
            soilQuality = Math.min(100, soilQuality + 2);
        }
    }

    public void infectWithPest() {
        if (plant != null) plant.infectWithPest();
    }

    public boolean isEmpty() {
        return plant == null || plant.getStage() == Plant.Stage.EMPTY;
    }

    public Plant getPlant() { return plant; }
    public Plant getCrop() { return plant; }
    
    public String getDisplayEmoji() {
        if (plant == null || isEmpty()) return "🟫";
        return plant.getDisplayEmoji();
    }

    public String getInfo() {
        String info = String.format("Cell [%d,%d] - Soil: %d%%", row, col, soilQuality);
        if (plant != null && !isEmpty()) {
            info += "\n" + plant.getInfo();
        } else {
            info += "\nEmpty";
        }
        return info;
    }
}
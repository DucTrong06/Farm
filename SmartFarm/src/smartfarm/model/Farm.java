// Manage the grid of Cell objects
// Coordinate the weather system and its effects on crops
package smartfarm.model;

import smartfarm.exception.GameException;
import smartfarm.model.plant.Plant;
import smartfarm.model.weather.*;

import java.util.Random;

public class Farm {
    private Cell[][] grid;
    private int rows, cols;
    private Weather currentWeather;
    private Random random = new Random(); // Random number generator used for weather and pest/disease events


    public Farm(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new Cell[rows][cols];
        this.currentWeather = new Sunny();

        // Initialize each specific land cell in the array
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new Cell(i, j);
            }
        }
    }


    public boolean shouldChangeWeather() {
        return random.nextDouble() < 0.3;
    }

    public void updateWeatherRandomly() {
        String currentName = currentWeather.getName();
        Weather nextWeather;
        // State transition logic:
        if (currentName.equals("Rainy")) nextWeather = random.nextDouble() < 0.5 ? new Cloudy() : new Rainy();
        else if (currentName.equals("Cloudy")) nextWeather = random.nextDouble() < 0.4 ? new Sunny() : new Rainy();
        else if (currentName.equals("Sunny")) nextWeather = random.nextDouble() < 0.1 ? new Drought() : new Cloudy();
        else if (currentName.equals("Drought")) nextWeather = random.nextDouble() < 0.6 ? new Sunny() : new Drought();
        else if (currentName.equals("Storm")) nextWeather = new Rainy(); // Storms usually weaken into normal rain
        else nextWeather = getRandomWeather();
        
        this.currentWeather = nextWeather;
    }

    private Weather getRandomWeather() {
        int choice = random.nextInt(5);
        switch (choice) {
            // Helper function to get a random weather type
            case 0: return new Sunny();
            case 1: return new Rainy();
            case 2: return new Cloudy();
            case 3: return new Drought();
            case 4: return new Storm();
            default: return new Sunny();
        }
    }

    public Cell getCell(int row, int col) throws GameException {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            // Validate coordinates to prevent IndexOutOfBounds errors
            throw new GameException("Invalid position");
        }
        return grid[row][col];
    }

    public void plantCrop(int row, int col, Plant plant) throws GameException {
        // This method delegates the logic handling to the Cell class
        getCell(row, col).plant(plant);
    }

    public void updateDaily() {
        applyWeatherEffects(); // Weather
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j].updateDaily(); // Growth
            }
        }
    }

    private void applyWeatherEffects() {
        // Iterate through each cell; if it contains a plant, apply the effects of the current weather
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Cell cell = grid[i][j];
                if (!cell.isEmpty()) {
                    currentWeather.applyEffect(cell.getPlant());
                }
            }
        }
    }

    public int infectRandomCrops(double probability) {
        int infected = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Infect the plant if the cell has one and the random chance is met
                if (!grid[i][j].isEmpty() && Math.random() < probability) {
                    grid[i][j].infectWithPest();
                    infected++;
                }
            }
        }
        return infected;
    }

    public int getTotalCrops() {
        int count = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!grid[i][j].isEmpty()) count++;
            }
        }
        return count;
    }

    public Weather getCurrentWeather() { return currentWeather; }
    public void setCurrentWeather(Weather weather) { this.currentWeather = weather; }
    public int getRows() { return rows; }
    public int getCols() { return cols; }
}
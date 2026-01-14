// Class GameState: Manages the entire game data state
package smartfarm.model;

import smartfarm.exception.GameException;
import smartfarm.model.plant.Plant;
import smartfarm.util.Constants;

import java.util.Random;

public class GameState {
    private int money;
    private int day;
    private int waterStock;
    private int fertilizerStock;
    private int pesticideStock;
    private Farm farm;
    private Random random = new Random();

    public GameState(int rows, int cols) {
        this.money = 1000;
        this.day = 1;
        this.waterStock = 100;
        this.fertilizerStock = 60;
        this.pesticideStock = 20;
        this.farm = new Farm(rows, cols);
    }

    public boolean checkPestAttack() {
        return random.nextDouble() < Constants.PEST_ATTACK_PROBABILITY;
    }

    public int executePestAttack() {
        double infectionRate = 0.1 + random.nextDouble() * 0.2;
        return farm.infectRandomCrops(infectionRate);
    }

    public void buySeed(Plant plant) throws GameException {
        if (money < plant.getSeedCost()) throw new GameException(Constants.NOT_ENOUGH_MONEY);
        money -= plant.getSeedCost();
    }

    public void buyResource(String type, int amount, int cost) throws GameException {
        if (money < cost) throw new GameException(Constants.NOT_ENOUGH_MONEY);
        money -= cost;
        switch (type) {
            case "WATER": waterStock += amount; break;
            case "FERTILIZER": fertilizerStock += amount; break;
            case "PESTICIDE": pesticideStock += amount; break;
        }
    }

    public void useWater(int amount) throws GameException {
        if (waterStock < amount) throw new GameException("Not enough water!");
        waterStock -= amount;
    }

    public void useFertilizer(int amount) throws GameException {
        if (fertilizerStock < amount) throw new GameException("Not enough fertilizer!");
        fertilizerStock -= amount;
    }

    public void usePesticide() throws GameException {
        if (pesticideStock <= 0) throw new GameException("Not enough pesticide!");
        pesticideStock--;
    }

    public void earnMoney(int amount) {
        money += amount;
    }

    public void advanceDay() {
        day++;
        farm.updateDaily();
    }

    public String getStats() {
        return String.format(
            "Day %d | Money: $%d | Water: %d | Fertilizer: %d | Pesticide: %d | Crops: %d",
            day, money, waterStock, fertilizerStock, pesticideStock, farm.getTotalCrops()
        );
    }

    public Farm getFarm() { return farm; }
    public int getDay() { return day; }
    public int getMoney() { return money; }
    public int getWaterStock() { return waterStock; }
    public int getFertilizerStock() { return fertilizerStock; }
    public int getPesticideStock() { return pesticideStock; }
}
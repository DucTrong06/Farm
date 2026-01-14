package smartfarm.util;


public class Constants {
    public static final int DEFAULT_FARM_ROWS = 5;
    public static final int DEFAULT_FARM_COLS = 5;
    
    public static final int WATER_PRICE = 1;           
    public static final int FERTILIZER_PRICE = 2;     
    public static final int PESTICIDE_PRICE = 8;      
    
    public static final int WATER_AMOUNT_PER_USE = 5;
    public static final int FERTILIZER_AMOUNT_PER_USE = 3;
    
    // Probabilities
    public static final double PEST_ATTACK_PROBABILITY = 0.2; 
    public static final double WEATHER_CHANGE_PROBABILITY = 0.3; 
    
    // UI
    public static final int CELL_SIZE = 80;  
    public static final String WINDOW_TITLE = "Smart Farm Simulator";
    
    public static final String NOT_ENOUGH_MONEY = "Not enough money!";
    public static final String NOT_ENOUGH_RESOURCES = "Not enough resources!";
    public static final String CELL_NOT_EMPTY = "Cell already has a crop!";
    public static final String NO_CROP_TO_HARVEST = "No crop to harvest!";
    public static final String SUCCESS = "Success!";
}
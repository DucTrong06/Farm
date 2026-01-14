// Abstract class representing all types of crops in the game
// This class defines common attributes (price, growth time, water/fertilizer requirements)
// Contains core logic for growth, resource consumption, and health
package smartfarm.model.plant;

public abstract class Plant {
    
    public enum Stage {
        // Enum defining the growth stages (lifecycle) of a plant
        EMPTY("Empty", "⬜"),
        SEED("Seed", "🌰"),
        SEEDLING("Seedling", "🌱"),
        MATURE("Mature", "🌿"),      
        HARVEST("Harvest", "");

        private final String displayName;
        private final String defaultEmoji;

        Stage(String displayName, String defaultEmoji) {
            this.displayName = displayName;
            this.defaultEmoji = defaultEmoji;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getDefaultEmoji() {
            return defaultEmoji;
        }

        public boolean canGrow() {
            return this == SEED || this == SEEDLING || this == MATURE;
        }
    }
    // Fixed attributes (Stats) of each crop type (Wheat, Corn, etc.)
    private String displayName;
    private int seedCost;
    private int harvestValue;
    private int growthTime;
    private int waterNeed;
    private int fertilizerNeed;
    private String harvestEmoji;

    // Attributes that change over time (State)
    private Stage stage; 
    private int daysGrown;
    private int waterLevel;
    private int fertilizerLevel;
    private int health;                   // Hidden from external access
    private boolean isPestInfected;

    public Plant(String displayName, int seedCost, int harvestValue, 
                 int growthTime, int waterNeed, int fertilizerNeed, String harvestEmoji) {
        this.displayName = displayName;
        this.seedCost = seedCost;
        this.harvestValue = harvestValue;
        this.growthTime = growthTime;
        this.waterNeed = waterNeed;
        this.fertilizerNeed = fertilizerNeed;
        this.harvestEmoji = harvestEmoji;
        // Default initial state
        this.stage = Stage.SEED;
        this.daysGrown = 0;
        this.waterLevel = 0;
        this.fertilizerLevel = 0;
        this.health = 100;
        this.isPestInfected = false;
    }

    public void water(int amount) {
        // Maximum storage limit is 10 units of water
        waterLevel = Math.min(waterLevel + amount, 10);
    }

    public void fertilize(int amount) {
        fertilizerLevel = Math.min(fertilizerLevel + amount, 10);
    }

    public void infectWithPest() {
        // Infect the plant (due to a random event from GameState)
        this.isPestInfected = true;
    }

    public void treatPest() {
        // Remove pests and restore 20 health points (up to a maximum of 100)
        this.isPestInfected = false;
        this.health = Math.min(100, health + 20);
    }
    
    public void adjustHealth(int amount) {
        // Ensure health always stays within the range [0, 100]
        this.health = Math.min(100, Math.max(0, this.health + amount));
    }

    public boolean updateDaily() {
        // If already harvested or the land is empty, no calculation is needed
        if (stage == Stage.HARVEST || stage == Stage.EMPTY) {
            return true; 
        }
        beforeDailyUpdate();
        
        waterLevel = Math.max(0, waterLevel - waterNeed);
        fertilizerLevel = Math.max(0, fertilizerLevel - fertilizerNeed);

        if (waterLevel < waterNeed || fertilizerLevel < fertilizerNeed) {           
        	// Health logic: Lack of nutrients -> subtract 20 HP, Sufficient nutrients -> restore 5 HP
            health -= 20;
        } else {
            health = Math.min(100, health + 5);
        }

        if (isPestInfected) {
        	// If diseased -> call pestDamage to subtract health
            health -= pestDamage();
        }
        
        if (stage.canGrow() && waterLevel >= waterNeed && fertilizerLevel >= fertilizerNeed) {
            // Growth logic: Only grow if in the development stage AND nutrients are sufficient
            daysGrown++;

        if (daysGrown >= growthTime && stage == Stage.SEED) stage = Stage.SEEDLING;
        else if (daysGrown >= growthTime * 2 && stage == Stage.SEEDLING) stage = Stage.MATURE;
        else if (daysGrown >= growthTime * 3 && stage == Stage.MATURE) stage = Stage.HARVEST;
    		}
        // if health < 0 =>
        if (health <= 0) {
            stage = stage.EMPTY;
            return false;
            }
        return true;
    }
    
    public void beforeDailyUpdate() {};
    public int pestDamage() {
        return 15;
    }
    
    public String getDisplayName() { return displayName; }
    public int getSeedCost() { return seedCost; }
    public int getHarvestValue() { return harvestValue; }

    public Stage getStage() { return stage; }
    
    public int getHealth() { return health; }
    public boolean isPestInfected() { return isPestInfected; }

    public String getDisplayEmoji() {
        // Get the image to display on the Grid
        if (stage == Stage.HARVEST) return harvestEmoji;
        return stage.getDefaultEmoji();
    }

    public String getInfo() {
        return String.format("%s - %s\nDays: %d | Water: %d | Fertilizer: %d | HP: %d%s",
            displayName, stage.getDisplayName(), daysGrown, waterLevel, fertilizerLevel, health,
            isPestInfected ? " [🐛 PEST]" : "");
    }
}


// Abstract class defining the common structure for weather types in the game
// Each specific weather type (Sunny, Rainy, Storm, etc.) inherits this class and has its own impact parameters
package smartfarm.model.weather;

import smartfarm.model.plant.Plant;

public abstract class Weather {
    private String name;
    private int waterEffect;
    private int fertilizerEffect;
    private int healthEffect;

    public Weather(String name, int waterEffect, int fertilizerEffect, int healthEffect) {
        this.name = name;
        this.waterEffect = waterEffect;
        this.fertilizerEffect = fertilizerEffect;
        this.healthEffect = healthEffect;
    }

    public void applyEffect(Plant plant) {
        // Check the parameters waterEffect, fertilizerEffect, and healthEffect
        // If not zero, call the corresponding Plant class methods to modify the plant's state
        if (waterEffect != 0) plant.water(waterEffect);
        if (fertilizerEffect != 0) plant.fertilize(fertilizerEffect);
        if (healthEffect != 0) plant.adjustHealth(healthEffect);
    }
    // Get the weather name to display on the FarmFrame status bar (UI)
    public String getName() { return name; }
}
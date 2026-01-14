package smartfarm.model.plant;

public class Wheat extends Plant {
	public Wheat() {
		super("Wheat", 5, 50, 1, 2, 2, "🌾");
	}
	@Override
	public int pestDamage() {
	        return 10;
	    }
}

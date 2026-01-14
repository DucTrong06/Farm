package smartfarm.model.plant;

public class Potato extends Plant {
	public Potato() {
		super("Potato", 8, 60, 1, 2, 2, "🥔");
	}
		@Override
	public int pestDamage() {
		return 8;
	}
}

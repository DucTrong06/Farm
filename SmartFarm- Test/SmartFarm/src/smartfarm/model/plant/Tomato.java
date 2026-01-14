package smartfarm.model.plant;

public class Tomato extends Plant {
	public Tomato() {
		super("Tomato", 15, 120, 3, 4, 3, "🍅");
	}
		@Override
		public int pestDamage() {
			return 10;
		}
}

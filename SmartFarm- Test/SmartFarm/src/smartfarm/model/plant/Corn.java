package smartfarm.model.plant;

public class Corn extends Plant {
	public Corn() {
		super("Corn", 10, 80, 2, 3, 3, "🌽");
	}
		@Override
		public int pestDamage() {
			return 20;
		}
}

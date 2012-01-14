package space.game.engine.objects;

public class Scout extends ShipTemplate {

	public Scout(int nx, int ny) {
		super(nx, ny, 50, 80);
		velocity = 0.04f;
		turnRate = 0.03f;
		mass = 2;
		dieOnCollision = true;
	}
}

package space.game.engine.objects;

public class StarBase extends ShipTemplate{

	public StarBase(int nx, int ny) {
		super(nx, ny, 100, 80);
		velocity = 0.007f;
		mass = 3;
		ignoreAngle = true;
		dieOnCollision = true;
	}

}

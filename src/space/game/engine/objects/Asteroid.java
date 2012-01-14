package space.game.engine.objects;

public class Asteroid extends ShipTemplate {
	private boolean wasInside=false;
	
	public Asteroid(int x, int y){
		super(x,y,80,80);
		velocity = 0.01f;
		turnRate = 0.005f;
		mass = 3;
		path.visible = false;
		ignoreAngle = true;
		dieOnEmptyPath = true;
	}
	
	public void update(float delta){
		angle += turnRate*delta;
		if(angle >= 360) angle -= 360;
		
		super.update(delta);
	}
	
	public boolean shouldDie(){
		if (wasInside && isOutside()) {
			return true;
		} else if (!wasInside && !isOutside()) {
			wasInside = true;
		}
		
		return super.shouldDie();
	}
}

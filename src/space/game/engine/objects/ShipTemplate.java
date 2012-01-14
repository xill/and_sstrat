package space.game.engine.objects;

import java.util.ArrayList;

import android.util.Log;

import samik.util.math.Vec2f;
import samik.util.math.VectorFactory;
import space.game.engine.World;
import space.game.engine.pathing.PathingHandler;
import space.game.graphics.GameRenderer;

public class ShipTemplate {
	public static final int COLLISION_DURATION = 1000;
	
	public float x,y;
	public int width,height;
	
	/**
	 * Yes, even space things have mass.
	 */
	public float mass;
	
	/**
	 * current ship angle in degrees.
	 */
	public float angle = 0;
	
	/**
	 * time left until collision effect wears off.
	 */
	public long collision_left = 0;
	
	/**
	 * if ship ignores angle.
	 */
	public boolean ignoreAngle = false;
	public boolean active = false;
	
	/**
	 * if this object is set to die upon reaching the end of the waypoint path.
	 */
	public boolean dieOnEmptyPath = false;
	
	/**
	 * if this object is set to die after collision effect ends.
	 */
	public boolean dieOnCollision = false;
	
	/**
	 * is this object dead?
	 */
	public boolean dead = false;
	
	/**
	 * when close enough to the waypoint.
	 */
	public float deadZone;
	
	/**
	 * velocity of this ship object.
	 */
	protected float velocity=0;
	
	/**
	 * Basicly the collision range.
	 */
	public float shieldRadius=0;
	
	/**
	 * current turn rate of this object.
	 * 
	 * currently unused.
	 */
	protected float turnRate=0;
	public boolean hasStopped = true;
	private Vec2f pathingAssist;
	public Vec2f normal;
	private static Vec2f zeroAngle;
	public float movX = 0,movY = 0;
	private long lastCall;
	private boolean collisionEnded = false; 
	public PathingHandler path;
	
	protected ShipTemplate(int nx, int ny, int w, int h){
		x = nx;
		y = ny;
		width = w;
		height = h;
		deadZone = h/4;
		
		pathingAssist = VectorFactory.instance().assign();
		normal = VectorFactory.instance().assign();
		if(zeroAngle == null){
			zeroAngle = new Vec2f(0,1);
		}
		path = new PathingHandler(this);
		shieldRadius = ((width>height)?width:height)>>2;
	}
	
	protected void finalize(){
		VectorFactory.instance().free(pathingAssist);
		VectorFactory.instance().free(normal);
		try {
			super.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public boolean within(float nx, float ny){
		int w = width/2;
		int h = height/2;
		if(nx <= x+w && nx >= x-w 
			&& ny <= y+h && ny >= y-h)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * updates this shiptemplate object.
	 * 
	 * @param delta
	 */
	public void update(float delta){
		if(collision_left > 0){
			long duration = World.time - lastCall;
			if(duration > 100) duration = 100;
			lastCall = World.time;
			
			collision_left -= duration;
			x += movX*delta;
			y += movY*delta;
			
			if(collision_left <= 0) collisionEnded = true;
		}
		else if(!path.getPath().isEmpty()){
			lastCall = World.time;
			
			hasStopped = false;
			pathingAssist.x = path.getPath().get(0).x-x;
			pathingAssist.y = path.getPath().get(0).y-y;
			calculateAngle();
			float n = 1/pathingAssist.length();
			normal.x = pathingAssist.x*n;
			normal.y = pathingAssist.y*n;
			
			x += (movX = velocity*normal.x)*delta;
			y += (movY = velocity*normal.y)*delta;
			
			if(pathingAssist.length() < deadZone){
				path.getPath().remove(0);
			}
		} else {
			hasStopped = true;
			movX = 0;
			movY = 0;
		}
		
		if(shouldDie()) dead = true;
	}
	
	/**
	 * calculates the collision between this and objectB.
	 * 
	 * @param objectB - other colliding ShipTemplate object.
	 * @param hitnormal - normal vector for the hit.
	 */
	public boolean collision(ShipTemplate objectB, Vec2f hitnormal){
		boolean status = false;
		if(collision_left <= 0){
			normal.x = hitnormal.x;
			normal.y = hitnormal.y;
			
			float impulse = -(2*((movX-objectB.movX)*normal.x + (movY-objectB.movY)*normal.y)/((1/this.mass)+(1/objectB.mass)));
			movX += (impulse/this.mass)*normal.x;
			movY += (impulse/this.mass)*normal.y;
	
			objectB.movX -= (impulse/objectB.mass)*hitnormal.x;
			objectB.movY -= (impulse/objectB.mass)*hitnormal.y;
			
			objectB.normal.x = objectB.movX;
			objectB.normal.y = objectB.movY;
			objectB.normal.normalize();
			
			collision_left = COLLISION_DURATION;
			objectB.collision_left = COLLISION_DURATION;
			status = true;
		}
		return status;
	}
	
	/**
	 * Redirects this object to move towards its current direction by given distance.
	 * 
	 * @param distance
	 */
	public final void redirect(float distance){
		ArrayList<Vec2f> l = path.getPath();
		VectorFactory f = VectorFactory.instance();
		/* clear the old waypoints. */
		for(Vec2f v : l){
			f.free(v);
		}
		l.clear();

		Vec2f newPoint = f.assign(x + normal.x * distance , y + normal.y * distance);
		
		l.add(newPoint);
	}
	
	/**
	 * Calculates the current angle for the set pathingAssist
	 * 
	 * @param p
	 */
	private void calculateAngle(){
		if(ignoreAngle) return;
		angle = zeroAngle.angle(pathingAssist);
		if(pathingAssist.x >= 0) angle = (float) (Math.PI*2-angle);
		angle *= 180/Math.PI;
	}
	
	/**
	 * @return True if this shiptemplate is outside the current view.
	 */
	public final boolean isOutside() {
		if(x+(width/2) < 0 || x-(width/2) > GameRenderer.scrWidth
				|| y+(height/2) < 0 || y-(height/2) > GameRenderer.scrHeight)
		{
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Overridable function which by default always returns false
	 * preset booleans dieOnEmptyPath and dieOnCollision can make this function return true.
	 * 
	 * This function is called after every update.
	 * This shiptemplate object will die if this function should ever return true.
	 * 
	 * @return should this object die?
	 */
	public boolean shouldDie(){
		if(( dieOnEmptyPath && path.getPath().isEmpty() )
			|| ( dieOnCollision && collisionEnded )
		){
			collisionEnded = false;
			return true;
		} else {
			collisionEnded = false;
			return false;
		}
	}
}

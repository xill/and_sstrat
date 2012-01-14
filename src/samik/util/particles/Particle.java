package samik.util.particles;

/**
 * Object that represents a single particle.
 * 
 * @author Sami
 *
 */
public class Particle {
	
	/**
	 * time in milliseconds this object is alive.
	 */
	protected int alive = 1;
	protected long last = 0;
	protected float x;
	protected float y;
	protected float mov_x;
	protected float mov_y;
	
	public float R=1,G=1,B=1,A=1;
	
	public Particle(int nx, int ny, float r, float g, float b, float a, int alive){
		this.x = nx;
		this.y = ny;
		this.R = r;
		this.G = g;
		this.B = b;
		this.A = a;
		this.mov_x = 0;
		this.mov_y = 0;
		this.alive = alive;
	}
	
	public Particle(int nx, int ny,float movx, float movy, float r, float g, float b, float a, int alive){
		this.x = nx;
		this.y = ny;
		this.R = r;
		this.G = g;
		this.B = b;
		this.A = a;
		this.mov_x = movx;
		this.mov_y = movy;
		this.alive = alive;
	}
	
	protected long birthTime = 0;
	
	/**
	 * true if the particle is still alive.
	 * 
	 * @return
	 */
	public boolean update(float delta){
		long time = System.currentTimeMillis();
		if (birthTime==0) birthTime = time; 
		long duration = (time - last);
		if(duration > 100) {
			duration = 100;
		}
		last = time;
		alive -= duration;
		if(alive <= 0) {
			return false;
		}
		else {
			x += (mov_x*delta);
			y += (mov_y*delta);
			A = 1 - ((time-birthTime) / ((float)alive));
			return true;
		}
	}
	
	public void setX(int nx){
		this.x = nx;
	}
	
	public float getX(){
		return x;
	}
	
	public void setY(int ny){
		this.y = ny;
	}
	
	public float getY(){
		return y;
	}
	
	public void setMovX(float mx){
		this.mov_x = mx;
	}
	
	public void setMovY(float my){
		this.mov_y = my;
	}
	
	public void setAlive(int a){
		this.alive = a;
		birthTime = 0;
	}
	
	public boolean isAlive(){
		return (alive > 0);
	}
}

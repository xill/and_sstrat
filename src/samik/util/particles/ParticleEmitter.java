package samik.util.particles;

/**
 * Object that emits particles.
 * 
 * use through particlefactory class.
 * 
 * @author Sami
 *
 */
public class ParticleEmitter {
	
	protected int x,y,width,height,lifespan, alive;
	protected float dirx,diry,minspeed,maxspeed,rate;
	protected float R=0,G=0,B=0,A=0;
	protected long last = 0;
	
	public ParticleEmitter(int nx, int ny, int w, int h, int alive){
		this.x = nx;
		this.y = ny;
		this.width = w;
		this.height = h;
		this.lifespan = alive;
	}
	
	public void initParticles(float dirx, float diry,float minSpeed,float maxSpeed,float rate, int alive){
		this.dirx = dirx;
		this.diry = diry;
		this.minspeed = minSpeed;
		this.maxspeed = maxSpeed;
		this.rate = rate;
		this.alive = alive;
	}
	
	public void setColor(float r, float g, float b, float a){
		A = a;
		B = b;
		R = r;
		G = g;
	}
	
	public boolean update(){
		long duration = System.currentTimeMillis() - last;
		if(duration > 100) duration = 100;
		lifespan -= duration;
		if(lifespan <= 0){
			return false;
		}
		else{
			int amount = (int) ((duration/1000.0f)*rate);
			int nx,ny;
			float dx,dy,speed;
			ParticleFactory f = ParticleFactory.instance();
			for(int i = 0; i < amount ; i++){
				nx = (int) (x + (Math.random()*width));
				ny = (int) (y + (Math.random()*height));
				speed = (float) (minspeed + (Math.random()*(maxspeed-minspeed)));
				dx = dirx * speed;
				dy = diry * speed;
				f.newParticle(nx, ny, dx, dy, R, G, B, A, (int)(alive*(Math.random()+0.5)));
			}
		}
		last = System.currentTimeMillis();
		return true;
	}
	
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getLifespan() {
		return lifespan;
	}

	public void setLifespan(int lifespan) {
		this.lifespan = lifespan;
	}
}

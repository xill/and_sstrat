package space.util.effects;

import samik.util.particles.Particle;

/**
 * Effect class derived from samik.util.particles.Particle with few added variables.
 * 
 * @author Sami
 *
 */
public class Effect extends Particle {
	
	public static final String KEY_GLOW = "glow";
	
	public float scalex = 1, scaley = 1;
	public float angle = 0;
	public String KEY = "";
	
	private boolean scaling = false;
	private float scale_from, scale_to;

	public Effect(
			String key
			, int nx
			, int ny
			, float movx
			, float movy
			, float r
			, float g
			, float b
			, float a
			, int alive
	) {
		super(nx, ny, movx, movy, r, g, b, a, alive);
		KEY = key;
	}
	
	public Effect addLinearScaling(float from, float to){
		this.scaling = true;
		this.scale_from = from;
		this.scale_to = to;
		return this;
	}
	
	public boolean update(float delta){
		long time = System.currentTimeMillis();
		if (birthTime==0) birthTime = time; 
		long duration = (time - last);
		if(duration > 100) {
			duration = 100;
		}
		last = time;
		
		x += (mov_x*delta);
		y += (mov_y*delta);
		float s = ((time-birthTime) / ((float)alive));
		A = 1 - s;
		
		if(scaling){
			scalex = scale_from - (scale_from - scale_to)*s;
			scaley = scale_from - (scale_from - scale_to)*s;
		}
		
		alive -= duration;
		if(alive <= 0) {
			return false;
		}
		else {
			return true;
		}
	}

}

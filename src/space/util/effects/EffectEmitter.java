package space.util.effects;

import samik.util.particles.ParticleEmitter;

/**
 * EffectEmitter class derived from samik.util.particles.ParticleEmitter with few added variables.
 * 
 * @author Sami
 *
 */
public class EffectEmitter extends ParticleEmitter {

	public String KEY = "";
	
	public EffectEmitter(String key,int nx, int ny, int w, int h, int alive) {
		super(nx, ny, w, h, alive);
		KEY = key;
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
			EffectFactory f = EffectFactory.instance();
			for(int i = 0; i < amount ; i++){
				nx = (int) (x + (Math.random()*width));
				ny = (int) (y + (Math.random()*height));
				speed = (float) (minspeed + (Math.random()*(maxspeed-minspeed)));
				dx = dirx * speed;
				dy = diry * speed;
				f.newEffect(KEY,nx, ny, dx, dy, R, G, B, A, (int)(alive*(Math.random()+0.5)));
			}
		}
		last = System.currentTimeMillis();
		return true;
	}

}

package samik.util.particles;

import java.util.ArrayList;
import java.util.List;

import samik.util.BaseFactory;

/**
 * Particle Factory singleton.
 * 
 * updates all particles and particle emitters.
 * On Android samik.android.util.opengl.ParticleRenderer
 * can be used to draw the created particles.
 *  
 * other implementations require a distinct renderer.
 * 
 * @author Sami
 *
 */
public class ParticleFactory {
	private static ParticleFactory instance;
	
	private BaseFactory<Particle> free_particles;
	private BaseFactory<ParticleEmitter> free_emitters;
	private List<Particle> in_use_particles;
	private List<ParticleEmitter> in_use_emitters;
	
	private ParticleFactory(){
		free_particles = new BaseFactory<Particle>();
		free_emitters = new BaseFactory<ParticleEmitter>();
		in_use_particles = new ArrayList<Particle>();
		in_use_emitters = new ArrayList<ParticleEmitter>();
	}
	
	public static ParticleFactory instance(){
		if(instance == null){
			instance = new ParticleFactory();
		}
		return instance;
	}
	
	/**
	 * Creates a single new particle.
	 * Particle is automatically added to factory's particle list.
	 * 
	 * same particle is also returned.
	 * 
	 * @param nx start point of this particle in x axis.
	 * @param ny start point of this particle in y axis.
	 * @param movx movement speed of this particle in x axis.
	 * @param movy movement speed of this particle in y axis.
	 * @param r	red color value. between 0.0-1.0.
	 * @param g green color value. between 0.0-1.0.
	 * @param b blue color value. between 0.0-1.0.
	 * @param a alpha value. between 0.0-1.0.
	 * @param alive duration of this particle.
	 * @return created particle.
	 */
	public Particle newParticle(
			int nx
			, int ny
			,float movx
			, float movy
			, float r
			, float g
			, float b
			, float a
			, int alive
	){
		Particle m = free_particles.assign();
		if(m == null){
			m = new Particle(nx, ny,movx, movy, r, g, b, a,alive);
		} else {
			m.setX(nx);
			m.setY(ny);
			m.setMovX(movx);
			m.setMovY(movy);
			m.setAlive(alive);
			m.A = a;
			m.B = b;
			m.G = g;
			m.R = r;
		}
		in_use_particles.add(m);
		return m;
	}
	
	/**
	 * Point in space which auto generates particles.
	 * 
	 * @param nx start point of this emitter in x axis.
	 * @param ny start point of this emitter in y axis.
	 * @param w width of this emitter. dimensions of this emitter is x to (x+width).
	 * @param h height of this emitter. dimensions of this emitter is y to (x+height).
	 * @param alive duration of this emitter.
	 * @return
	 */
	public ParticleEmitter newParticleEmitter(
			int nx
			, int ny
			, int w
			, int h
			, int alive
	){
		ParticleEmitter m = free_emitters.assign();
		if(m == null){
			m = new ParticleEmitter(nx, ny, w, h, alive);
		} else {
			m.setX(nx);
			m.setY(ny);
			m.setWidth(w);
			m.setHeight(h);
			m.setLifespan(alive);
		}
		in_use_emitters.add(m);
		
		return m;
	}
	
	/**
	 * Updates the status of all particle and effect points and emitters.
	 * 
	 * @param delta
	 */
	public void update(float delta){
		if(!in_use_emitters.isEmpty()){
			for(int i = in_use_emitters.size()-1; i >= 0 ; i--){
				if(!in_use_emitters.get(i).update()){
					free_emitters.free(in_use_emitters.get(i));
					in_use_emitters.remove(i);
				}
			}
		}
		
		if(!in_use_particles.isEmpty()){
			for(int i = in_use_particles.size()-1; i >= 0 ; i--){
				if(!in_use_particles.get(i).update(delta)){
					free_particles.free(in_use_particles.get(i));
					in_use_particles.remove(i);
				}
			}
		}
	}
	
	/**
	 * Wipes this factory clean.
	 */
	public void wipe(){
		in_use_particles = null;
		free_particles = null;
		instance = null;
	}
	
	public List<Particle> particles(){
		return in_use_particles;
	}
}

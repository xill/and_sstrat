package space.util.effects;

import java.util.ArrayList;
import java.util.List;

import samik.util.BaseFactory;

public class EffectFactory {
	private static EffectFactory instance;
	
	private BaseFactory<Effect> free_effects;
	private BaseFactory<EffectEmitter> free_emitters;
	private List<Effect> in_use_effects;
	private List<EffectEmitter> in_use_emitters;
	
	private EffectFactory(){
		free_effects = new BaseFactory<Effect>();
		free_emitters = new BaseFactory<EffectEmitter>();
		in_use_effects = new ArrayList<Effect>();
		in_use_emitters = new ArrayList<EffectEmitter>();
	}
	
	public static EffectFactory instance(){
		if(instance == null){
			instance = new EffectFactory();
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
	public Effect newEffect(
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
	){
		Effect m = free_effects.assign();
		if(m == null){
			m = new Effect(key,nx, ny,movx, movy, r, g, b, a,alive);
		} else {
			m.KEY = key;
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
		in_use_effects.add(m);
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
	public EffectEmitter newEffectEmitter(
			String key
			, int nx
			, int ny
			, int w
			, int h
			, int alive
	){
		EffectEmitter m = free_emitters.assign();
		if(m == null){
			m = new EffectEmitter(key,nx, ny, w, h, alive);
		} else {
			m.KEY = key;
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
		
		if(!in_use_effects.isEmpty()){
			for(int i = in_use_effects.size()-1; i >= 0 ; i--){
				if(!in_use_effects.get(i).update(delta)){
					free_effects.free(in_use_effects.get(i));
					in_use_effects.remove(i);
				}
			}
		}
	}
	
	/**
	 * Wipes this factory clean.
	 */
	public void wipe(){
		in_use_effects = null;
		free_effects = null;
		instance = null;
	}
	
	public List<Effect> effects(){
		return in_use_effects;
	}
}

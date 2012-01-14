package space.game.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.opengl.GLSurfaceView;
import samik.android.util.audio.AudioManager;
import samik.util.TextWrapper;
import samik.util.math.Conversions;
import samik.util.math.Vec2f;
import samik.util.math.VectorFactory;
import samik.util.particles.ParticleEmitter;
import samik.util.particles.ParticleFactory;
import space.game.engine.objects.Scout;
import space.game.engine.objects.ShipTemplate;
import space.game.engine.objects.StarBase;
import space.game.engine.pathing.PathMachine;
import space.game.graphics.GameRenderer;
import space.game.graphics.ScoutRenderer;
import space.game.graphics.StarBaseRenderer;
import space.util.Level;
import space.util.effects.Effect;
import space.util.effects.EffectFactory;

public class World implements Runnable{

	public enum GAME_STATE {
		GAME,
		PAUSED,
		INIT
	};
	
	private GAME_STATE state;
	public boolean RUNNING = true;
	
	private GameRenderer m_renderer;
	private GLSurfaceView m_canvas;
	private LevelQueue levelqueue;
	private Context m_context;
	private ParticleFactory m_particles;
	private EffectFactory m_effects;
	private VectorFactory m_vectors;
	
	public AudioManager audio;
	
	/**
	 * Current selected ship object.
	 */
	private ShipTemplate active = null;
	private List<ShipTemplate> ships;
	public List<ShipTemplate> enemies;
	
	private boolean pathfinished = false;
	private float delta = 0;
	private long laststep = 0;
	public static long time = 0;
	
	public World(Context t, GameRenderer r, GLSurfaceView view, Level l){
		m_context = t;
		m_renderer = r;
		m_canvas = view;
		state = GAME_STATE.INIT;
		m_particles = ParticleFactory.instance();
		m_effects = EffectFactory.instance();
		m_vectors = VectorFactory.instance();
		ships = new ArrayList<ShipTemplate>();
		enemies = new ArrayList<ShipTemplate>();
		levelqueue = new LevelQueue(l.queueEvents,this,r); 

		for(ShipTemplate s : l.friendly){
			ships.add(s);
		}
		
		
	}
	
	@Override
	public void run() {
		while(RUNNING){
			synchronized(m_renderer){
				step();
			}
			m_canvas.requestRender();
		}
		
		m_particles.wipe();
		PathMachine.instance().wipe();
		TextWrapper.instance("game").wipe();
	}

	private void step(){
		
		/* time difference is calculated in nanoseconds to make it more accurate. */
		long ntime = 0;
		long duration = (ntime = System.nanoTime()) - laststep;
		laststep = ntime;
		time = (long) (ntime*Conversions.NsToMs);
		duration *= Conversions.NsToMs;
		if(duration > 100 || duration <= 0){
			duration = 100;
		}
		delta = duration/32.0f;
		
		switch(state){
		case GAME:
			game_step();
			break;
		case PAUSED:
			pause_step();
			break;
		case INIT:
			if(m_renderer.isInitialized())init_step();
			break;
		}
		
	}
	
	private void game_step(){
		
		float realx = (InputHandler.instance().x/(float)GameRenderer.rawWidth)*GameRenderer.scrWidth
				,realy = (InputHandler.instance().y/(float)GameRenderer.rawHeight)*GameRenderer.scrHeight;
		
		if(InputHandler.instance().isReleased()){
			pathfinished = true;
			for(ShipTemplate var : ships){
				if(var.within(realx,realy)){
					if(var.active){
						if(active != null) active.active = false;
						active = null;
						var.active = false;
						m_renderer.selector.setActive(false);
					} else {
						if(active != null) active.active = false;
						active = var;
						var.active = true;
						m_renderer.selector.select_scale = (var.height+var.width)/80;
						m_renderer.selector.setActive(true);
					}
					break;
				}
			}
		} else if (InputHandler.instance().isPressed()) {
			boolean wasWithin = false;
			
			for(ShipTemplate var : ships){
				if(var.within(realx,realy)){
					wasWithin = true;
					break;
				}
			}
			if(active != null && !wasWithin){
				if(pathfinished){
					active.path.finished();
					pathfinished = false;
				}
				
				active.path.add((int)realx,(int)realy);
			}
		}
		
		/* update friendly ships. */
		ShipTemplate tmp;
		if(!ships.isEmpty()){
			for(int i = ships.size()-1; i >= 0; i-- ){
				(tmp = ships.get(i)).update(delta);
				if(tmp.dead){
					m_renderer.removeDrawable(tmp);
					ships.remove(tmp);
					
					float minSpeed = 0.01f ,maxSpeed = 0.05f ,rate = 20;
					int alive = 3000;
					
					Effect e = EffectFactory.instance()
						.newEffect(
								Effect.KEY_GLOW
								, (int)tmp.x
								, (int)tmp.y
								, 0
								, 0
								, 1
								, 1
								, 1
								, 1
								, alive
						);
					e.scalex = (tmp.height+tmp.width)/80;
					e.scaley = (tmp.height+tmp.width)/80;
					
					ParticleEmitter emitter = ParticleFactory.instance().newParticleEmitter((int)tmp.x-tmp.width/2,(int) tmp.y-tmp.height/2, tmp.width, tmp.height, 1000);
					emitter.initParticles(1, 0, minSpeed, maxSpeed, rate, alive);
					emitter.setColor(1, 1, 1, 1);
					
					emitter = ParticleFactory.instance().newParticleEmitter((int)tmp.x-tmp.width/2,(int) tmp.y-tmp.height/2, tmp.width, tmp.height, 1000);
					emitter.initParticles(-1, 0, minSpeed, maxSpeed, rate, alive);
					emitter.setColor(1, 1, 1, 1);
					
					emitter = ParticleFactory.instance().newParticleEmitter((int)tmp.x-tmp.width/2,(int) tmp.y-tmp.height/2, tmp.width, tmp.height, 1000);
					emitter.initParticles(0, 1, minSpeed, maxSpeed, rate, alive);
					emitter.setColor(1, 1, 1, 1);
					
					emitter = ParticleFactory.instance().newParticleEmitter((int)tmp.x-tmp.width/2,(int) tmp.y-tmp.height/2, tmp.width, tmp.height, 1000);
					emitter.initParticles(0, -1, minSpeed, maxSpeed, rate, alive);
					emitter.setColor(1, 1, 1, 1);
					
					if(ships.isEmpty()) levelqueue.startEndCountdown();
				}
			}
		}
		
		/* update hostile ships & things. */
		if(!enemies.isEmpty()){
			for(int i = enemies.size()-1; i>=0 ; i--){
				(tmp = enemies.get(i)).update(delta);
				if(tmp.dead){
					m_renderer.removeDrawable(tmp);
					enemies.remove(tmp);
				}
			}
		}
		
		Vec2f first = m_vectors.assign();
		Vec2f second = m_vectors.assign();
		for(ShipTemplate friendlies : ships){
			for(ShipTemplate baddies : enemies){
				/*
				 * Simple bounding box check. 
				 * faster then the conclusive check performed afterwards 
				 * if this test is passed.
				 */
				if(friendlies.x + friendlies.shieldRadius < baddies.x - baddies.shieldRadius
						|| friendlies.x - friendlies.shieldRadius > baddies.x + baddies.shieldRadius
						|| friendlies.y + friendlies.shieldRadius < baddies.y - baddies.shieldRadius
						|| friendlies.y - friendlies.shieldRadius > baddies.y + baddies.shieldRadius
				) continue;
				
				first.x = friendlies.x;
				first.y = friendlies.y;
				second.x = baddies.x;
				second.y = baddies.y;
				first.subs(second);
				float dist = first.length();
				if(dist < friendlies.shieldRadius+baddies.shieldRadius){
					first.normalize();
					if(friendlies.collision(baddies,first)){
						baddies.redirect(GameRenderer.scrWidth);
						
						if(friendlies.dieOnCollision){
							friendlies.path.finished();
							if(active != null && active.equals(friendlies)){
								active = null;
								m_renderer.selector.setActive(false);
							}
						}
					}
				}
			}
		}
		VectorFactory.instance().free(first);
		VectorFactory.instance().free(second);
		
		if(active != null){
			m_renderer.selector.x = active.x;
			m_renderer.selector.y = active.y;
		}
		
		levelqueue.update();
		m_particles.update(delta);
		m_effects.update(delta);
		
		if(levelqueue.aboutToEnd()){
			 if(enemies.isEmpty()) levelqueue.showLevelClear();
			 else if(ships.isEmpty()) levelqueue.showGameOver();
		}
		
		if(levelqueue.ended()){
			RUNNING = false;
			((Launcher)m_context).finish();
		}
	}
	
	private void pause_step(){
		
	}
	
	private void init_step(){
		for(ShipTemplate var : ships){
			if(var instanceof Scout){
				m_renderer.addToQueue(new ScoutRenderer((Scout)var));
			} else if (var instanceof StarBase){
				m_renderer.addToQueue(new StarBaseRenderer((StarBase)var));
			}
		}
		m_renderer.resolveQueue();
		
		audio = new AudioManager(m_context,R.raw.aatw);
		audio.setLooping(true);
		audio.start();
		
		state = GAME_STATE.GAME;
	}
}

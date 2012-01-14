package space.game.graphics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import samik.android.util.opengl.Drawable;
import samik.android.util.opengl.GPUMemoryManager;
import samik.android.util.opengl.ParticleRenderer;
import samik.android.util.opengl.QuadRenderer;
import samik.android.util.opengl.TextWrapperRenderer;
import samik.android.util.opengl.TextureManager;
import samik.util.math.Vec2f;
import space.game.engine.objects.ShipTemplate;
import space.util.graphics.EffectRenderer;

import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

public class GameRenderer implements Renderer {
	/**
	 * Set screen pixel size.
	 */
	public static int scrWidth,scrHeight;
	
	/**
	 * Raw screen size.
	 */
	public static int rawWidth,rawHeight;
	
	private boolean initialized = false;
	private List<Drawable> drawables;
	private List<Drawable> queue;
	private PathRenderer d;
	private TextWrapperRenderer text;
	private ParticleRenderer particles;
	private EffectRenderer effects;
	private QuadRenderer background;
	public SelectRenderer selector;

	public GameRenderer(){
		drawables = new ArrayList<Drawable>();
		queue = new ArrayList<Drawable>();
		
		/* actual pixel size of the screen used. 
		 * this is hard coded mainly to keep 
		 * the experience consistent over different sized phones.
		 */
		scrWidth = 700;
		scrHeight = 500;
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glLoadIdentity();
		
		synchronized(this){
			for(Drawable var : drawables){
				var.draw(gl);
			}
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		rawWidth = width;
		rawHeight = height;
		
		gl.glViewport(0, 0, rawWidth, rawHeight);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		
		GLU.gluOrtho2D(gl, 0, scrWidth, scrHeight, 0);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		TextureManager.destroyTextures(gl);
		GPUMemoryManager.instance().deletePointers(gl);
		
		/*
		 * all these renderer object have a null check for one reason.
		 * function onSurfaceChanged is called each time screen is recreated. 
		 */
		
		if(d == null){
			d = new PathRenderer();
			drawables.add(d);
		}
		
		if(selector == null){
			selector = new SelectRenderer();
			drawables.add(selector);
		}
		
		if(text == null){
			text = new TextWrapperRenderer("game");
			drawables.add(text);
		}
		
		if(particles == null){
			particles = new ParticleRenderer();
			drawables.add(particles);
		}
		
		if(effects == null){
			effects = new EffectRenderer();
			drawables.add(effects);
		}
		
		if(background == null){
			background = new QuadRenderer(new Vec2f(0,0), new Vec2f(scrWidth,scrHeight), "bg", 0);
			drawables.add(background);
		}
		
		Collections.sort(drawables);
		reloadTextures(gl);
		
		initialized = true;
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glClearColor(0, 0, 0, 1);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}
	
	public void addToQueue(Drawable d){
		queue.add(d);
	}
	
	public void resolveQueue(){
		if(!queue.isEmpty()){
			synchronized(this){
				for(Drawable d : queue){
					drawables.add(d);
				}
				Collections.sort(drawables);
			}
			queue.clear();
		}
	}
	
	public void removeDrawable(Object o){
		int size = drawables.size();
		Drawable tmp;
		
		if(o instanceof ShipTemplate){
			for(int i = 0; i < size ; i++){
				if((tmp = drawables.get(i)) instanceof ShipRenderer){
					if(((ShipRenderer)tmp).host == o){
						drawables.remove(i);
						return;
					}
				}
			}
		}
	}

	public boolean isInitialized() {
		return initialized;
	}
	
	private void reloadTextures(GL10 gl){
		ScoutRenderer.loadTextures(gl);
		StarBaseRenderer.loadTextures(gl);
		AsteroidRenderer.loadTextures(gl);
		SelectRenderer.loadTextures(gl);
		TextWrapperRenderer.loadTextures(gl);
		ShipRenderer.loadTextures(gl);
		
		try {
			TextureManager.getTextureId(gl,"bg", GL10.GL_CLAMP_TO_EDGE, GL10.GL_CLAMP_TO_EDGE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

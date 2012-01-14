package space.game.graphics;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import samik.android.util.opengl.Drawable;
import samik.android.util.opengl.GPUMemoryManager;
import samik.android.util.opengl.TextureManager;
import space.game.engine.objects.ShipTemplate;

public class ShipRenderer extends Drawable {
	
	private String KEY = "scout";
	private static String GLOW_KEY = "glow";
	private static final String DIRECTION = "direction";
	protected ShipTemplate host;
	private static int glowTexture;
	private float glowScale = 0;
	
	public ShipRenderer(ShipTemplate host, String key,int priority) {
		super(priority);
		this.host = host;
		KEY = key;
		
		buildBuffers();
	}
	
	private void buildBuffers(){
		glowScale = (host.height+host.width)/80;
		
		if(safe().getVertex(GLOW_KEY) == null){
			
			float s = 30;
			float vertices[] = {
				-s,-s,0,
				s,-s,0,
				-s,s,0,
				s,s,0
			};
			
			safe().addVertex(GLOW_KEY, build().customBuffer(vertices));
		}
		
		if(safe().getVertex(KEY) == null){
			int w = host.width/2;
			int h = host.height/2;
			float vertices[] = {
				-w,-h,0,
				w,-h,0,
				-w,h,0,
				w,h,0
			};
			
			safe().addVertex(KEY, build().customBuffer(vertices));
		}
		if(safe().getTextureBuffer(KEY) == null){
			safe().addTextureBuffer(KEY, build().textureBuffer(0, 1, 0, 1));
		}
	}
	
	protected void onInit(GL10 gl){
		if(gl instanceof GL11){
			GPUMemoryManager manager = GPUMemoryManager.instance();
			if(manager.getVertexPointer(KEY) == 0){
				manager.toVertexPointer(gl, KEY, safe().getVertex(KEY));
			}
			if(manager.getVertexPointer(GLOW_KEY) == 0){
				manager.toVertexPointer(gl, GLOW_KEY, safe().getVertex(GLOW_KEY));
			}
			if(manager.getTexturePointer(KEY) == 0){
				manager.toTexturePointer(gl, KEY, safe().getTextureBuffer(KEY));
			}
		}
	}
	
	protected void onDraw(GL10 gl){
		/* if OpenGL ES 1.1 is supported. */
		if(gl instanceof GL11){
			GL11 gl11 = (GL11) gl;
			
			gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, GPUMemoryManager.instance().getTexturePointer(KEY));
			gl11.glTexCoordPointer(2, GL11.GL_FLOAT, 0, 0);
			
			gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, GPUMemoryManager.instance().getVertexPointer(KEY));
			gl11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);
			gl11.glRotatef(host.angle, 0, 0, 1);
			gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, safe().getSize(KEY)/3);
		} else {
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, safe().getTextureBuffer(KEY));
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, safe().getVertex(KEY));
			gl.glRotatef(host.angle, 0, 0, 1);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, safe().getSize(KEY)/3);
		}
	}
	
	protected void postDraw(GL10 gl){
		
		if(gl instanceof GL11) {
			GL11 gl11 = (GL11) gl;
			
			/* Draws a collision death effect. Texture used here should be defined by the sub-class after onDraw and supered here afterwards. */
			if(host.dieOnCollision && host.collision_left > 0) {
				gl11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
				/* bleed effect upon death hit. */
				float s = (ShipTemplate.COLLISION_DURATION-host.collision_left)/(float)ShipTemplate.COLLISION_DURATION;
				gl11.glColor4f(s, s, s, 1);
				gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, safe().getSize(KEY)/3);
				
				/* glow effect upon death hit. */
				TextureManager.bindTexture(gl, glowTexture);
				gl11.glVertexPointer(3, GL11.GL_FLOAT, 0, safe().getVertex(GLOW_KEY));
				gl11.glScalef(glowScale, glowScale, 1);
				gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, safe().getSize(GLOW_KEY)/3);
				
				gl11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			}
			
			gl11.glColor4f(1, 1, 1, 1);
			gl11.glPopMatrix();
		} else {
			/* Draws a collision death effect. Texture used here should be defined by the sub-class after onDraw and supered here afterwards. */
			if(host.dieOnCollision && host.collision_left > 0) {
				gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE);
				/* bleed effect upon death hit. */
				float s = (ShipTemplate.COLLISION_DURATION-host.collision_left)/(float)ShipTemplate.COLLISION_DURATION;
				gl.glColor4f(s, s, s, 1);
				gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, safe().getSize(KEY)/3);
				
				/* glow effect upon death hit. */
				TextureManager.bindTexture(gl, glowTexture);
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, safe().getVertex(GLOW_KEY));
				gl.glScalef(glowScale, glowScale, 1);
				gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, safe().getSize(GLOW_KEY)/3);
				
				gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			}
			
			gl.glColor4f(1, 1, 1, 1);
			gl.glPopMatrix();
		}
	}
	
	public static void loadTextures(GL10 gl){
		try {
			glowTexture = TextureManager.getTextureId(gl, GLOW_KEY, GL10.GL_CLAMP_TO_EDGE, GL10.GL_CLAMP_TO_EDGE);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}

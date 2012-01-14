package space.util.graphics;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import samik.android.util.opengl.Drawable;
import samik.android.util.opengl.GPUMemoryManager;
import samik.android.util.opengl.TextureManager;
import space.util.effects.Effect;
import space.util.effects.EffectFactory;

public class EffectRenderer extends Drawable {
	
	public EffectRenderer() {
		super(5);
		buildBuffers();
	}
	
	private void buildBuffers(){
		if(safe().getVertex(Effect.KEY_GLOW) == null){
			
			float s = 30;
			float vertices[] = {
				-s,-s,0,
				s,-s,0,
				-s,s,0,
				s,s,0
			};
			
			safe().addVertex(Effect.KEY_GLOW, build().customBuffer(vertices));
		}
		if(safe().getTextureBuffer(Effect.KEY_GLOW) == null){
			safe().addTextureBuffer(Effect.KEY_GLOW, build().textureBuffer(0, 1, 0, 1));
		}
	}
	
	protected void onInit(GL10 gl){
		if(gl instanceof GL11){
			GPUMemoryManager manager = GPUMemoryManager.instance();
			if(manager.getVertexPointer(Effect.KEY_GLOW) == 0){
				manager.toVertexPointer(gl, Effect.KEY_GLOW, safe().getVertex(Effect.KEY_GLOW));
			}
			if(manager.getTexturePointer(Effect.KEY_GLOW) == 0){
				manager.toTexturePointer(gl, Effect.KEY_GLOW, safe().getTextureBuffer(Effect.KEY_GLOW));
			}
		}
	}
	
	protected void onDraw(GL10 gl){
		if(gl instanceof GL11){
			GL11 gl11 = (GL11) gl;
			
			List<Effect> list = EffectFactory.instance().effects();
			
			for(Effect p : list){
				gl11.glPushMatrix();
				gl11.glScalef(p.scalex, p.scaley, 1);
				gl11.glTranslatef(p.getX(), p.getY(), 0);
				gl11.glColor4f(p.R, p.G, p.B, p.A);
				try {
					TextureManager.bindTexture(gl, TextureManager.getTextureId(gl, p.KEY, GL11.GL_CLAMP_TO_EDGE, GL11.GL_CLAMP_TO_EDGE));
				} catch (Exception e) {
					e.printStackTrace();
				}
	
				gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, GPUMemoryManager.instance().getTexturePointer(p.KEY));
				gl11.glTexCoordPointer(2, GL11.GL_FLOAT, 0, 0);
				gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, GPUMemoryManager.instance().getVertexPointer(p.KEY));
				gl11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);
				gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, safe().getSize(p.KEY)/3);
				gl11.glPopMatrix();
			}
			gl11.glColor4f(1, 1, 1, 1);

		} else {
			List<Effect> list = EffectFactory.instance().effects();
			
			for(Effect p : list){
				gl.glPushMatrix();
				gl.glScalef(p.scalex, p.scaley, 1);
				gl.glTranslatef(p.getX(), p.getY(), 0);
				gl.glColor4f(p.R, p.G, p.B, p.A);
				try {
					TextureManager.bindTexture(gl, TextureManager.getTextureId(gl, p.KEY, GL10.GL_CLAMP_TO_EDGE, GL10.GL_CLAMP_TO_EDGE));
				} catch (Exception e) {
					e.printStackTrace();
				}
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, safe().getTextureBuffer(p.KEY));
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, safe().getVertex(p.KEY));
				gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, safe().getSize(p.KEY)/3);
				gl.glPopMatrix();
			}
			gl.glColor4f(1, 1, 1, 1);
		}
	}
}

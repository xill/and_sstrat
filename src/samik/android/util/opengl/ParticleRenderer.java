package samik.android.util.opengl;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import samik.android.util.opengl.Drawable;
import samik.util.particles.Particle;
import samik.util.particles.ParticleFactory;

public class ParticleRenderer extends Drawable {

	private static final String KEY = "particle";
	
	public ParticleRenderer() {
		super(10);
		buildBuffers();
	}
	
	private void buildBuffers(){
		if(safe().getVertex(KEY) == null){
			float w = 0.5f;
			float h = 0.5f;
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
			if(manager.getTexturePointer(KEY) == 0){
				manager.toTexturePointer(gl, KEY, safe().getTextureBuffer(KEY));
			}
		}
	}
	
	protected void preDraw(GL10 gl){
		if(gl instanceof GL11){
			GL11 gl11 = (GL11) gl;
			
			gl11.glDisable(GL10.GL_TEXTURE_2D);
		} else {
			gl.glDisable(GL10.GL_TEXTURE_2D);
		}
	}
	
	protected void onDraw(GL10 gl){
		List<Particle> list = ParticleFactory.instance().particles();
		
		if(gl instanceof GL11){
			GL11 gl11 = (GL11) gl;
			
			gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, GPUMemoryManager.instance().getTexturePointer(KEY));
			gl11.glTexCoordPointer(2, GL11.GL_FLOAT, 0, 0);
			gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, GPUMemoryManager.instance().getVertexPointer(KEY));
			gl11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);
			for(Particle p : list){
				gl11.glPushMatrix();
				gl11.glTranslatef(p.getX(), p.getY(), 0);
				gl11.glColor4f(p.R, p.G, p.B, p.A);
				gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, safe().getSize(KEY)/3);
				gl11.glPopMatrix();
			}
			gl11.glColor4f(1, 1, 1, 1);
		} else {
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, safe().getTextureBuffer(KEY));
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, safe().getVertex(KEY));
			for(Particle p : list){
				gl.glPushMatrix();
				gl.glTranslatef(p.getX(), p.getY(), 0);
				gl.glColor4f(p.R, p.G, p.B, p.A);
				gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, safe().getSize(KEY)/3);
				gl.glPopMatrix();
			}
			gl.glColor4f(1, 1, 1, 1);
		}
	}
	
	protected void postDraw(GL10 gl){
		if(gl instanceof GL11){
			GL11 gl11 = (GL11) gl;
			
			gl11.glEnable(GL10.GL_TEXTURE_2D);
		} else {
			gl.glEnable(GL10.GL_TEXTURE_2D);
		}
	}

}

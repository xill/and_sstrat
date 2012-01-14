package space.game.graphics;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import samik.android.util.opengl.Drawable;
import samik.android.util.opengl.GPUMemoryManager;
import samik.android.util.opengl.TextureManager;

import android.util.Log;

public class SelectRenderer extends Drawable {

	public float x,y;
	public float select_scale = 1.2f;
	private static String KEY = "selector";
	private static int texture;
	
	public SelectRenderer() {
		super(1);
		setActive(false);
		if(safe().getVertex(KEY) == null){
			
			float s = 30;
			float vertices[] = {
				-s,-s,0,
				s,-s,0,
				-s,s,0,
				s,s,0
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
			
			gl11.glPushMatrix();
			gl11.glTranslatef(x, y, 0);
			gl11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
			gl11.glColor4f(0.0f, 0.7f, 0.0f, 1);
			gl11.glScalef(select_scale, select_scale, 1);			
		} else {
			gl.glPushMatrix();
			gl.glTranslatef(x, y, 0);
			gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE);
			gl.glColor4f(0.0f, 0.7f, 0.0f, 1);
			gl.glScalef(select_scale, select_scale, 1);
		}
	}
	
	protected void onDraw(GL10 gl){
		if(gl instanceof GL11){
			GL11 gl11 = (GL11) gl;
			
			TextureManager.bindTexture(gl, texture);
			gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, GPUMemoryManager.instance().getTexturePointer(KEY));
			gl11.glTexCoordPointer(2, GL11.GL_FLOAT, 0, 0);
			gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, GPUMemoryManager.instance().getVertexPointer(KEY));
			gl11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);
			gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, safe().getSize(KEY)/3);			
		} else {
			TextureManager.bindTexture(gl, texture);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, safe().getTextureBuffer(KEY));
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, safe().getVertex(KEY));
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, safe().getSize(KEY)/3);
		}
	}
	
	protected void postDraw(GL10 gl){
		if(gl instanceof GL11){
			GL11 gl11 = (GL11) gl;
			
			gl11.glColor4f(1, 1, 1, 1);
			gl11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			gl11.glPopMatrix();
		} else {
			gl.glColor4f(1, 1, 1, 1);
			gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			gl.glPopMatrix();
		}
	}

	public static void loadTextures(GL10 gl){
		try {
			texture = TextureManager.getTextureId(gl, KEY, GL10.GL_CLAMP_TO_EDGE, GL10.GL_CLAMP_TO_EDGE);
		} catch(Exception e){
			Log.e("ScoutRenderer","Problem loading textures");
		}
	}
}

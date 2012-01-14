package space.game.graphics;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.util.Log;

import samik.android.util.opengl.TextureManager;
import space.game.engine.objects.StarBase;

public class StarBaseRenderer extends ShipRenderer {
	
	private static final String KEY = "starbase";
	private static int[] textures;
	
	public StarBaseRenderer(StarBase host) {
		super(host,KEY,2);
	}
	
	protected void preDraw(GL10 gl){
		if(gl instanceof GL11){
			GL11 gl11 = (GL11) gl;
			
			gl11.glPushMatrix();
			gl11.glTranslatef(host.x, host.y, 0);
			TextureManager.bindTexture(gl, textures[0]);
		} else {
			gl.glPushMatrix();
			gl.glTranslatef(host.x, host.y, 0);
			TextureManager.bindTexture(gl, textures[0]);
		}
	}
	
	protected void postDraw(GL10 gl){
		if(host.dieOnCollision && host.collision_left > 0) {
			TextureManager.bindTexture(gl, textures[1]);
		}
		super.postDraw(gl);
	}
	
	public static void loadTextures(GL10 gl){
		if(textures == null) {
			textures = new int[2];
		}
		try {
			textures[0] = TextureManager.getTextureId(gl, KEY, GL10.GL_CLAMP_TO_EDGE, GL10.GL_CLAMP_TO_EDGE);
			textures[1] = TextureManager.getTextureId(gl, KEY+"_blank", GL10.GL_CLAMP_TO_EDGE, GL10.GL_CLAMP_TO_EDGE);
		} catch(Exception e){
			Log.e("ScoutRenderer","Problem loading textures");
		}
	}
}

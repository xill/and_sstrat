package space.game.graphics;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.util.Log;
import samik.android.util.opengl.TextureManager;
import space.game.engine.objects.ShipTemplate;

public class AsteroidRenderer extends ShipRenderer {

	private final static String KEY = "asteroid";
	private static int texture;
	
	public AsteroidRenderer(ShipTemplate host) {
		super(host,KEY, 2);
	}

	protected void preDraw(GL10 gl){
		if(gl instanceof GL11){
			GL11 gl11 = (GL11) gl;
			
			gl11.glPushMatrix();
			gl11.glTranslatef(host.x, host.y, 0);
			TextureManager.bindTexture(gl, texture);
		} else {
			gl.glPushMatrix();
			gl.glTranslatef(host.x, host.y, 0);
			TextureManager.bindTexture(gl, texture);
		}
	}

	
	public static void loadTextures(GL10 gl){
		try {
			texture = TextureManager.getTextureId(gl, KEY, GL10.GL_CLAMP_TO_EDGE, GL10.GL_CLAMP_TO_EDGE);
		} catch(Exception e){
			Log.e("AsteroidRenderer","Problem loading textures");
		}
	}
}

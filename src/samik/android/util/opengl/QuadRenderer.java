package samik.android.util.opengl;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import samik.util.math.Vec2f;

/**
 * Basic quad renderer.
 * 
 * draws a simple texture to a set priority layer set by the key value from the TextureManager and BufferSafe classes.
 * 
 * @author Sami
 *
 */
public class QuadRenderer extends Drawable {

	public Vec2f location,dimension;
	public String KEY;
	
	public QuadRenderer(Vec2f location, Vec2f dimension , String key ,int priority) {
		super(priority);
		this.location = location;
		this.dimension = dimension;
		this.KEY = key;
		
		if(safe().getVertex(KEY) == null){
			float[] vertex = {
				0			,0			,0,
				dimension.x	,0			,0,
				0			,dimension.y,0,
				dimension.x	,dimension.y,0
			};
			
			safe().addVertex(KEY, build().customBuffer(vertex));
		}
		
		if(safe().getTextureBuffer(KEY) == null){
			float[] tex = {
				0.0f	,0.0f,
				1.0f	,0.0f,
				0.0f	,1.0f,
				1.0f	,1.0f
			};
					
			safe().addTextureBuffer(KEY, build().customBuffer(tex));
		}
	}

	/**
	 * Recreates buffers associated to this object.
	 * 
	 * Changed dimension and key values won't reflect the truth until redefined.
	 */
	public void redefine(){
		float[] vertex = {
			0			,0			,0,
			dimension.x	,0			,0,
			0			,dimension.y,0,
			dimension.x	,dimension.y,0
		};
			
		safe().addVertex(KEY, build().customBuffer(vertex));
		
		float[] tex = {
			0.0f	,0.0f,
			1.0f	,0.0f,
			0.0f	,1.0f,
			1.0f	,1.0f
		};
			
		safe().addTextureBuffer(KEY, build().customBuffer(tex));
	}
	
	protected void preDraw(GL10 gl){
		gl.glPushMatrix();
	}
	
	protected void onDraw(GL10 gl){
		if(gl instanceof GL11){
			GL11 gl11 = (GL11) gl;
			
			gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
		}
		
		TextureManager.bindTexture(gl, KEY);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, safe().getTextureBuffer(KEY));
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, safe().getVertex(KEY));
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, safe().getSize(KEY)/3);
	}
	
	protected void postDraw(GL10 gl){
		gl.glPopMatrix();
	}
}

package samik.android.util.opengl;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import samik.util.TextDataObject;
import samik.util.TextWrapper;

/**
 * More universal text rendering class.
 * 
 * Uses OpenglTextRenderer class to render 
 * all TextDataObjects within given multiton key string.
 * 
 * @author Sami
 *
 */
public class TextWrapperRenderer extends Drawable {
	
	private String KEY;
	private static OpenglTextRenderer text;
	private TextWrapper wrapper;
	
	public TextWrapperRenderer(String key) {
		super(10);
		this.KEY = key;
		wrapper = TextWrapper.instance(KEY);
		if(text == null) text = new OpenglTextRenderer();
	}
	
	protected void onDraw(GL10 gl){
		if(gl instanceof GL11) {
			((GL11)gl).glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
		}
		
		for(TextDataObject t : wrapper.m_stack){
			if(t != null && t.alive) {
				if(t.useColors) gl.glColor4f(t.r, t.g, t.b, t.a);
				text.drawString(gl, t.x, t.y, t.text, t.cursive);
				if(t.useColors) gl.glColor4f(1, 1, 1, 1);
			}
		}
	}
	
	public static void loadTextures(GL10 gl){
		if(text == null){
			text = new OpenglTextRenderer();
		}
		text.loadTexture(gl, "font");
	}

}

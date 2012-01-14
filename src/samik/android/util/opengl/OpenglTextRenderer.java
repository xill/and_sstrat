package samik.android.util.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.opengles.GL10;

/**
 *  Text class for opengl canvas.
 *
 */
public class OpenglTextRenderer {

	private int textureId;
	Map<Character, Glyph> characters = new HashMap<Character, Glyph>();
	Map<Character, Glyph> cursive_characters = new HashMap<Character, Glyph>();

	class Glyph {
		/**
		 * Class that holds the coordinates of the
		 * character in the texture.
		 *
		 * x1,y1  
		 *  *-------* x2,y2
		 *  *      /*
		 *  *     / *
		 *  *    /  *
		 *  *   /   *
		 *  *  /    *
		 *  * /     *
		 *  *-------* x4,y4
		 * x3,y3  
		 **/ 
		private FloatBuffer vertexBuffer, textureBuffer;
		
		public Glyph(
				float x1, float y1, float x2, float y2,
				float x3, float y3, float x4, float y4
		) {
			float one = 16;
			float vertices[] = {
					0, 0, 0,
					one, 0, 0,
					0, one, 0,
					one, one, 0,
			};
			
			ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
			vbb.order(ByteOrder.nativeOrder());
			vertexBuffer = vbb.asFloatBuffer();
			vertexBuffer.put(vertices);
			vertexBuffer.position(0);
			
			float textureCoords[] = {
					x3, y3,
					x4, y4,
					x1, y1,
					x2, y2
			};
			
			ByteBuffer tbb = ByteBuffer.allocateDirect(textureCoords.length * 4);
			tbb.order(ByteOrder.nativeOrder());
			textureBuffer = tbb.asFloatBuffer();
			textureBuffer.put(textureCoords);
			textureBuffer.position(0);
		}
		
		public void draw(GL10 gl) {
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		}
	}
	
	public OpenglTextRenderer() {
		buildFont();
	}
	
	public void loadTexture(GL10 gl , String texture){
		try {
			textureId = TextureManager.getTextureId(gl, texture, GL10.GL_CLAMP_TO_EDGE, GL10.GL_CLAMP_TO_EDGE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void buildFont() {
		float x, y;
				
		// teH magical characters
		Character[] chars = {
				'\u00C9', '\u00E6', '\u00C6', '\u00F4',
				'\u00F6', '\u00F2', '\u00FB', '\u00F9', 
				'\u00FF', '\u00D6', '\u00DC', '\u00F8',
				'\u00A3', '\u00D8', '\u00D7', '\u0192', 

				'\u00C7', '\u00FC', '\u00EA', '\u00E2',
				'\u00E4', '\u00E0', '\u00E5', '\u00E7', 
				'\u00EA', '\u00EB', '\u00E8', '\u00EF',
				'\u00EE', '\u00EC', '\u00C4', '\u00C5', 
				
				'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
				'x', 'y', 'z', '{', '|', '}', '~', ' ',

				'`', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
				'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
				
				'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
				'X', 'Y', 'Z', '[', '\\', ']', '^', '_',

				'@', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
				'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
				
				'0', '1', '2', '3', '4', '5', '6', '7',
				'8', '9', ':', ';', '<', '=', '>', '?',

				' ', '!', '"', '#', '$', '%', '&', '\'',
				'(', ')', '*', '+', ',', '-', '.', '/',
		};
		
		Map<Character, Glyph> ptr = cursive_characters;
		
		for (int i=0; i<256; i++) {
			x = (float) ((i%16)/16.0);
			y = (float) ((i/16)/16.0);

			if (i==chars.length) {
				ptr = characters;
			}
			
			if (! ptr.containsKey(chars[i%chars.length])) {
				ptr.put(chars[i%chars.length],
						new Glyph(
								x,			1-y,
								x+0.0625f,	1-y,
								x,			1-y-0.0625f,
								x+0.0625f,	1-y-0.0625f							
						)
				);
			}
		}
	}
	
	/**
	 * Draws given string on screen.
	 * 
	 * @param gl
	 * @param x
	 * @param y
	 * @param s
	 * @param cursive
	 */
	public void drawString(GL10 gl, int x, int y, String s, boolean cursive) {
		TextureManager.bindTexture(gl, textureId);
		
		gl.glPushMatrix();
		
		gl.glTranslatef(x, y, 0);
		
		Map<Character, Glyph> ptr = (cursive) ? cursive_characters : characters;
		
		for (int i=0; i<s.length(); i++) {
			if (ptr.containsKey(s.charAt(i))) {
				ptr.get(s.charAt(i)).draw(gl);
			}
			gl.glTranslatef(10, 0, 0);
		}
		
		gl.glPopMatrix();
	}
	
}

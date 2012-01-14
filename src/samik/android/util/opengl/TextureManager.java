package samik.android.util.opengl;

import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

/**
 * Class for handling and loading textures.
 * 
 * @author sami
 *
 */
public class TextureManager {
	
	private static HashMap<String, Integer> idmap;
	private static HashMap<String, Integer> texturemap;
	private static int latestBind;
	private static Context context;
	
	private TextureManager(){
		
	}
	
	/**
	 * Initializes texture loading.
	 * 
	 * @param c - Current Activity context.
	 */
	public static void init(Context c){
		context = c;
		idmap = new HashMap<String, Integer>();
		texturemap = new HashMap<String, Integer>();
	}
	
	/**
	 * Binds the texture if security check deems it necessary.
	 * 
	 * @param gl - OpenGL 1.0 context.
	 * @param key - texture string key.
	 */
	public static void bindTexture(GL10 gl, String key) {
		Integer texture = texturemap.get(key);
		if(texture != null && latestBind != texture){
			gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
			latestBind = texture;
		}
	}
	
	/**
	 * Binds the texture if security check deems it necessary.
	 * 
	 * @param gl - OpenGL 1.0 context.
	 * @param texture - texture ID returned by getTextureId.
	 */
	public static void bindTexture(GL10 gl, int texture) {
		if(latestBind != texture){
			gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
			latestBind = texture;
		}
	}
	
	private static void loadTexture(GL10 gl, String key, int xWrap, int yWrap) throws Exception {
		int[] texture = new int[1];
		gl.glGenTextures(1, texture, 0);
		int textureId = texture[0];
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, xWrap);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, yWrap);
		
		gl.glTexEnvf(GL10.GL_TEXTURE_ENV,GL10.GL_TEXTURE_ENV_MODE,GL10.GL_MODULATE);
		
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), getMapValue(key));
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		bitmap.recycle();
		texturemap.put(key, textureId);
	}
	
	private static int getMapValue(String key) throws Exception {
		Integer mappedId = idmap.get(key);
		if(mappedId == null){
			throw new Exception("Id map does not contain the key specified.");
		}
		return mappedId;
	}
	
	public static int getTextureId(GL10 gl, String key, int xWrap, int yWrap) throws Exception {
		Integer id = texturemap.get(key);
		if(id == null) {
			loadTexture(gl, key, xWrap, yWrap);
			id = texturemap.get(key);
			if(id == null){
				 throw new Exception("Cannot load texture");
			}
		}
		return id;
	}
	
	/**
	 * Releases all texture memory currently in use.
	 * 
	 * @param gl - OpenGL 1.0 context.
	 */
	public static void destroyTextures(GL10 gl){
		if(!texturemap.isEmpty()){
			
			for(Integer i : texturemap.values()){
				int[] array = new int [1];
				array[0] = i;
				gl.glDeleteTextures(1, array, 0);
				
			}
		} 
		texturemap.clear();
	}
	
	/**
	 * Wipes String key to Integer id references. Actual textures still exist.
	 */
	public static final void wipeMappedTextures(){
		idmap.clear();
	}
	
	public static void mapTexture(String name, int id){
		idmap.put(name, id);
	}
	
	public static void unmapTexture(String name){
		idmap.remove(name);
	}
}

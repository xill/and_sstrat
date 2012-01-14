package samik.android.util.opengl;

import java.nio.FloatBuffer;
import java.util.HashMap;

/**
 * Holds all given float buffered objects.
 * 
 * @author Sami
 *
 */
public class BufferSafe {
	private volatile static HashMap<String,FloatBuffer> m_buffer_stack;
	private volatile static HashMap<String,FloatBuffer> m_textureBuffer_stack;
	private volatile static HashMap<String,String> m_texturefile_stack;
	private volatile static HashMap<String,Integer> m_size_stack;
	public String latest_vertex;
	public String latest_texture;
	
	protected BufferSafe() {
		if(m_buffer_stack == null){
			m_buffer_stack = new HashMap<String,FloatBuffer>();
		}
		if(m_textureBuffer_stack == null){
			m_textureBuffer_stack = new HashMap<String,FloatBuffer>();
		}
		if(m_texturefile_stack == null){
			m_texturefile_stack = new HashMap<String,String>();
		}
		if(m_size_stack == null){
			m_size_stack = new HashMap<String,Integer>();
		}
	}
	
	/**
	 * adds a additional FloatBuffer to the buffer safe.
	 * 
	 * @param key - key value which identifies given FloatBuffer. 
	 * @param f - FloatBuffer object to link.
	 */
	public final void addVertex(String key,FloatBuffer f){
		m_buffer_stack.put(key, f);
		m_size_stack.put(key, f.capacity());
	}
	
	/**
	 * adds a additional FloatBuffer to the buffer safe.
	 * 
	 * @param key - key value which identifies given FloatBuffer.
	 * @param f - FloatBuffer object to link.
	 */
	public final void addTextureBuffer(String key, FloatBuffer f){
		m_textureBuffer_stack.put(key, f);
	}
	
	/**
	 * Link given key to texture file id.
	 * 
	 * @param key - Key value of any UV that uses given id texture.
	 * @param id - Same string used by the TextureManager class to find a specific texture.
	 */
	public final void addTextureFile(String key, String id){
		m_texturefile_stack.put(key, id);
	}
	
	public final FloatBuffer getTextureBuffer(String s){
		latest_texture = s;
		return m_textureBuffer_stack.get(s);
	}
	
	public final FloatBuffer getVertex(String s){
		latest_vertex = s;
		return m_buffer_stack.get(s);
	}
	
	public final int getSize(String s){
		return m_size_stack.get(s);
	}
	
	public final String getTextureFileId(String s){
		return m_texturefile_stack.get(s);
	}
	
	/**
	 * Clears all stacks saved to this object.
	 */
	public final void wipe(){
		m_buffer_stack.clear();
		m_textureBuffer_stack.clear();
		m_texturefile_stack.clear();
		m_size_stack.clear();
	}
}

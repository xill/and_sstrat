package samik.android.util.opengl;

import java.nio.FloatBuffer;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

/**
 * Handles stored VBO pointer memory allocations.
 * Using this class requires a GL11 supporting Android phone.
 * 
 * @author Sami
 *
 */
public class GPUMemoryManager {

	private static HashMap<String,Integer> m_texturepointers;
	private static HashMap<String,Integer> m_vertexpointers;
	
	private static GPUMemoryManager m_instance;
	
	private GPUMemoryManager() {
		m_texturepointers = new HashMap<String,Integer>();
		m_vertexpointers = new HashMap<String,Integer>();
	}
	
	public final static GPUMemoryManager instance(){
		if(m_instance == null){
			m_instance = new GPUMemoryManager();
		}
		return m_instance;
	}
	
	/**
	 * Turn build FloatBuffer texture coordinate list into a GPU memory pointer.
	 * 
	 * @param gl - OpenGL 1.0 context.
	 * @param key - key string to later get the texture pointer. if set to null the pointer is not saved. 
	 * @param textureBuffer - should contain the UV coordinates for the texture.
	 * @return built texture VBO pointer.
	 */
	public final int toTexturePointer(GL10 gl, String key, FloatBuffer textureBuffer){
		if(gl instanceof GL11){
			// this thing is completely untested. so... continue here ;>
			GL11 gl11 = (GL11) gl;
			int textureBufferIndex = 0;
			int[] buffer = new int[1];
			gl11.glGenBuffers(1, buffer, 0);
	        textureBufferIndex = buffer[0];
	        gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, textureBufferIndex);
	        gl11.glBufferData(GL11.GL_ARRAY_BUFFER, textureBuffer.capacity() * 4, textureBuffer, GL11.GL_STATIC_DRAW);
	        
	        if(key != null){
	        	m_texturepointers.put(key, textureBufferIndex);
	        }
	        
			return textureBufferIndex;
		} else {
			return 0;
		}
	}
	
	public final int getTexturePointer(String key){
		Integer tmp = m_texturepointers.get(key);
		return (tmp != null) ? tmp : 0 ;
	}
	
	/**
	 * Turn build FloatBuffer vertex coordinate list into a GPU memory pointer.
	 * 
	 * @param gl - OpenGL 1.0 context.
	 * @param key - key string to later get the vertex pointer. if set to null the pointer is not saved. 
	 * @param vertexBuffer - should contain the coordinates for the VBO.
	 * @return built vertex VBO pointer.
	 */
	public final int toVertexPointer(GL10 gl, String key, FloatBuffer vertexBuffer){
		if(gl instanceof GL11){
			// this thing is completely untested. so... continue here ;>
			GL11 gl11 = (GL11) gl;
			int vertexBufferIndex = 0;
			int[] buffer = new int[1];
			gl11.glGenBuffers(1, buffer, 0);
			vertexBufferIndex = buffer[0];
	        gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, vertexBufferIndex);
	        gl11.glBufferData(GL11.GL_ARRAY_BUFFER, vertexBuffer.capacity() * 4, vertexBuffer, GL11.GL_STATIC_DRAW);
	        
	        if(key != null){
	        	m_vertexpointers.put(key, vertexBufferIndex);
	        }
	        
			return vertexBufferIndex;
		} else {
			return 0;
		}
	}
	
	public final int getVertexPointer(String key){
		Integer tmp = m_vertexpointers.get(key);
		return (tmp != null ) ? tmp : 0;
	}
	
	public final void deletePointer(GL10 gl, String key){
		if(gl instanceof GL11){
			GL11 gl11 = (GL11) gl;
			Integer tmp;
			if((tmp = m_texturepointers.get(key)) != null){
				int[] array = new int [1];
				array[0] = tmp;
				gl11.glDeleteBuffers(1, array, 0);
			}
			
			if((tmp = m_vertexpointers.get(key)) != null){
				int[] array = new int [1];
				array[0] = tmp;
				gl11.glDeleteBuffers(1, array, 0);
			}
		}
	}
	
	public final void deletePointers(GL10 gl){
		if(gl instanceof GL11){
			GL11 gl11 = (GL11) gl;
			
			if(!m_texturepointers.isEmpty()){
				
				for(Integer i : m_texturepointers.values()){
					int[] array = new int [1];
					array[0] = i;
					gl11.glDeleteBuffers(1, array, 0);
				}
			} 
			m_texturepointers.clear();
			
			if(!m_vertexpointers.isEmpty()){
				
				for(Integer i : m_vertexpointers.values()){
					int[] array = new int [1];
					array[0] = i;
					gl11.glDeleteBuffers(1, array, 0);
				}
			} 
			m_vertexpointers.clear();
		}
	}
}

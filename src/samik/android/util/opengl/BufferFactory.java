package samik.android.util.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * builds a defined float buffered object.
 * and then returns it. Doesn't keep any of the build objects.
 * 
 * @author Sami
 *
 */
public class BufferFactory {
	
	protected BufferFactory(){
		
	}
	
	public final FloatBuffer customBuffer(float[] vertices){
		return allocate(vertices);
	}
	
	/**
	 * Builds, allocates and returns a FloatBuffer object.
	 * 
	 * designed for texture buffers.
	 * 
	 * @param left
	 * @param right
	 * @param up
	 * @param down
	 * @return
	 */
	public final FloatBuffer textureBuffer(float left, float right, float up, float down){
		
		float texture[] = {
			left,up,
			right,up,
			left,down,
			right,down
		};
		
		return allocate(texture);
	}
	
	/**
	 * Builds, allocates and returns a FloatBuffer object.
	 * 
	 * designed for vertex buffers.
	 * 
	 * @param left
	 * @param right
	 * @param up
	 * @param down
	 * @return
	 */
	public final FloatBuffer centeredBuffer(float left, float right, float up, float down){
		
		float vertices[] = {
			left,up,0,
			right,up,0,
			left,down,0,
			right,down,0
		};
		
		return allocate(vertices);
	}
	
	private final FloatBuffer allocate(float[] vertices){
		FloatBuffer tmp;
		ByteBuffer bb = ByteBuffer.allocateDirect(4 * vertices.length);
		bb.order(ByteOrder.nativeOrder());
		tmp = bb.asFloatBuffer();
		tmp.put(vertices);
		tmp.position(0);
		
		return tmp;
	}
}

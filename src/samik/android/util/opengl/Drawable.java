package samik.android.util.opengl;

import javax.microedition.khronos.opengles.GL10;

/**
 * Drawable wrapper for building, storing and drawing.
 * 
 * @author Sami
 *
 */
public abstract class Drawable implements java.io.Serializable, java.lang.Comparable<Drawable>{
	
	private static BufferFactory m_factory;
	private static BufferSafe m_safe;
	private int current_priority;
	private boolean active = true;
	
	/**
	 * if onInit should be called prior to preDraw
	 */
	private boolean GLInitialized = false;
	
	public Drawable(int priority){
		current_priority = priority;
	}
	
	public static final BufferFactory build() {
		if(m_factory == null){
			m_factory = new BufferFactory();
		}
		return m_factory;
	}
	
	public static final BufferSafe safe() {
		if(m_safe == null){
			m_safe = new BufferSafe();
		}
		return m_safe;
	}
	
	/**
	 * an OpenGL init function for this drawable object.
	 * Called only once prior to first preDraw.
	 * Override to add your own functionality.
	 * 
	 * @param gl
	 */
	protected void onInit(GL10 gl){
		
	}
	
	/**
	 * When using preset draw this function is called right before.
	 * 
	 * override to add your own functionality.
	 * 
	 * @param gl - OpenGL 1.0 context.
	 */
	protected void preDraw(GL10 gl){
		
	}
	
	/**
	 * Main draw function. 
	 * Called after preDraw and just before postDraw.
	 * 
	 * Following preset functionality:
	 * 		- Draws the last vertex and texture buffers returned using Triangle strip.
	 * 
	 * override to add your own functionality.
	 * 
	 * @param gl - OpenGL 1.0 context.
	 */
	protected void onDraw(GL10 gl){
		if(m_safe != null && m_safe.getVertex(m_safe.latest_texture) != null){
			if(m_safe.getTextureBuffer(m_safe.latest_texture) != null){
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, m_safe.getTextureBuffer(m_safe.latest_vertex));
			}
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_safe.getVertex(m_safe.latest_vertex));
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, m_safe.getSize(m_safe.latest_vertex)/3);
		}
	}
	
	/**
	 * When using preset draw this function is called right after.
	 * 
	 * override to add your own functionality.
	 * 
	 * @param gl - OpenGL 1.0 context.
	 */
	protected void postDraw(GL10 gl){
		
	}
	
	
	/**
	 * sort objects by priority
	 * */
	public int compareTo(Drawable drawable) {
		if (this.current_priority == (drawable).getPriority())
			return 0;
		else if(this.current_priority > (drawable).getPriority())
			return 1;
		else 
			return -1;
	}
	
	public int getPriority() {
		return current_priority;
	}

	/**
	 * Preset draw function called by the renderer object.
	 * 
	 * @param gl - OpenGL 1.0 context.
	 */
	public final void draw(GL10 gl){
		/*
		 * This Renderer object is not active.
		 * Return and draw nothing.
		 */
		if(!active) return;
		
		/*
		 * This Renderer object has not yet been initialized.
		 */
		if(!GLInitialized) {
			onInit(gl);
			GLInitialized = true;
		}
		
		preDraw(gl);
		onDraw(gl);
		postDraw(gl);
	}
	
	/**
	 * @return True - If this drawable object is active.
	 */
	public final boolean isActive(){
		return active;
	}
	
	/**
	 * @return True - if this renderer object has been initialized.
	 */
	public final boolean isInitialized(){
		return GLInitialized;
	}
	
	/**
	 * Sets the active state for this drawable object.
	 * 
	 * @param active
	 */
	public final void setActive(boolean active){
		this.active = active;
	}
		
}
package samik.util.math;

import samik.util.BaseFactory;
import samik.util.math.Vec2f;

public class VectorFactory extends BaseFactory<Vec2f>{
	
	private static VectorFactory m_instance;
	
	private VectorFactory(){
		
	}
	
	public static VectorFactory instance(){
		if(m_instance == null){
			m_instance = new VectorFactory();
		}
		return m_instance;
	}
	
	// TODO. make all vector assigning more dynamic.
	
	public Vec2f assign(){
		Vec2f v = super.assign();
		if(v == null){
			v = new Vec2f();
		}
		v.x = 0;
		v.y = 0;
		return v;
	}
	
	public Vec2f assign(float x, float y){
		Vec2f v = super.assign();
		if(v == null){
			v = new Vec2f();
		}
		v.x = x;
		v.y = y;
		return v;
	}
	
	/**
	 * Wipes this singleton clean.
	 */
	public void clear(){
		stack().clear();
		m_instance = null;
	}
}

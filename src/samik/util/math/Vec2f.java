package samik.util.math;

/**
 * Float vector wrapper.
 * 
 * @author Sami
 *
 */
public class Vec2f {
	public float x=0,y=0;
	
	public Vec2f(){}
	
	public Vec2f(float x,float y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Calculates and return the radiant angle between the vectors.
	 * 
	 * @param vector
	 * @return
	 */
	public final float angle(Vec2f v){
		float v12 = (int) (x*v.x+y*v.y);
		float vroot = length()*v.length();
		return (float) Math.acos(v12/vroot);
	}
	
	/**
	 * Returns the length of this vector.
	 * 
	 * @return
	 */
	public final float length(){
		return (float) Math.sqrt(x*x+y*y);
	}
	
	/**
	 * Converts this vector in to a unit vector.
	 */
	public final void normalize(){
		float n = length();
		x /= n;
		y /= n;
	}
	
	/**
	 * Adds B to this vector.
	 * 
	 * A = A + B
	 * 
	 * @param B
	 */
	public final void add(Vec2f B){
		x += B.x;
		y += B.y;
	}
	
	/**
	 * Subtracts vector B from this vector.
	 * 
	 * A = A - B
	 * 
	 * @param B
	 */
	public final void subs(Vec2f B){
		x -= B.x;
		y -= B.y;
	}
	
	/**
	 * Multiplies this vector with factor B.
	 * 
	 * A.x = A.x * B
	 * A.y = A.y * B
	 * 
	 * @param B
	 */
	public final void multiply(float B){
		x *= B;
		y *= B;
	}
	
	/**
	 * Dot products this vector with vector B.
	 * 
	 * @param B
	 */
	public final void dot(Vec2f B){
		x *= B.x;
		y *= B.y;
	}
	
	/**
	 * @return distinct copy of this vector.
	 */
	public final Vec2f copy(){
		Vec2f n = new Vec2f(x,y);
		return n;
	}
}

package samik.util.math;

public class Matrixf {
	public float[] m;
	protected int dimension_x,dimension_y;
	
	public Matrixf(int dimension_x,int dimension_y){
		m = new float[dimension_x*dimension_y];
		this.dimension_x = dimension_x;
		this.dimension_y = dimension_y;
	}
	
	public Matrixf(int dimension_x,int dimension_y,float[] content){
		m = new float[dimension_x*dimension_y];
		this.dimension_x = dimension_x;
		this.dimension_y = dimension_y;
		
		if(content.length != m.length){
			for(int i = 0; i < content.length ; i++){
				if(i >= m.length){
					break;
				}
				m[i] = content[i];
			}
		} else {
			m = content;
		}
	}
	
}

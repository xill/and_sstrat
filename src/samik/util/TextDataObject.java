package samik.util;

/**
 * TextData object. Given to TextWrapper for drawing.
 */
public class TextDataObject {
	
	/**
	 * Is this text draw in cursive?
	 */
	public boolean cursive = false;
	
	/**
	 * Should the text be colored?
	 */
	public boolean useColors = false;
	
	/**
	 * Is this text alive?
	 */
	public boolean alive = true;
	
	public int x = 0 ,y = 0;
	public float r=1,g=1,b=1,a=1;
	
	/**
	 * Current text.
	 */
	public String text = "";
	
}

package space.game.engine;

/**
 * A single event in the queue
 * 
 * @author Sami
 *
 */
public class QueueData {
	
	/**
	 * Type of the object created.
	 * 
	 * Following types accepted:
	 * - text
	 * 
	 */
	public String type = "";
	
	/**
	 * Extra data for the object created.
	 * 
	 * case text : text shown.
	 */
	public String extra = "";
	
	/**
	 * Object start point.
	 */
	public int from_x = 0,from_y = 0;
	
	/**
	 * Object end point.
	 */
	public int to_x = 0,to_y = 0;
	
	/**
	 * how long until next queue event.
	 * duration is represented in milliseconds.
	 */
	public int duration = 0;
	
	/**
	 * Is this queue event joined to the next?
	 * 
	 * if true next event is activated at the same time and duration is ignored.
	 */
	public boolean joined = false;
}

package samik.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Sub-class for ObjectFactory.
 * 
 * Main purpose is to handle any sort of object it is given.
 * Recycle and reuse those objects.
 * 
 * @param <T> type of objects this class should handle.
 */
public class BaseFactory<T> {
	
	/**
	 * Big list of objects that can be reused.
	 */
	private List<T> m_free_objects = new ArrayList<T>();
	
	/**
	 * Reuse an object.
	 * 
	 * @return object to use. or null if none is available.
	 */
	public T assign(){
		if(!m_free_objects.isEmpty()){
			T m = m_free_objects.get(0);
			m_free_objects.remove(m);
			return m;
		} else {
			return null;
		}
	}
	
	/**
	 * Set the given object for reuse.
	 * 
	 * @param f
	 */
	public void free(T f){
		m_free_objects.add(f);
	}
	
	/**
	 * Returns the current stack of free objects.
	 * 
	 * @return
	 */
	public List<T> stack(){
		return m_free_objects;
	}
}

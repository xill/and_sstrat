package samik.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Text framework object.
 * 
 * holds string value, location and possible alive duration.
 * Rendering solution is not provided. This is merely a wrapper.
 * 
 * Append more to add more onscreen.
 * 
 * This class is a multiton wrapper.
 */
public class TextWrapper {
	private static Map <String,TextWrapper> m_instance;
	public volatile ArrayList<TextDataObject> m_stack;
	private String m_key = "";
	
	private TextWrapper(){
		this.m_stack = new ArrayList<TextDataObject>();
	}
	
	public static TextWrapper instance(String key){
		if(m_instance == null){
			m_instance = new HashMap<String,TextWrapper>(2);
		} else;
		if(!m_instance.containsKey(key)){
			TextWrapper tmp = new TextWrapper();
			tmp.m_key = key;
			m_instance.put(key,tmp);
		}

		return m_instance.get(key);
	}
	
	public void append(TextDataObject t){
		this.m_stack.add(t);
	}
	
	public void remove(TextDataObject t){
		this.m_stack.remove(t);
	}
	
	/**
	 * if Object has expired. remove it.
	 * 
	 * returns number of objects removed or -1 for error.
	 */
	public int removeDeadObjects(){
		if(m_stack == null){
			return -1;
		}
		int count = 0;
		for(int i = m_stack.size()-1 ; i >= 0 ; --i){
			if(!m_stack.get(i).alive){
				count++;
				m_stack.remove(i);
			}
		}
		return count;
	}
	
	/**
	 * wipes all current data.
	 */
	public void wipe(){
		m_stack.clear();
		m_instance.remove(this.m_key);
	}
	
	/**
	 * Formats a string of number into the "1 234 567" format.
	 * @param str Input string. Preferably contains long numbers, otherwise there's very little point to this.
	 * @return String Output string
	 */
	public static String formatThrees(String str)
	{
		if (str.length() < 4) 
		{
			return str;
		}
		else
		{
			String ret = "";
			int i;
			for (i = str.length() % 3; i < str.length(); i+=3)
			{
				if (i > str.length() % 3) 
				{
					ret += " "+str.substring(i-3, i);
				}
				else 
				{
					ret += str.substring(0, i);
				}
			}
			if (i <= str.length()) ret+= " "+str.substring(i-3, i);
			
			return ret;
		}
	}
}


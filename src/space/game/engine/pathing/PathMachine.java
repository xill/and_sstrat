package space.game.engine.pathing;

import java.util.ArrayList;

public class PathMachine {

	private static PathMachine instance;
	private ArrayList<PathingHandler> stack;
	private PathingHandler active;
	
	private PathMachine(){
		stack = new ArrayList<PathingHandler>();
	}
	
	public static PathMachine instance(){
		if(instance == null){
			instance = new PathMachine();
		}
		return instance;
	}
	
	public void add(PathingHandler p){
		stack.add(p);
	}

	public void remove(PathingHandler p){
		stack.remove(p);
	}
	
	/**
	 * returns the current active pathing.
	 */
	public PathingHandler getActive(){
		return active;
	}
	
	public void setActive(PathingHandler p){
		active = p;
	}
	
	/**
	 * returns a list of all current pathing elements.
	 * 
	 * @return
	 */
	public ArrayList<PathingHandler> get(){
		return stack;
	}
	
	public void wipe(){
		stack.clear();
		active = null;
	}
}

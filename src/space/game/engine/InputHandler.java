package space.game.engine;

import android.view.MotionEvent;
import android.view.View;

/**
 * Games input system.
 * 
 * @author Sami
 *
 */
public class InputHandler {
	
	private static InputHandler instance;
	private boolean isPressed = false;
	private boolean isReleased = false;
	
	/**
	 * latest cursor location.
	 */
	public int x,y;
	
	private InputHandler(){
		
	}
	
	public static InputHandler instance(){
		if(instance == null) {
			instance = new InputHandler();
		}
		return instance;
	}
	
	public void onTouch(View v, MotionEvent event){
		x = (int) event.getRawX();
		y = (int) event.getRawY();
		
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			isPressed = true;
			break;
		case MotionEvent.ACTION_UP:
			if(isPressed) isReleased = true;
			isPressed = false;
			break;
		default:
			
			break;
		}
	}
	
	public boolean isPressed(){
		return isPressed;
	}
	
	public boolean isReleased(){
		if(isReleased){
			isReleased = false;
			return true;
		}
		return false;
	}
}

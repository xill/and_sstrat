package space.game.engine;

import java.util.List;

import samik.util.TextDataObject;
import samik.util.TextWrapper;
import space.game.engine.objects.Asteroid;
import space.game.graphics.AsteroidRenderer;
import space.game.graphics.GameRenderer;

/**
 * Reads the queue of the current level and invokes different elements as required.
 * 
 * @author Sami
 *
 */
public class LevelQueue {
	
	private GameRenderer renderer;
	private World world;
	
	private List<QueueData> queue;
	private QueueData latest;
	private Object current;
	private long lastCall = 0;
	
	private enum QUEUE_STATE {
		NORMAL,
		WAITING,
		ENDING,
		ENDED
	};
	
	QUEUE_STATE m_state;
	
	public LevelQueue(List<QueueData> list, World w, GameRenderer r){
		queue = list;
		renderer = r;
		world = w;
		m_state = QUEUE_STATE.NORMAL;
		next();
	}
	
	public void update(){
		long duration = World.time - lastCall;
		if(duration > 100) duration = 100;
		lastCall = World.time;
		
		switch(m_state){
		case ENDING:
			if(latest.type.equals("end")) {
				latest.duration -= duration;
				if(latest.duration <= 0) {
					m_state = QUEUE_STATE.ENDED;
				}
			}
			break;
		case NORMAL:
			
			latest.duration -= duration;
			if(latest.duration <= 0) {
					next();
			}
			break;
		case WAITING:
		case ENDED:
			return;
		}
	}
	
	public void next(){
		if(!queue.isEmpty()){
			latest = queue.get(0);
			queue.remove(0);

			if(latest.type.equals("text")){
				wipeCurrent();
				current = new TextDataObject();
				((TextDataObject)current).x = latest.from_x;
				((TextDataObject)current).y = latest.from_y;
				((TextDataObject)current).text = latest.extra;
				TextWrapper.instance("game").append((TextDataObject)current);
			} else if(latest.type.equals("enemy")) {
				wipeCurrent();
				if(latest.extra.equals("asteroid")){
					current = new Asteroid(latest.from_x,latest.from_y);
					((Asteroid)current).path.add(latest.to_x, latest.to_y);
					world.enemies.add((Asteroid)current);
					renderer.addToQueue(new AsteroidRenderer((Asteroid)current));
					renderer.resolveQueue();
				}
			} else if(latest.type.equals("end")){
				wipeCurrent();
				m_state = QUEUE_STATE.WAITING;
			}
			
			if(latest.joined){
				next();
			}
		} else if(current != null){
			wipeCurrent();
			current = null;
		}
	}
	
	public boolean aboutToEnd(){
		return (latest.type.equals("end") && m_state.equals(QUEUE_STATE.WAITING));
	}
	
	public boolean ended(){
		return m_state.equals(QUEUE_STATE.ENDED);
	}
	
	public void showLevelClear(){
		if(latest.type.equals("end")){
			m_state = QUEUE_STATE.ENDING;
			wipeCurrent();
			current = new TextDataObject();
			((TextDataObject)current).x = latest.from_x;
			((TextDataObject)current).y = latest.from_y;
			if(latest.extra.contains("|")){
				String[] tmp = latest.extra.split("\\|");
				((TextDataObject)current).text = tmp[0];
			}
			else {
				((TextDataObject)current).text = latest.extra;
			}
			TextWrapper.instance("game").append((TextDataObject)current);
		}
	}
	
	public void showGameOver(){
		if(latest.type.equals("end")){
			m_state = QUEUE_STATE.ENDING;
			wipeCurrent();
			current = new TextDataObject();
			((TextDataObject)current).x = latest.from_x;
			((TextDataObject)current).y = latest.from_y;
			if(latest.extra.contains("|")){
				String[] tmp = latest.extra.split("\\|");
				((TextDataObject)current).text = tmp[1];
			}
			else {
				((TextDataObject)current).text = latest.extra;
			}
			TextWrapper.instance("game").append((TextDataObject)current);
		}
	}
	
	public void startEndCountdown(){
		if(queue.isEmpty()) return;
		
		while(!queue.get(0).type.equals("end")){
			queue.remove(0);
		}
		if(!queue.isEmpty()) {
			latest = queue.get(0);
			
			wipeCurrent();
			m_state = QUEUE_STATE.WAITING;
		}
	}
	
	private void wipeCurrent(){
		if(current instanceof TextDataObject){
			TextWrapper.instance("game").remove((TextDataObject)current);
		}
	}
}

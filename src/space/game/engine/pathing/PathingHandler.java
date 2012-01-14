package space.game.engine.pathing;

import java.util.ArrayList;

import samik.util.math.Vec2f;
import samik.util.math.VectorFactory;
import space.game.engine.objects.ShipTemplate;

public class PathingHandler {
	
	private static final int MIN_RADIUS = 5;
	private static final int MAX_SIZE = 20;
	private boolean finished = false;
	private ArrayList<Vec2f> path = new ArrayList<Vec2f>();
	public ShipTemplate host;
	/** is path drawn onscreen. */
	public boolean visible = true;
	public VectorFactory m_factory;
	
	public PathingHandler(){
		PathMachine.instance().add(this);
		m_factory = VectorFactory.instance();
	}
	
	public PathingHandler(ShipTemplate host){
		this.host = host;
		PathMachine.instance().add(this);
		m_factory = VectorFactory.instance();
	}
	
	protected void finalize(){
		PathMachine.instance().remove(this);
		try{
			super.finalize();
		} catch(Throwable e) {
			e.printStackTrace();
		}
	}
	
	public void add(int x, int y){
		if(path.isEmpty()){
			path.add(m_factory.assign(x,y));
		} else {
			Vec2f last = path.get(path.size()-1);
			if((x > last.x + MIN_RADIUS || x < last.x - MIN_RADIUS)
				&& (y > last.y + MIN_RADIUS || y < last.y - MIN_RADIUS))
			{
				if(path.size() >= MAX_SIZE){
					m_factory.free(path.get(0));
					path.remove(0);
				}
				path.add(m_factory.assign(x,y));
			}
		}
	}
	
	public void finished(){
		path.clear();
	}
	
	public ArrayList<Vec2f> getPath(){
		return path;
	}
	
	public boolean wasFinished(){
		if(finished){
			finished = false;
			return true;
		}
		return false;
	}
}
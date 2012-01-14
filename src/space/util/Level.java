package space.util;

import java.util.ArrayList;
import java.util.List;

import space.game.engine.QueueData;
import space.game.engine.objects.ShipTemplate;

public class Level {
	public List<ShipTemplate> friendly;
	public List<QueueData> queueEvents;
	
	public Level(){
		friendly = new ArrayList<ShipTemplate>();
		queueEvents = new ArrayList<QueueData>();
	}
}

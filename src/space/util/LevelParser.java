package space.util;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import space.game.engine.QueueData;
import space.game.engine.objects.Scout;
import space.game.engine.objects.ShipTemplate;
import space.game.engine.objects.StarBase;
import space.game.graphics.GameRenderer;

import android.util.Log;

public class LevelParser {
	private XmlPullParser xpp;
	
	public LevelParser(XmlPullParser xpp){
		this.xpp = xpp;
	}
	
	public void setParser(XmlPullParser xpp){
		this.xpp = xpp;
	}
	
	public Title parseTitle() throws XmlPullParserException, IOException {
		Title t = new Title();
		int eventType = xpp.getEventType();
		while(eventType != XmlPullParser.END_DOCUMENT){
			if(eventType == XmlPullParser.START_TAG && xpp.getName().equals("title")){
				eventType = xpp.next();
				while(!xpp.getName().equals("title")){
					if(xpp.getName().equals("name")){
						for(int i = 0; i < xpp.getAttributeCount() ; i++){
							Log.v("Launcher",xpp.getAttributeValue(i));
							if(xpp.getAttributeName(i).equals("value")){
								t.name = xpp.getAttributeValue(i);
							}
						}
					} else if(xpp.getName().equals("difficulty")){
						for(int i = 0; i < xpp.getAttributeCount() ; i++){
							Log.v("Launcher",xpp.getAttributeValue(i));
							if(xpp.getAttributeName(i).equals("value")){
								t.difficulty = xpp.getAttributeValue(i);
							}
						}
					}
					eventType = xpp.next();
				}
			}
			eventType = xpp.next();
		}
		Log.v("Launcher",t.toString());
		return t;
	}
	
	public Level parse() throws XmlPullParserException, IOException {
		Level l = new Level();
		int eventType = xpp.getEventType();
		while(eventType != XmlPullParser.END_DOCUMENT){
			Log.v("Launcher","pre looping "+xpp.getName());
			if(eventType == XmlPullParser.START_TAG && xpp.getName().equals("friendly")){
				eventType = xpp.next();
				l = parseFriendly(l);
			} else if(eventType == XmlPullParser.START_TAG && xpp.getName().equals("queue")){
				l = parseQueue(l);
			}
			eventType = xpp.next();
		}
		return l;
	}
	
	private Level parseFriendly(Level l) throws XmlPullParserException, IOException {
		Log.v("Launcher","start");
		while(!xpp.getName().equals("friendly")){
			if(xpp.getName().equals("ship")){
				ShipTemplate s = null;
				xpp.next();
				while(!xpp.getName().equals("ship")){
					Log.v("Launcher","ship looping "+xpp.getName());
					if(xpp.getName().equals("type")){
						for(int i = 0; i < xpp.getAttributeCount() ; i++){
							Log.v("Launcher","ship attr "+xpp.getAttributeName(i));
							if(xpp.getAttributeName(i).equals("value")){
								if(xpp.getAttributeValue(i).equals("scout")) s = new Scout(0,0);
								else if(xpp.getAttributeValue(i).equals("starbase")) s = new StarBase(0,0);
							}
						}
					} else if(xpp.getName().equals("attributes")){
						Log.v("Launcher","attributes "+xpp.getName());
						if(s != null){
							int w=GameRenderer.scrWidth,h=GameRenderer.scrHeight;
							for(int i = 0; i < xpp.getAttributeCount() ; i++){
								if(xpp.getAttributeName(i).equals("x")) s.x = parseLocation(i,w);
								else if(xpp.getAttributeName(i).equals("y")) s.y = parseLocation(i,h);
								else if(xpp.getAttributeName(i).equals("angle")) s.angle = Integer.parseInt(xpp.getAttributeValue(i));
							}
						}
					}
					xpp.next();
				}
				l.friendly.add(s);
			}
			xpp.next();
		}
		return l;
	}
	
	private Level parseQueue(Level l){
		int count = xpp.getAttributeCount();
		QueueData d = new QueueData();
		int w=GameRenderer.scrWidth,h=GameRenderer.scrHeight;
		for(int i = 0; i < count ; i++){
			if(xpp.getAttributeName(i).equals("type")) d.type = xpp.getAttributeValue(i);
			else if(xpp.getAttributeName(i).equals("extra")) { d.extra = xpp.getAttributeValue(i); continue; }
			else if(xpp.getAttributeName(i).equals("from_x")) { d.from_x = parseLocation(i,w); continue; }
			else if(xpp.getAttributeName(i).equals("from_y")) { d.from_y = parseLocation(i,h); continue; }
			else if(xpp.getAttributeName(i).equals("to_x")) { d.to_x = parseLocation(i,w); continue; }
			else if(xpp.getAttributeName(i).equals("to_y")) { d.to_y = parseLocation(i,h); continue; }
			else if(xpp.getAttributeName(i).equals("duration")) { d.duration = Integer.parseInt(xpp.getAttributeValue(i)); continue; }
			else if(xpp.getAttributeName(i).equals("joined")) d.joined = Boolean.parseBoolean(xpp.getAttributeValue(i));
		}
		l.queueEvents.add(d);
		return l;
	}
	
	private int parseLocation(int i, int size){
		String s = xpp.getAttributeValue(i);
		if(s.endsWith("%")){
			s = s.replace("%", "");
			float r = Float.parseFloat(s)/100.0f;
			Log.v("Launcher","parsittu R on "+r+" "+size);
			return (int) (size*r);
		}
		return Integer.parseInt(s);
	}
}

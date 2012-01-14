package space.game.engine;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import samik.android.util.opengl.TextureManager;
import space.game.graphics.GameRenderer;
import space.util.Level;
import space.util.LevelParser;
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

public class Launcher extends Activity implements OnTouchListener {
	GLSurfaceView view;
	GameRenderer renderer;
	World world;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        view = new GLSurfaceView(this);
        view.setRenderer(renderer = new GameRenderer());
        view.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        view.setOnTouchListener(this);
        setContentView(view);
        XmlPullParser xmlfile = null;
        Bundle gameBundle = getIntent().getExtras();
        
        switch(gameBundle.getInt("level",1)){
        case 1:
        	xmlfile = getResources().getXml(R.xml.level_1);
        	break;
        case 2:
        	xmlfile = getResources().getXml(R.xml.level_2);
        	break;
        case 3:
        	xmlfile = getResources().getXml(R.xml.level_3);
        	break;
        }
        
        LevelParser p = new LevelParser(xmlfile);
        Level level = null;
        try {
			level = p.parse();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        setTextures();
        new Thread(world = new World(this,renderer,view,level)).start();
    }

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		InputHandler.instance().onTouch(v, event);
		return true;
	}
	
	protected void onPause() {
		super.onPause();
		view.onPause();
		
		if(world.audio != null && world.audio.isPlaying()){
			world.audio.setLooping(false);
			world.audio.stop();			
		}
	}
	
	protected void onResume() {
		super.onResume();
		view.onResume();
		
		if(world.audio != null && !world.audio.isPlaying()){
			world.audio.setLooping(true);
			world.audio.start();
		}
	}
	
	protected void onDestroy(){
		super.onDestroy();
		world.RUNNING = false;
		finish();
	}
	
	public void finish(){
		super.finish();
	}
	
	private void setTextures(){
		TextureManager.init(this);
		TextureManager.mapTexture("scout", R.drawable.scout);
		TextureManager.mapTexture("scout_blank", R.drawable.scout_blank);
		TextureManager.mapTexture("starbase", R.drawable.starbase);
		TextureManager.mapTexture("starbase_blank", R.drawable.starbase_blank);
		TextureManager.mapTexture("asteroid",R.drawable.asteroid1);
		TextureManager.mapTexture("fog1", R.drawable.fog1);
		TextureManager.mapTexture("fog2", R.drawable.fog2);
		TextureManager.mapTexture("selector", R.drawable.select_icon);
		TextureManager.mapTexture("font", R.drawable.font_alpha);
		TextureManager.mapTexture("glow", R.drawable.glow);
		TextureManager.mapTexture("bg", R.drawable.bg);
	}
}
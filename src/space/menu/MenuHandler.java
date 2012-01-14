package space.menu;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import space.game.engine.Launcher;
import space.game.engine.R;
import space.util.LevelParser;
import space.util.Title;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MenuHandler extends Activity {
	private Button level_1,level_2,level_3;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        LevelParser parser = new LevelParser(getResources().getXml(R.xml.level_1));
        Title title_l1 = null;
        Title title_l2 = null;
        Title title_l3 = null;
        try {
			title_l1 = parser.parseTitle();
			parser.setParser(getResources().getXml(R.xml.level_2));
			title_l2 = parser.parseTitle();
			parser.setParser(getResources().getXml(R.xml.level_3));
			title_l3 = parser.parseTitle();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        setContentView(R.layout.main);
        
        Listener listener = new Listener();
        
        level_1 = (Button)findViewById(R.id.button1);
        level_1.setText(title_l1.name + " \n" + "Difficulty: "+title_l1.difficulty);
        level_1.setOnClickListener(listener);
        
        level_2 = (Button)findViewById(R.id.button2);
        level_2.setText(title_l2.name + " \n" + "Difficulty: "+title_l2.difficulty);
        level_2.setOnClickListener(listener);
        
        level_3 = (Button)findViewById(R.id.button3);
		level_3.setText(title_l3.name + " \n" + "Difficulty: "+title_l3.difficulty);
        level_3.setOnClickListener(listener);
	}
	
	protected void onPause() {
		super.onPause();
		
	}
	
	protected void onResume() {
		super.onResume();
		
	}
	
	protected void onDestroy(){
		super.onDestroy();
		finish();
		System.runFinalizersOnExit(true);
		System.exit(0);
	}
	
	private class Listener implements View.OnClickListener {
		@Override
		public void onClick(View v){
			if(v.equals(level_1)){
				Intent game = new Intent(getBaseContext(), Launcher.class);
				game.putExtra("level", 1);
				startActivity(game);
			} else if (v.equals(level_2)) {
				Intent game = new Intent(getBaseContext(), Launcher.class);
				game.putExtra("level", 2);
				startActivity(game);
			} else if (v.equals(level_3)) {
				Intent game = new Intent(getBaseContext(), Launcher.class);
				game.putExtra("level", 3);
				startActivity(game);
			}
		}
	}
	
}

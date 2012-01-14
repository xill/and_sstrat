package space.util.graphics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import samik.android.util.opengl.Drawable;

/**
 * Parse texture UV's from a file
 * 
 */
public class TextureParser {
	/**
	 * file stream containing texture uv data.
	 */
	private InputStream file;
	
	public TextureParser(InputStream stream){
		this.file = stream;
	}
	
	/**
	 * Parse the set file for uv coord data.
	 * 
	 * All the valid uv data is then given to BufferSafe for holding.
	 */
	public void parse(){
		List<String> contents = new ArrayList<String>();
		
		try{
			BufferedReader input = new BufferedReader(new InputStreamReader(file));
			try{
				String line = null;
				while((line = input.readLine()) != null){
					contents.add(line);
				}
			} finally {
				input.close();
			}
		} catch(IOException e){
			e.printStackTrace();
		}
		
		for(String s : contents){
			String[] res = s.split(" ");
			
			float x1 = Float.parseFloat(res[2])
				, y1 = Float.parseFloat(res[3])
				, x2 = Float.parseFloat(res[4])
				, y2 = Float.parseFloat(res[5]);
			
			float[] uv = 
				{
					x1 , y1,
					x2 , y1,
					x1 , y2,
					x2 , y2
				};

			Drawable.safe().addTextureFile(res[1], res[0]);
			Drawable.safe().addTextureBuffer(res[1], Drawable.build().customBuffer(uv));
		}
	}
}

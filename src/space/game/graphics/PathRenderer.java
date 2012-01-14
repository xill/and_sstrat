package space.game.graphics;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import samik.android.util.opengl.Drawable;
import samik.util.math.Vec2f;
import space.game.engine.pathing.PathMachine;
import space.game.engine.pathing.PathingHandler;

public class PathRenderer extends Drawable {
	
	public PathRenderer() {
		super(1);

	}

	@Override
	protected void preDraw(GL10 gl) {
		if(gl instanceof GL11) {
			((GL11)gl).glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
		}
		
		gl.glColor4f(1, 0, 0, 1);
		gl.glPushMatrix();
		gl.glDisable(GL10.GL_TEXTURE_2D);
	}
	
	protected void onDraw(GL10 gl) {
		List<PathingHandler> m = PathMachine.instance().get();
		for(PathingHandler host : m){
			if(!host.visible) return;
			
			List<Vec2f> l = host.getPath();
			int startp = 0;
			float vertex[];
			if(host.host != null){
				vertex = new float[l.size()*3 + 3];
				vertex[startp] = host.host.x;
				vertex[startp+1] = host.host.y;
				vertex[startp+2] = 0;
				startp += 3;
			} else {
				vertex = new float[l.size()*3];
			}
			
			for(Vec2f p : l){
				vertex[startp] = p.x;
				vertex[startp+1] = p.y;
				vertex[startp+2] = 0;
				startp += 3;
			}
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, build().customBuffer(vertex));

			gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, vertex.length/3);
		}
	}
	
	@Override
	protected void postDraw(GL10 gl) {
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glPopMatrix();
		gl.glColor4f(1, 1, 1, 1);
	}

}

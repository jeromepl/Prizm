package com.constellationgames.prizm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.constellationgames.prizm.utils.TriangleColor;

public class DragOverlay {
	
	private Triangle draggedTriangle = null;
	private int startX, startY, currentX, currentY;
	
	public void render(float delta, ShapeRenderer shapeRenderer) {
		if (draggedTriangle != null) {
			float[][] pos = draggedTriangle.getScreenPosition();
			
			// Flip y values since libGDX's coordinate system is different in render than in input processing...
			pos[0][1] = Prizm.STANDARD_HEIGHT - pos[0][1];
			pos[1][1] = Prizm.STANDARD_HEIGHT - pos[1][1];
			pos[2][1] = Prizm.STANDARD_HEIGHT - pos[2][1];
			
			float[][] dragPos = new float[pos.length][2];	
			for (int i = 0; i < pos.length; i++) {
				dragPos[i] = new float[] {pos[i][0] + currentX - startX,
						pos[i][1] - currentY + startY}; // Flip the y once again
			}
			
			TriangleColor color = draggedTriangle.getColor();
			
			// Render a semi-transparent grey triangle on top of the original one to indicate it is being moved
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(new Color(0.65f, 0.65f, 0.65f, 0.7f));
			shapeRenderer.triangle(pos[0][0], pos[0][1], pos[1][0], pos[1][1], pos[2][0], pos[2][1]);
			shapeRenderer.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);
			
			// Render the interior of the drag triangle
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(color.getColor());
			shapeRenderer.triangle(dragPos[0][0], dragPos[0][1], dragPos[1][0], dragPos[1][1], dragPos[2][0], dragPos[2][1]);
			shapeRenderer.end();
			
			// Render the border of the drag triangle
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setColor(Color.BLACK); // TODO consider changing this color
			shapeRenderer.triangle(dragPos[0][0], dragPos[0][1], dragPos[1][0], dragPos[1][1], dragPos[2][0], dragPos[2][1]);
			shapeRenderer.end();
		}
	}
	
	public void startDrag(Triangle t, int x, int y) {
		draggedTriangle = t;
		startX = x;
		startY = y;
		currentX = x;
		currentY = y;
		
		//draggedTriangle.setDragged(true);
	}
	
	public void dragTo(int x, int y) {
		if (draggedTriangle != null) {
			currentX = x;
			currentY = y;
		}
	}
	
	public void stopDrag() {
		//draggedTriangle.setDragged(false);
		draggedTriangle = null;
	}
}

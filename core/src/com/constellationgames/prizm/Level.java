package com.constellationgames.prizm;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.constellationgames.prizm.screens.GameScreen;
import com.constellationgames.prizm.utils.TriangleColor;

public class Level {
	
	private GameScreen gameScreen;
	private int levelNumber;
	
	private Triangle[][] triangles = new Triangle[8][];
	
	private Color textColor = Color.BLACK;

	public Level(GameScreen gameScreen, int levelNumber) {
		this.gameScreen = gameScreen;
		this.levelNumber = levelNumber;
		
		// Initialize the triangles array to be all blank triangles
		for (int i = 0; i < triangles.length; i++) {
			triangles[i] = new Triangle[Math.min(i + 1, 8 - i) * 2 - 1]; // Create the diamond shape
			
			for (int j = 0; j < triangles[i].length; j++) {
				triangles[i][j] = new Triangle(i, j, TriangleColor.BLANK);
			}
		}
		
		//TODO load level from file
		
		triangles[0][0].setColor(TriangleColor.BLUE);
	}
	
	public void render(float delta, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, BitmapFont font, GlyphLayout glyphLayout,
			int verticalMargin, int triangleWidth, int triangleHeight) {
		
		for (Triangle[] row : triangles) {
			for (Triangle t : row) {
				t.render(delta, shapeRenderer, verticalMargin, triangleWidth, triangleHeight);
			}
		}
	}
	
	// Free memory when exiting the game
	public void dispose() {
		
	}
	
	public int getLevelNumber() {
		return levelNumber;
	}
	
	public Triangle[][] getTriangles() {
		return triangles;
	}
}

package com.constellationgames.prizm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
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
		
		loadLevel(levelNumber);
	}
	
	private void loadLevel(int levelNumber) {
		
		//TODO there is no error handling here. A small mistake in the .csv file and everything goes boom
		
		FileHandle levelFile = Gdx.files.internal("levels/" + levelNumber + ".csv");
		String content = levelFile.readString();
		
		String[] rows = content.split("\\r?\\n"); // Split on end-of-lines
		String[][] cells = new String[rows.length][];
		for (int i = 0; i < rows.length; i++) {
			cells[i] = rows[i].split("[;,]");
		}
		
		// Generate the level based on the cells values
		TriangleColor[] triangleColors = TriangleColor.values(); // Cache the enum values
		for (int i = 0; i < triangles.length; i++) {
			
			int nbInRow = Math.min(i + 1, 8 - i) * 2 - 1;
			int horizontalOffset = (7 - nbInRow) / 2;
			
			for (int j = 0; j < triangles[i].length; j++) {
				int color = Integer.parseInt(cells[triangles.length - 1 - i][horizontalOffset + j]); // Use triangles.length - 1 - i since the coordinates are flipped
				TriangleColor triangleColor = null;
				
				// Get the corresponding TriangleColor enum value
				for (int k = 0; k < triangleColors.length; k++) {
					if(triangleColors[k].getValue() == color) {
						triangleColor = triangleColors[k];
						break;
					}
				}
				
				if (triangleColor == null)
					throw new RuntimeException("Invalid triangle color " + color);
				
				triangles[i][j].setColor(triangleColor);
			}
		}
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

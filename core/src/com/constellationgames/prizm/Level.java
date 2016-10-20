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
	
	public static final int NB_ROWS = 8;
	public static final int NB_COLUMNS = NB_ROWS - 1; // The max number of triangles on a row
	
	private GameScreen gameScreen;
	private int levelNumber;
	
	private Triangle[][] triangles = new Triangle[NB_ROWS][NB_COLUMNS];
	
	private Color textColor = Color.BLACK;

	public Level(GameScreen gameScreen, int levelNumber) {
		this.gameScreen = gameScreen;
		this.levelNumber = levelNumber;
		
		// Initialize the triangles array to be all blank triangles
		for (int i = 0; i < triangles.length; i++) {
			int offset = (triangles[i].length - getRowSize(i)) / 2;
			for (int j = 0; j < triangles[i].length; j++) {
				if (j >= offset && j < triangles[i].length - offset)
					triangles[i][j] = new Triangle(i, j, TriangleColor.BLANK);
				// Triangles out of the bounds of the row stay null
				// The fact that the diamond shape is placed in a grid makes calculations much easier
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
			for (int j = 0; j < triangles[i].length; j++) {
				
				if (cells[i].length > j && cells[i][j].length() > 0) {
					int color = Integer.parseInt(cells[i][j]);
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
	}
	
	public void render(float delta, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, BitmapFont font, GlyphLayout glyphLayout,
			int verticalMargin, int triangleWidth, int triangleHeight) {
		
		for (Triangle[] row : triangles) {
			for (Triangle t : row) {
				if (t != null)
					t.render(delta, shapeRenderer, verticalMargin, triangleWidth, triangleHeight);
			}
		}
	}
	
	/**
	 * @param rowIndex 0-indexed
	 * @return the number of triangles on that row, in the range [1,NB_COLUMNS]
	 */
	public static int getRowSize(int rowIndex) {
		return Math.min(rowIndex + 1, NB_ROWS - rowIndex) * 2 - 1;
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

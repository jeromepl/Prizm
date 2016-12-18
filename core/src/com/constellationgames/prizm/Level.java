package com.constellationgames.prizm;

import java.util.ArrayList;
import java.util.Stack;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.constellationgames.prizm.screens.GameScreen;
import com.constellationgames.prizm.utils.LevelLoader;
import com.constellationgames.prizm.utils.TriangleColor;

public class Level {
	
	public static final int NB_ROWS = 8;
	public static final int NB_COLUMNS = NB_ROWS - 1; // The max number of triangles on a row
	
	private GameScreen gameScreen;
	private int levelNumber;
	
	private int moveCount = -1; // Start the move count at -1 since it will be incremented to 1 at the first applyChanges() in the constructor
	private int points = 0; //TODO
	
	private Triangle[][] triangles = new Triangle[NB_ROWS][NB_COLUMNS];
	
	private Stack<LevelState> states = new Stack<LevelState>();

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
		
		// Set the initial state
		loadLevel(levelNumber);
		applyChanges();
	}
	
	private void loadLevel(int levelNumber) {
		
		//TODO there is no error handling here. A small mistake in the .csv file and everything goes boom
		
		FileHandle levelFile = LevelLoader.getLevelFile(levelNumber);
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
	
	public void render(float delta, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, BitmapFont font, GlyphLayout glyphLayout) {
		
		for (Triangle[] row : triangles) {
			for (Triangle t : row) {
				if (t != null)
					t.render(delta, shapeRenderer);
			}
		}
	}
	
	
	/**
	 * check for color canceling around a given triangle
	 * @param t
	 * @return the number of collisions found
	 */
	public int checkCollisions(Triangle t) {
		TriangleColor color = t.getColor();
		ArrayList<Triangle> neighbors = new ArrayList<Triangle>();
		int nbCollisions = 0;
		
		if (t.isOvert()) // Check triangle below
			neighbors.add(triangles[t.getRow() + 1][t.getColumn()]);
		else // Check triangle above
			neighbors.add(triangles[t.getRow() - 1][t.getColumn()]);
		
		
		// Check the triangles on both sides
		if (t.getColumn() > 0)
			neighbors.add(triangles[t.getRow()][t.getColumn() - 1]);
		if (t.getColumn() < NB_COLUMNS - 1)
			neighbors.add(triangles[t.getRow()][t.getColumn() + 1]);
		
		for (Triangle toCheck: neighbors) {
			if (toCheck != null && toCheck.getColor().getValue() + color.getValue() == TriangleColor.COMPLEMENTARY.getValue()) {
				toCheck.setColor(TriangleColor.GREY);
				t.setColor(TriangleColor.GREY);
				nbCollisions++;
			}
		}
		
		return nbCollisions;	
	}
	
	/**
	 * Check for big triangles formed with small grey triangles and remove them.
	 * 
	 * The idea here is to check for overt triangles first, by doing a top->bottom sweep,
	 * and then checking for invert triangles with a bottom->top sweep.
	 * 
	 * For the top->bottom, here are the possible locations where big triangles can occur (marked by 'x'):
	 *   0 1 2 3 4 5 6
	 * 0       x
	 * 1     x o x
	 * 2   x o x o x
	 * 3 o o x o x o o
	 * 4 o o o x o o o
	 * 5   o o o o o
	 * 6     o o o
	 * 7       o
	 * 
	 * For the bottom->top, flip this image.
	 */
	public void removeGreyTriangles() {
		ArrayList<Triangle> toRemove = new ArrayList<Triangle>();
		
		// Do both the top->bottom and bottom-top sweeps at the same time:
		for (int i = 0; i < 5; i++) { // Go through the first 5 rows
			int rowSize = getRowSize(i);
			int offset = (triangles[i].length - rowSize) / 2;
			int skip = (i > 2 ? i - 1 : 0); // Additional offset because on row 3, 4 we don't check all spots
			
			for (int j = offset + skip; j < rowSize + offset - skip; j += 2) {
				// Check for smallest triangles first (made of 4 triangles) then move on to bigger ones if the small one exists
				// Check for grey triangles below and in the bottom diagonals
				
				// top->bottom sweep
				Triangle toCheck = triangles[i][j];
				if (toCheck.getColor() == TriangleColor.GREY) {
					ArrayList<Triangle> greyTriangles = getGreyTriangles(toCheck, true);
					
					if (greyTriangles.size() > 1) // If there is only 1 triangle returned then it is not a large enough triangle to be removed
						toRemove.addAll(greyTriangles);
				}
				
				// bottom->top sweep
				toCheck = triangles[triangles.length - 1 - i][j];
				if (toCheck.getColor() == TriangleColor.GREY) {
					ArrayList<Triangle> greyTriangles = getGreyTriangles(toCheck, false);
					
					if (greyTriangles.size() > 1) // If there is only 1 triangle returned then it is not a large enough triangle to be removed
						toRemove.addAll(greyTriangles);
				}
				
			}
		}
		
		// Make sure to remove triangles only at the very end since all possible grey triangles need to be removed at once
		for (Triangle t: toRemove) {
			t.setColor(TriangleColor.BLANK);
			
			// TODO create a point system. Check if t.getColor != TriangleColor.BLANK already and if so add points
		}
	}
	
	/**
	 * Check if the 3 triangles below or above the given triangle are grey, recursively.
	 * This process finds the biggest possible grey triangle.
	 * Note that some triangles will be checked twice using this method, but it does not really matter as
	 * they will all be removed in the end.
	 * @param t the starting triangle
	 * @param downwards Whether to sweep upwards or downwards
	 * @return All small grey triangles that are part of bigger triangles (includes duplicates)
	 */
	private ArrayList<Triangle> getGreyTriangles(Triangle t, boolean downwards) {
		Triangle t1 = null, t2 = null, t3 = null;
		
		ArrayList<Triangle> greyTriangles = new ArrayList<Triangle>();
		greyTriangles.add(t);
		
		if (downwards) { // Check the bottom 3 triangles
			int rowSize = getRowSize(t.getRow() + 1);
			int offset = (NB_COLUMNS - rowSize) / 2;
			if (t.getRow() < NB_ROWS - 1 && t.getColumn() > offset && t.getColumn() < NB_COLUMNS - 1 - offset) {
				t1 = triangles[t.getRow() + 1][t.getColumn() - 1];
				t2 = triangles[t.getRow() + 1][t.getColumn()];
				t3 = triangles[t.getRow() + 1][t.getColumn() + 1];
			}
		}
		else {
			int rowSize = getRowSize(t.getRow() - 1);
			int offset = (NB_COLUMNS - rowSize) / 2;
			if (t.getRow() > 0 && t.getColumn() > offset && t.getColumn() < NB_COLUMNS - 1 - offset) {
				t1 = triangles[t.getRow() - 1][t.getColumn() - 1];
				t2 = triangles[t.getRow() - 1][t.getColumn()];
				t3 = triangles[t.getRow() - 1][t.getColumn() + 1];
			}
		}
		
		if (t1 != null && t2 != null && t3 != null
				&& t1.getColor() == TriangleColor.GREY && t2.getColor() == TriangleColor.GREY && t3.getColor() == TriangleColor.GREY) {
			
			greyTriangles.addAll(getGreyTriangles(t1, downwards));
			// The middle triangle does not need to be checked since the side ones will already take care of the full next layer
			// Also, the middle triangle will have a different orientation than the other triangles, which helps to get some intuition about this
			greyTriangles.add(t2);
			greyTriangles.addAll(getGreyTriangles(t3, downwards));
		}
		
		return greyTriangles;
	}
	
	/**
	 * @return whether the player has cleared the board and won
	 */
	public boolean hasWon() {
		for (Triangle[] row : triangles) {
			for (Triangle t : row) {
				if (t != null && t.getColor() != TriangleColor.BLANK)
					return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Create a new State of the board.
	 * This will thus create a point that players can go back to using the "Undo" command
	 */
	public void applyChanges() {
		states.push(new LevelState(triangles, points));
		moveCount++;
	}
	
	/**
	 * Set the board back to the last state (the last applyChanges() call)
	 */
	public void undo() {
		if (states.size() > 1) {
			states.pop();
			triangles = states.peek().getTrianglesCopy();
			points = states.peek().getPoints();
			moveCount--;
		}
	}
	
	/**
	 * @param rowIndex 0-indexed
	 * @return the number of triangles on that row, in the range [1,NB_COLUMNS]
	 */
	public static int getRowSize(int rowIndex) {
		return Math.min(rowIndex + 1, NB_ROWS - rowIndex) * 2 - 1;
	}
	
	/**
	 * @return an array containing the number of triangles of each color and each orientation
	 */
	public int[][] getTriangleStats() {
		int[][] count = new int[3][2];
		
		for (Triangle[] row : triangles) {
			for (Triangle t : row) {
				if (t != null) {			
					int orientationIndex = t.isOvert() ? 0 : 1;
					
					switch(t.getColor()) {
					case YELLOW:
						count[0][orientationIndex]++;
						break;
					case BLUE:
						count[1][orientationIndex]++;
						break;
					case RED:
						count[2][orientationIndex]++;
						break;
					case GREEN:
						count[0][orientationIndex]++;
						count[1][orientationIndex]++;
						break;
					case PURPLE:
						count[1][orientationIndex]++;
						count[2][orientationIndex]++;
						break;
					case ORANGE:
						count[0][orientationIndex]++;
						count[2][orientationIndex]++;
						break;
					default:
						continue;
					}
				}
			}
		}
		
		return count;
	}
	
	public int getLevelNumber() {
		return levelNumber;
	}
	
	public int getMoveCount() {
		return moveCount;
	}
	
	public int getPoints() {
		return points;
	}
	
	public Triangle[][] getTriangles() {
		return triangles;
	}
}

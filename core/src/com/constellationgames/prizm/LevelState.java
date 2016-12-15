package com.constellationgames.prizm;

/**
 * This class is used to store the state of the game board periodically.
 * This allows players to "undo" moves (going back in the states Stack)
 */
public class LevelState {
	
	private Triangle[][] triangles;
	private int points;
	
	/**
	 * Create a new board state. This method will automatically create a copy of the triangles
	 * @param triangles
	 * @param points
	 */
	public LevelState(Triangle[][] triangles, int points) {
		this.triangles = trianglesCopy(triangles);
		this.points = points;
	}
	
	public Triangle[][] getTriangles() {
		return triangles;
	}
	
	public Triangle[][] getTrianglesCopy() {
		return trianglesCopy(triangles);
	}
	
	public int getPoints() {
		return points;
	}
	
	/**
	 * Create a copy of a 2D array of triangles (used to copy and save the board state)
	 * @param a the array to copy
	 * @return the copy of the array
	 */
	private static Triangle[][] trianglesCopy(Triangle[][] a) {
		Triangle[][] copy = new Triangle[a.length][];
		for(int i = 0; i < a.length; i++) {
			Triangle[] rowCopy = new Triangle[a[i].length];
			for (int j = 0; j < rowCopy.length; j++) {
				if (a[i][j] != null)
					rowCopy[j] = a[i][j].copy();
			}
			copy[i] = rowCopy;
		}
		
		return copy;
	}

}

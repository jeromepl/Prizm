package com.constellationgames.prizm;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.constellationgames.prizm.screens.GameScreen;
import com.constellationgames.prizm.utils.TriangleColor;

public class Triangle {

	private static final int WIDTH = (Prizm.STANDARD_WIDTH - GameScreen.MARGIN * 2) / 4;
	private static final int HEIGHT = (int) Math.round(WIDTH / 2.0 * Math.sqrt(3)); // Formula to find height of equilateral triangle
	
	private int row, column;
	private TriangleColor color;

	/**
	 * Instantiate a triangle
	 * @param row 0-indexed row value of the triangle's position
	 * @param column Index of the column the triangle is in. NOTE: column index starts at 0 from the left on every row
	 * @param color a color value as taken from the Colors enum in the 'utils' package
	 */
	public Triangle(int row, int column, TriangleColor color) {
		this.row = row;
		this.column = column;
		this.color = color;
	}
	
	public void render(float delta, ShapeRenderer shapeRenderer) {
		
		float[][] pos = getScreenPosition();
		
		// Flip y values since libGDX's coordinate system is different in render than in input processing...
		pos[0][1] = Prizm.STANDARD_HEIGHT - pos[0][1];
		pos[1][1] = Prizm.STANDARD_HEIGHT - pos[1][1];
		pos[2][1] = Prizm.STANDARD_HEIGHT - pos[2][1];
		
		// Render the interior
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(color.getColor());
		shapeRenderer.triangle(pos[0][0], pos[0][1], pos[1][0], pos[1][1], pos[2][0], pos[2][1]);
		
		shapeRenderer.end();
		
		// Render the border
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.triangle(pos[0][0], pos[0][1], pos[1][0], pos[1][1], pos[2][0], pos[2][1]);
		
		shapeRenderer.end();
	}
	
	public float[][] getScreenPosition() {	
		float[][] pos = new float[3][2]; // Triangle coordinates
		if (isOvert()) {
			pos[0][0] = GameScreen.MARGIN + WIDTH * column / 2;
			pos[0][1] = GameScreen.MARGIN + HEIGHT * (row + 1);
			pos[1][0] = pos[0][0] + WIDTH;
			pos[1][1] = pos[0][1];
			pos[2][0] = (pos[0][0] + pos[1][0]) / 2;
			pos[2][1] = pos[0][1] - HEIGHT;
		}
		else {
			pos[0][0] = GameScreen.MARGIN + WIDTH * (column - 1) / 2 + WIDTH / 2;
			pos[0][1] = GameScreen.MARGIN + HEIGHT * row;
			pos[1][0] = pos[0][0] + WIDTH;
			pos[1][1] = pos[0][1];
			pos[2][0] = (pos[0][0] + pos[1][0]) / 2;
			pos[2][1] = pos[0][1] + HEIGHT;
		}
		
		return pos;
	}
	
	/**
	 * Check if the triangle contains a point (x, y)
	 * @return whether the point lies inside the triangle
	 */
	public boolean contains(int x, int y) {
		float[][] pos = getScreenPosition();
		
		// See http://mathworld.wolfram.com/TriangleInterior.html for mathematical explanation
		float v0_x = pos[0][0];
		float v0_y = pos[0][1];
		float v1_x = pos[1][0] - v0_x;
		float v1_y = pos[1][1] - v0_y;
		float v2_x = pos[2][0] - v0_x;
		float v2_y = pos[2][1] - v0_y;
		
		float denominator = determinant(v1_x, v1_y, v2_x, v2_y);
		
		float a = (determinant(x, y, v2_x, v2_y) - determinant(v0_x, v0_y, v2_x, v2_y)) / denominator;
		float b = - (determinant(x, y, v1_x, v1_y) - determinant(v0_x, v0_y, v1_x, v1_y)) / denominator;
		
		if (a > 0 && b > 0 && a + b < 1)
			return true;
		else
			return false;
	}
	
	private float determinant(float u_x, float u_y, float v_x, float v_y) {
		return u_x * v_y - u_y * v_x;
	}
	
	public void setColor(TriangleColor color) {
		this.color = color;
	}
	
	public TriangleColor getColor() {
		return color;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
	
	/**
	 * Check if the triangle points upwards or downwards
	 * @return whether the triangle is 'overt' or 'invert'
	 */
	public boolean isOvert() {
		// Even columns indices are overts in the upper half of the board
		return (row + column) % 2 != 0;
	}
}

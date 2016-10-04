package com.constellationgames.prizm;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.constellationgames.prizm.screens.GameScreen;
import com.constellationgames.prizm.utils.TriangleColor;

public class Triangle {
	
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
	
	public void render(float delta, ShapeRenderer shapeRenderer, int verticalMargin, int triangleWidth, int triangleHeight) {
		
		// Each row needs to be centered:
		int nbInRow = Math.min(row + 1, 8 - row) * 2 - 1;
		int horizontalOffset = (int) (triangleWidth * (7 - nbInRow) / 4.0); // There are a max of 7 triangles per row
		
		float x1, y1, x2, y2, x3, y3; // Triangle coordinates
		if (isOvert()) {
			x1 = horizontalOffset + GameScreen.MARGIN + triangleWidth * column / 2;
			y1 = verticalMargin + triangleHeight * (row + 1);
			x2 = x1 + triangleWidth;
			y2 = y1;
			x3 = (x1 + x2) / 2;
			y3 = y1 - triangleHeight;
		}
		else {
			x1 = horizontalOffset + GameScreen.MARGIN + triangleWidth * (column - 1) / 2 + triangleWidth / 2;
			y1 = verticalMargin + triangleHeight * row;
			x2 = x1 + triangleWidth;
			y2 = y1;
			x3 = (x1 + x2) / 2;
			y3 = y1 + triangleHeight;
		}
		
		
		// Render the interior
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(color.getColor());
		shapeRenderer.triangle(x1, y1, x2, y2, x3, y3);
		
		shapeRenderer.end();
		
		// Render the border
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.triangle(x1, y1, x2, y2, x3, y3);
		
		shapeRenderer.end();
	}
	
	public void setColor(TriangleColor color) {
		this.color = color;
	}
	
	public TriangleColor getColor() {
		return color;
	}
	
	/**
	 * Check if the triangle points upwards or downwards
	 * @return whether the triangle is 'overt' or 'invert'
	 */
	public boolean isOvert() {
		// Even columns indices are overts in the upper half of the board
		return (column % 2 == 0 && row < 4 || column % 2 != 0 && row >= 4);
	}
}

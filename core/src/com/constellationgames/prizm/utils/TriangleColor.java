package com.constellationgames.prizm.utils;

import com.badlogic.gdx.graphics.Color;

public enum TriangleColor {
	
	BLANK(0, Color.WHITE),
	
	YELLOW(2, Color.YELLOW),
	BLUE(3, Color.BLUE),
	RED(4, Color.RED),
	GREEN(5, Color.GREEN),
	ORANGE(6, Color.ORANGE),
	PURPLE(7, Color.PURPLE),
	  
	// Use an unused value for GREY
	GREY(100, Color.GRAY),
	  
	// Notice that the addition of complementary colors always gives 9
	// Use this value to check when 2 colors cancel out
	COMPLEMENTARY(9, null);
	
	private final int value;
	private final Color color; // The libGdx Color value
	
	private TriangleColor(int value, Color color) {
		this.value = value;
		this.color = color;
	}
	
	public int getValue() {
		return value;
	}
	
	public Color getColor() {
		return color;
	}
	
	/**
	 * Adds two primary colors and returns the resulting secondary color.
	 * Returns null in the case that no color could be formed from the given 2.
	 * @param color1
	 * @param color2
	 * @return
	 */
	public static TriangleColor addColors(TriangleColor color1, TriangleColor color2) {
		TriangleColor[] colors = TriangleColor.values();
		int sum = color1.getValue() + color2.getValue();
		
		for (TriangleColor c : colors) {
			if (sum == c.getValue())
				return c;
		}
		return null;
	}
}

package com.constellationgames.prizm;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.constellationgames.prizm.screens.GameScreen;

public class Level {
	
	private GameScreen gameScreen;
	private int levelNumber;
	
	private Color textColor = Color.BLACK;

	public Level(GameScreen gameScreen, int levelNumber) {
		this.gameScreen = gameScreen;
		this.levelNumber = levelNumber;
	}
	
	public void render(float delta, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, BitmapFont font, GlyphLayout glyphLayout) {
		
	}
	
	// Free memory when exiting the game
	public void dispose() {
		
	}
	
	public int getLevelNumber() {
		return levelNumber;
	}
}

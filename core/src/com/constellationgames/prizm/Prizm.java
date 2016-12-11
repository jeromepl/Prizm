package com.constellationgames.prizm;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.constellationgames.prizm.screens.MenuScreen;

public class Prizm extends Game {
	
	public static final int STANDARD_WIDTH = 576;
	public static final int STANDARD_HEIGHT = 924;
	
	@Override
	public void create() {
		setScreen(new MenuScreen(this));
		
		// Render only on input events
		Gdx.graphics.setContinuousRendering(false);
		Gdx.graphics.requestRendering(); // Render once
		
		// Android: Handle back button presses
		Gdx.input.setCatchBackKey(true);
	}
	
}

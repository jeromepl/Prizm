package com.constellationgames.prizm.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class LevelSelectScreen implements Screen {
	
	private static final int STANDARD_WIDTH = 576; // Used to scale everything
	private static final float STANDARD_BUTTON_SCALE = 0.13f;
	
	private Game game;
	private Skin skin;
	private Stage stage = new Stage();
	
	private TextButton button;
	
	public LevelSelectScreen(Game game, Skin skin) {
		this.game = game;
		this.skin = skin;
		
		button = new TextButton("Button", skin);
	}

	@Override
	public void show() {
		float xScale = Gdx.graphics.getWidth() * 1.0f / STANDARD_WIDTH;
		
		button.getStyle().font.getData().setScale(STANDARD_BUTTON_SCALE * xScale);
		
		stage.addActor(button);
		
		Gdx.input.setInputProcessor(stage);
		
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new GameScreen(game, skin, 1)); //TODO
			}
		});
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.85f, 0.85f, 0.85f, 1); //Light grey background
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        stage.act();
        stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}

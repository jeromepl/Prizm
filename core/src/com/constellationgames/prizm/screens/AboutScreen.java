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

public class AboutScreen implements Screen {
	
	private static final int STANDARD_WIDTH = 576; // Used to scale everything
	private static final float STANDARD_BUTTON_SCALE = 0.13f;
	
	private Game game;
	private Skin skin;
	private Stage stage = new Stage();
	
	private TextButton backButton;
	
	public AboutScreen(Game game, Skin skin) {
		this.game = game;
		this.skin = skin;
		
		backButton = new TextButton("Back", skin);
	}

	@Override
	public void show() {
		float xScale = Gdx.graphics.getWidth() * 1.0f / STANDARD_WIDTH;
		
		backButton.getStyle().font.getData().setScale(STANDARD_BUTTON_SCALE * xScale);
		
		stage.addActor(backButton);
		
		Gdx.input.setInputProcessor(stage);
		
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new MenuScreen(game));
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

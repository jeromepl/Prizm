package com.constellationgames.prizm.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.constellationgames.prizm.Prizm;
import com.constellationgames.prizm.utils.CustomTextButton;

public class AboutScreen implements Screen {
	
	private static final float STANDARD_BUTTON_SCALE = 0.13f;
	private static final int STANDARD_BUTTON_WIDTH = 350;
	private static final int STANDARD_BUTTON_HEIGHT = 70;
	
	private Skin skin;
	private Stage stage = new Stage(new ExtendViewport(Prizm.STANDARD_WIDTH, Prizm.STANDARD_HEIGHT));
	private Table table = new Table();
	
	private TextButton backButton;
	
	public AboutScreen(final Game game, final Skin skin) {
		this.skin = skin;
		
		backButton = new CustomTextButton("Back", skin);
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new MenuScreen(game));
			}
		});
	}

	@Override
	public void show() {
		backButton.setSize(STANDARD_BUTTON_WIDTH, STANDARD_BUTTON_HEIGHT);
		backButton.getStyle().font.getData().setScale(STANDARD_BUTTON_SCALE);
		
		stage.addActor(backButton);
		
		Gdx.input.setInputProcessor(stage);
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
		stage.getViewport().update(width, height, true);
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

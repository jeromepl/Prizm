package com.constellationgames.prizm.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MenuScreen implements Screen {
	
	private static final int STANDARD_WIDTH = 576; // Used to scale everything
	private static final int STANDARD_HEIGHT = 924;
	private static final int STANDARD_BUTTON_PADDING = 15;
	private static final int STANDARD_TITLE_PADDING = 80;
	private static final int STANDARD_BUTTON_WIDTH = 350;
	private static final int STANDARD_BUTTON_HEIGHT = 70;
	private static final float STANDARD_TITLE_SCALE = 0.5f;
	private static final float STANDARD_BUTTON_SCALE = 0.13f;
	
	private Stage stage = new Stage();
	private Table table = new Table();
	
	private Game game;
	
	private Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
	private TextButton play = new TextButton("Play", skin);
	private TextButton about = new TextButton("About", skin);
	private Label title = new Label("PRIZM", skin);
	
	public MenuScreen(Game game) {
		this.game = game;
	}

	@Override
	public void show() {
		
		// Set up the menu elements
		
		float xScale = Gdx.graphics.getWidth() * 1.0f / STANDARD_WIDTH;
		float yScale = Gdx.graphics.getHeight() * 1.0f / STANDARD_HEIGHT;
		
		title.setFontScale(STANDARD_TITLE_SCALE * xScale);
		play.getStyle().font.getData().setScale(STANDARD_BUTTON_SCALE * xScale);
		about.getStyle().font.getData().setScale(STANDARD_BUTTON_SCALE * xScale);
		
		table.add(title).pad(STANDARD_TITLE_PADDING * yScale).row();
		table.add(play).size(STANDARD_BUTTON_WIDTH * xScale, STANDARD_BUTTON_HEIGHT * yScale).pad(STANDARD_BUTTON_PADDING * yScale).row();
		table.add(about).size(STANDARD_BUTTON_WIDTH * xScale, STANDARD_BUTTON_HEIGHT * yScale).pad(STANDARD_BUTTON_PADDING * yScale).row();
		
		table.setFillParent(true);
		stage.addActor(table);
		
		Gdx.input.setInputProcessor(stage);
		
		play.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new LevelSelectScreen(game, skin));
			}
		});
		about.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new AboutScreen(game, skin));
			}
		});
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.85f, 0.85f, 0.85f, 1); // Light grey background
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        stage.act();
        stage.draw();
	}
	
	public Game getGame() {
		return game;
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
	}

}

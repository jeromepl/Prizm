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
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.constellationgames.prizm.Prizm;
import com.constellationgames.prizm.utils.CustomTextButton;

public class MenuScreen implements Screen {
	
	private static final int STANDARD_BUTTON_PADDING = 15;
	private static final int STANDARD_TITLE_PADDING = 80;
	private static final int STANDARD_BUTTON_WIDTH = 350;
	private static final int STANDARD_BUTTON_HEIGHT = 70;
	private static final float STANDARD_TITLE_SCALE = 0.5f;
	private static final float STANDARD_BUTTON_SCALE = 0.13f;
	
	private Stage stage = new Stage(new ExtendViewport(Prizm.STANDARD_WIDTH, Prizm.STANDARD_HEIGHT));
	private Table table = new Table();
	
	private Game game;
	
	private static Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
	private TextButton play = new CustomTextButton("Play", skin);
	private TextButton about = new CustomTextButton("About", skin);
	private Label title = new Label("PRIZM", skin);
	
	public MenuScreen(Game game) {
		this.game = game;
		
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void show() {
		
		// Set up the menu elements
		title.setFontScale(STANDARD_TITLE_SCALE);
		play.getStyle().font.getData().setScale(STANDARD_BUTTON_SCALE);
		about.getStyle().font.getData().setScale(STANDARD_BUTTON_SCALE);
		
		table.add(title).pad(STANDARD_TITLE_PADDING).row();
		table.add(play).size(STANDARD_BUTTON_WIDTH, STANDARD_BUTTON_HEIGHT).pad(STANDARD_BUTTON_PADDING).row();
		table.add(about).size(STANDARD_BUTTON_WIDTH, STANDARD_BUTTON_HEIGHT).pad(STANDARD_BUTTON_PADDING).row();
		
		table.setFillParent(true);
		stage.addActor(table);
		
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

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
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
		skin.dispose();
	}

}

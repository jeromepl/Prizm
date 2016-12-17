package com.constellationgames.prizm.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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

public class AboutScreen implements Screen {
	
	private static final float STANDARD_TITLE_SCALE = 0.5f;
	private static final float STANDARD_SUBTITLE_SCALE = 0.3f;
	private static final float STANDARD_TEXT_SCALE = 0.13f;
	private static final int STANDARD_BUTTON_WIDTH = 350;
	private static final int STANDARD_BUTTON_HEIGHT = 70;
	private static final int STANDARD_TEXT_PADDING = 25;
	private static final int STANDARD_TITLE_PADDING = 15;
	private static final int STANDARD_BUTTON_PADDING = 60;
	
	private Game game;
	private Skin skin;
	
	private Stage stage = new Stage(new ExtendViewport(Prizm.STANDARD_WIDTH, Prizm.STANDARD_HEIGHT));
	private Table table = new Table();
	private TextButton backButton;
	private Label title, subtitle, copyright, text;
	
	public AboutScreen(Game game, Skin skin) {
		this.game = game;
		this.skin = skin;
		
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void show() {
		
		title = new Label("RJH Games", skin);
		subtitle = new Label("(les jeux RJH)", skin);
		copyright = new Label("Copyright (c) 2006-2016", skin);
		text = new Label("Prizm is part of the \"Constellation Games\" series, created and developped by RJH Games.", skin);
		backButton = new CustomTextButton("Back", skin);
		
		// Set up the menu elements
		title.setFontScale(STANDARD_TITLE_SCALE);
		subtitle.setFontScale(STANDARD_SUBTITLE_SCALE);
		copyright.getStyle().font.getData().setScale(STANDARD_TEXT_SCALE);
		text.getStyle().font.getData().setScale(STANDARD_TEXT_SCALE);
		backButton.getStyle().font.getData().setScale(STANDARD_TEXT_SCALE);
		
		copyright.setColor(Color.BLACK);
		text.setColor(Color.BLACK);
		
		// The text is too long to fit on one line and needs to be wrapped
		int textWidth = Prizm.STANDARD_WIDTH - STANDARD_TEXT_PADDING * 2;
		text.setWrap(true);
		text.setWidth(textWidth);
		
		table.add(title).pad(STANDARD_TITLE_PADDING).row();
		table.add(subtitle).pad(STANDARD_TEXT_PADDING).row();
		table.add(copyright).pad(STANDARD_TEXT_PADDING).row();
		table.add(text).width(textWidth).row();
		table.add(backButton).size(STANDARD_BUTTON_WIDTH, STANDARD_BUTTON_HEIGHT).pad(STANDARD_BUTTON_PADDING).row();
		
		table.setFillParent(true);
		stage.addActor(table);
		
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

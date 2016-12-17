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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.constellationgames.prizm.Prizm;
import com.constellationgames.prizm.utils.CustomTextButton;
import com.constellationgames.prizm.utils.LevelLoader;

public class LevelSelectScreen implements Screen {

	private static final float STANDARD_BUTTON_SCALE = 0.13f;
	private static final int STANDARD_BUTTON_WIDTH = 100;
	private static final int STANDARD_BUTTON_HEIGHT = 70;
	private static final int STANDARD_BUTTON_MARGIN = 10;
	private static final float STANDARD_TITLE_SCALE = 0.4f;
	
	private Stage stage = new Stage(new ExtendViewport(Prizm.STANDARD_WIDTH, Prizm.STANDARD_HEIGHT));
	
	private TextButton[] levelButtons;
	private TextButton backButton;
	private Label screenTitle;
	
	public LevelSelectScreen(final Game game, final Skin skin) {
		screenTitle = new Label("Select Level", skin);
		
		backButton = new CustomTextButton("Back", skin);
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new MenuScreen(game));
			}
		});
		
		// TODO Find all level files
		levelButtons = new TextButton[LevelLoader.getNumberOfLevels()];
		for (int i = 0; i < levelButtons.length; i++) {
			final int levelNumber = i + 1;
			levelButtons[i] = new CustomTextButton("Level " + levelNumber, skin);
			levelButtons[i].addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					game.setScreen(new GameScreen(game, skin, levelNumber));
				}
			});
		}
	}

	@Override
	public void show() {
		Table table = new Table();
		
		int buttonsPerRow = ((int)(Prizm.STANDARD_WIDTH - 2 * STANDARD_BUTTON_MARGIN)) / (STANDARD_BUTTON_WIDTH + STANDARD_BUTTON_MARGIN); // Changing STANDARD_BUTTON_WIDTH to Gdx.graphics.getHeight() will make the row of levels fill the width of the screen
		
		backButton.setSize(STANDARD_BUTTON_WIDTH, STANDARD_BUTTON_HEIGHT * 3f/4);
		backButton.getStyle().font.getData().setScale(STANDARD_BUTTON_SCALE);
		
		screenTitle.setFontScale(STANDARD_TITLE_SCALE);
		table.add(screenTitle).colspan(buttonsPerRow).spaceBottom(4 * STANDARD_BUTTON_MARGIN).center();
		
		for (int i = 0; i < levelButtons.length; i++) {
			if (i % buttonsPerRow == 0)
				table.row();
			
			levelButtons[i].setSize(STANDARD_BUTTON_WIDTH, STANDARD_BUTTON_HEIGHT);
			table.add(levelButtons[i]).space(STANDARD_BUTTON_MARGIN).size(levelButtons[i].getWidth(), levelButtons[i].getHeight());
		}
		table.row();
		table.add(backButton).colspan(buttonsPerRow).center().space(4 * STANDARD_BUTTON_MARGIN).size(backButton.getWidth(), backButton.getHeight());
		
		table.align(Align.center);
		table.setFillParent(true);
		stage.addActor(table);
		
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

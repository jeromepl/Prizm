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

public class WinScreen implements Screen {

	private static final int STANDARD_TITLE_MARGIN = 20;
	private static final int STANDARD_BUTTON_WIDTH = 275;
	private static final int STANDARD_BUTTON_HEIGHT = 70;
	private static final int STANDARD_BUTTON_MARGIN = 7;
	private static final float STANDARD_TITLE_SCALE = 0.4f;
	
	private Stage stage = new Stage(new ExtendViewport(Prizm.STANDARD_WIDTH, Prizm.STANDARD_HEIGHT));
	
	private TextButton nextLevel;
	private TextButton selectLevel;
	private TextButton mainMenu;
	private Label screenTitle;
	private Label nbMovesLabel;
	private Label pointsLabel;
	
	public WinScreen(final Game game, final Skin skin, final int level, int nbMoves, int points) {
		screenTitle = new Label("You Won!", skin);
		nbMovesLabel = new Label(points + " points", skin);
		pointsLabel = new Label(nbMoves + " moves", skin);
		
		// TODO check if level is not == final level (in which case disable the button?)
		nextLevel = new CustomTextButton("Next Level", skin);
		nextLevel.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new GameScreen(game, skin, level + 1));
			}
		});
		
		selectLevel = new CustomTextButton("Select Level", skin);
		selectLevel.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new LevelSelectScreen(game, skin));
			}
		});
		
		mainMenu = new CustomTextButton("Main Menu", skin);
		mainMenu.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new MenuScreen(game));
			}
		});
	}

	@Override
	public void show() {
		Table table = new Table();
		
		nextLevel.setSize(STANDARD_BUTTON_WIDTH, STANDARD_BUTTON_HEIGHT);
		selectLevel.setSize(STANDARD_BUTTON_WIDTH, STANDARD_BUTTON_HEIGHT);
		mainMenu.setSize(STANDARD_BUTTON_WIDTH, STANDARD_BUTTON_HEIGHT);
		//backButton.getStyle().font.getData().setScale(STANDARD_BUTTON_SCALE);
		
		screenTitle.setFontScale(STANDARD_TITLE_SCALE);
		table.add(screenTitle).center().spaceBottom(STANDARD_TITLE_MARGIN).row();
		table.add(nbMovesLabel).center().row();
		table.add(pointsLabel).center().row();

		table.add(nextLevel).center().space(4 * STANDARD_BUTTON_MARGIN).size(nextLevel.getWidth(), nextLevel.getHeight()).row();
		table.add(selectLevel).center().space(4 * STANDARD_BUTTON_MARGIN).size(selectLevel.getWidth(), selectLevel.getHeight()).row();
		table.add(mainMenu).center().space(4 * STANDARD_BUTTON_MARGIN).size(mainMenu.getWidth(), mainMenu.getHeight()).row();
		
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

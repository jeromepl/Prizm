package com.constellationgames.prizm.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.constellationgames.prizm.Level;
import com.constellationgames.prizm.Triangle;
import com.constellationgames.prizm.utils.TriangleColor;

public class GameScreen implements Screen, InputProcessor {
	
	// Screen values
	public static final int MARGIN = 40; // In px, margin between the grid and the borders of the screen
//	public static final int STANDARD_WIDTH = 576; // Used to scale the fonts
//	public static final int STANDARD_HEIGHT = 924;
	
	private Level level;
	private Game game;
	
	private Stage stage;
	private Skin skin;
	private TextButton backButton;
	
	private OrthographicCamera camera;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch spriteBatch;
	private GlyphLayout glyphLayout;
	private BitmapFont font;
	
	private int verticalMargin;
	private int triangleWidth;
	private int triangleHeight;
	
	// Variables for drag-and-dropping triangles
	private Triangle selectedTriangle = null;

	public GameScreen(Game game, Skin skin, int levelNumber) {
		this.game = game;
		this.skin = skin;
		level = new Level(this, levelNumber);
		
		Gdx.input.setInputProcessor(this);
		
		stage = new Stage();
		backButton = new TextButton("Back", skin);
	}
	
	@Override
	public void show() {
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		shapeRenderer = new ShapeRenderer();
		spriteBatch = new SpriteBatch();
		
		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // Initialize the size values
		
		// Create the font
		font = new BitmapFont(Gdx.files.internal("fonts/yaheiUI.fnt"));
		font.setColor(Color.BLACK);
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear); // Higher quality rendering for the font
		glyphLayout = new GlyphLayout();
		
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new MenuScreen(game));
			}
		});
		
		stage.addActor(backButton);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.85f, 0.85f, 0.85f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		shapeRenderer.setProjectionMatrix(camera.combined);
		spriteBatch.setProjectionMatrix(camera.combined);
		
		level.render(delta, shapeRenderer, spriteBatch, font, glyphLayout,
				verticalMargin, triangleWidth, triangleHeight);
		
		stage.act();
        stage.draw();
	}
	
	@Override
	public void dispose() {
		level.dispose();
		shapeRenderer.dispose();
		spriteBatch.dispose();
		font.dispose();
	}
	
	@Override
	public void resize(int width, int height) {
		camera.setToOrtho(false, width, height);
		
		int boardWidth = width - 2 * MARGIN;
		triangleWidth = boardWidth / 4;
		triangleHeight = (int) Math.round(triangleWidth / 2.0 * Math.sqrt(3)); // Formula to find height of equilateral triangle
		verticalMargin = height - 8 * triangleHeight;
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
	public boolean keyDown(int keycode) {
		return stage.keyDown(keycode);
	}

	@Override
	public boolean keyUp(int keycode) {
		stage.keyUp(keycode);
		// Handle back button press to go back to menu
		if (keycode == Keys.BACK) {          
			game.setScreen(new MenuScreen(game));
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return stage.keyTyped(character);
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		stage.touchDown(screenX, screenY, pointer, button);
		
		Triangle[][] triangles = level.getTriangles();
		
		for (Triangle[] row : triangles) {
			for (Triangle t: row) {
				if (t != null && t.getColor() != TriangleColor.BLANK && t.getColor() != TriangleColor.GREY
						&& t.contains(screenX, screenY, verticalMargin, triangleWidth, triangleHeight)) {
					selectedTriangle = t;
					
					return true;
				}
			}
		}
		
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		stage.touchUp(screenX, screenY, pointer, button);
		
		if (selectedTriangle != null) {
			Triangle[][] triangles = level.getTriangles();
			
			outerloop:
			for (Triangle[] row : triangles) {
				for (Triangle t: row) {
					if (t != null && t != selectedTriangle && t.isOvert() == selectedTriangle.isOvert()
							&& t.contains(screenX, screenY, verticalMargin, triangleWidth, triangleHeight)) {
						
						TriangleColor colorFrom = selectedTriangle.getColor();
						TriangleColor colorTo = t.getColor();
						boolean destinationModified = false;
						
						if (colorTo == TriangleColor.BLANK) {
							t.setColor(colorFrom);
							selectedTriangle.setColor(TriangleColor.BLANK);
							
							destinationModified = true;
						}
						else if (colorFrom.getValue() <= TriangleColor.RED.getValue()
								&& selectedTriangle.getColor().getValue() <= TriangleColor.RED.getValue()
								&& colorFrom != colorTo) {
							
							// If the two colors are primary colors and are not the same, then the colors
							// combined to form a secondary color
							t.setColor(TriangleColor.addColors(colorFrom, colorTo));
							selectedTriangle.setColor(TriangleColor.BLANK);
							
							destinationModified = true;
						}
						
						if (destinationModified) {
							// Check if some colors cancel out
							level.checkCollisions(t);
							
							// After all collisions have been checked, remove Grey triangles
							level.removeGreyTriangles();
							
							break outerloop;
						}
						
					}
				}
			}
			
			selectedTriangle = null;
		}
		
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return stage.touchDragged(screenX, screenY, pointer);
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return stage.mouseMoved(screenX, screenY);
	}

	@Override
	public boolean scrolled(int amount) {
		return stage.scrolled(amount);
	}
}

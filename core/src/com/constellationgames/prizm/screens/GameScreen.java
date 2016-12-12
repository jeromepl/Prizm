package com.constellationgames.prizm.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.constellationgames.prizm.Level;
import com.constellationgames.prizm.Prizm;
import com.constellationgames.prizm.Triangle;
import com.constellationgames.prizm.utils.TriangleColor;

public class GameScreen implements Screen, InputProcessor {
	
	// Screen values
	public static final int MARGIN = 30; // In px, margin between the grid and the borders of the screen
	
	private Level level;
	private Game game;
	
	private Stage stage;
	private Skin skin;
	private ColorSelectionPopup popup;
	private TextButton backButton;
	
	private Viewport viewport;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch spriteBatch;
	private GlyphLayout glyphLayout;
	private BitmapFont font;
	
	// Variables for drag-and-dropping triangles
	private Triangle selectedTriangle = null;

	public GameScreen(Game game, Skin skin, int levelNumber) {
		this.game = game;
		this.skin = skin;
		level = new Level(this, levelNumber);
		
		Gdx.input.setInputProcessor(this);
	}
	
	@Override
	public void show() {
		Camera camera = new OrthographicCamera(Prizm.STANDARD_WIDTH, Prizm.STANDARD_HEIGHT);
		viewport = new ExtendViewport(Prizm.STANDARD_WIDTH, Prizm.STANDARD_HEIGHT, camera);
		
		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		shapeRenderer = new ShapeRenderer();
		spriteBatch = new SpriteBatch();
		
		stage = new Stage(viewport);
		backButton = new TextButton("Back", skin);
		popup = new ColorSelectionPopup(this, skin);
		
		// Create the font
		font = new BitmapFont(Gdx.files.internal("fonts/yaheiUI.fnt"));
		font.setColor(Color.BLACK);
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear); // Higher quality rendering for the font
		glyphLayout = new GlyphLayout();
		
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new LevelSelectScreen(game, skin));
			}
		});
		
		stage.addActor(backButton);
		stage.addActor(popup);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.85f, 0.85f, 0.85f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
		spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
		
		level.render(delta, shapeRenderer, spriteBatch, font, glyphLayout);
		
		stage.act(delta);
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
		viewport.update(width, height, false);
		
		// Work around bug in ExtendViewport. Centers the camera.
		// See http://badlogicgames.com/forum/viewtopic.php?f=11&t=14331
		viewport.getCamera().position.set(Prizm.STANDARD_WIDTH / 2, Prizm.STANDARD_HEIGHT / 2, 0);
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
		
		// Un-project the screen positions to get the in-game positions
		Vector3 coords = viewport.getCamera().unproject(new Vector3(screenX, Gdx.graphics.getHeight() - screenY, 0),
				viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
		screenX = (int) coords.x;
		screenY = (int) coords.y;
		
		if (!popup.isVisible()) {
			Triangle[][] triangles = level.getTriangles();
			
			for (Triangle[] row : triangles) {
				for (Triangle t: row) {
					if (t != null && t.getColor() != TriangleColor.BLANK && t.getColor() != TriangleColor.GREY && t.contains(screenX, screenY)) {
						selectedTriangle = t;
						
						return true;
					}
				}
			}
		}
		
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		stage.touchUp(screenX, screenY, pointer, button);
		
		// Un-project the screen positions to get the in-game positions
		Vector3 coords = viewport.getCamera().unproject(new Vector3(screenX, Gdx.graphics.getHeight() - screenY, 0),
				viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
		screenX = (int) coords.x;
		screenY = (int) coords.y;
		
		if (selectedTriangle != null) {
			Triangle[][] triangles = level.getTriangles();
			
			outerloop:
			for (Triangle[] row : triangles) {
				for (final Triangle t: row) {
					if (t != null && t != selectedTriangle && t.isOvert() == selectedTriangle.isOvert() && t.contains(screenX, screenY)) {
						
						TriangleColor colorFrom = selectedTriangle.getColor();
						TriangleColor colorTo = t.getColor();
						boolean destinationModified = false;
						
						if (colorTo == TriangleColor.BLANK) {
							if (colorFrom.getValue() <= TriangleColor.RED.getValue()) {
								t.setColor(colorFrom);
								selectedTriangle.setColor(TriangleColor.BLANK);
								destinationModified = true;
							}
							else {
								// destinationModified is not set to true. The update will happen when the pop-up terminates
								popup.show(screenX, Prizm.STANDARD_HEIGHT - screenY, selectedTriangle, t, colorFrom);
							
								break outerloop;
							}
						}
						else if (colorFrom.getValue() <= TriangleColor.RED.getValue()
								&& colorTo.getValue() <= TriangleColor.RED.getValue()
								&& colorFrom != colorTo) {
							
							// If the two colors are primary colors and are not the same, then the colors
							// combined to form a secondary color
							t.setColor(TriangleColor.addColors(colorFrom, colorTo));
							selectedTriangle.setColor(TriangleColor.BLANK);
							
							destinationModified = true;
						}
						else if (colorFrom.getValue() > TriangleColor.RED.getValue()
								&& colorTo.getValue() <= TriangleColor.RED.getValue()) {
							// destinationModified is not set to true. The update will happen when the pop-up terminates
							popup.show(screenX, Prizm.STANDARD_HEIGHT - screenY, selectedTriangle, t, colorFrom);
						
							break outerloop;
						}
						
						if (destinationModified) {
							updateLevel(t);
							break outerloop;
						}
						
					}
				}
			}
			
			selectedTriangle = null;
		}
		
		return true;
	}
	
	public void updateLevel(final Triangle... updatedTriangles) {
		Timer.schedule(new Task(){
		    @Override
		    public void run() {
		    	// Check if some colors cancel out
		    	for (Triangle t: updatedTriangles)
		    		level.checkCollisions(t);
		    	
				Timer.schedule(new Task(){
				    @Override
				    public void run() {
				    	// After all collisions have been checked, remove Grey triangles
						level.removeGreyTriangles();
						
						// Check if the player has won (cleared the board)
						if (level.hasWon()) {
							Timer.schedule(new Task() {
								@Override
								public void run() {
									// TODO add points and number of moves here
									game.setScreen(new WinScreen(game, skin, level.getLevelNumber(), 0, 0));
								}
							}, 0.5f);
						}
				    }
				}, 0.5f);
		    }
		}, 0.5f);
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

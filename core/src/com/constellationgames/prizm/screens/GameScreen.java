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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.constellationgames.prizm.DragOverlay;
import com.constellationgames.prizm.Level;
import com.constellationgames.prizm.Prizm;
import com.constellationgames.prizm.Triangle;
import com.constellationgames.prizm.utils.TriangleColor;

public class GameScreen implements Screen, InputProcessor {
	
	// Screen values
	public static final int MARGIN_TOP = 30; // In px, margin between the GRID and the borders of the screen
	public static final int MARGIN_BOTTOM = 60;
	private static final int BUTTON_MARGIN = 15;
	private static final int BUTTON_PADDING = 10;
	private static final int FOOTER_Y_POSITION = 30;
	
	private Level level;
	private Game game;
	
	private Stage stage;
	private Skin skin;
	private ColorSelectionPopup popup;
	private TextButton backButton, undoButton, resetButton;
	private Label moveCountLabel, pointsLabel;
	
	// This variable is used to prevent users from moving triangles while the board is getting updated
	// This prevents an exploit since the updating time is quite long due to the delay introduced to let players see what is happening when triangles are cleared
	private boolean updatingLevel = false;
	
	private Viewport viewport;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch spriteBatch;
	private GlyphLayout glyphLayout;
	private BitmapFont font;
	
	// Variables for drag-and-dropping triangles
	private Triangle selectedTriangle = null;
	private DragOverlay dragOverlay = new DragOverlay();

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
		
		Table labelTable = new Table();
		Table buttonTable = new Table();
		
		moveCountLabel = new Label("0 Moves", skin);
		pointsLabel = new Label("0 Points", skin);
		moveCountLabel.setColor(Color.BLACK);
		pointsLabel.setColor(Color.BLACK);
		labelTable.add(moveCountLabel).space(BUTTON_MARGIN);
		labelTable.add(pointsLabel).space(BUTTON_MARGIN);
		
		// The label table is position in the bottom left corner and takes up 50% of the width of the screen
		labelTable.setWidth(Prizm.STANDARD_WIDTH / 2);
		labelTable.setX(0);
		labelTable.setY(FOOTER_Y_POSITION);
		
		backButton = new TextButton("Back", skin);
		undoButton = new TextButton("Undo", skin);
		resetButton = new TextButton("Reset", skin);
		backButton.padLeft(BUTTON_PADDING).padRight(BUTTON_PADDING);
		undoButton.padLeft(BUTTON_PADDING).padRight(BUTTON_PADDING);
		resetButton.padLeft(BUTTON_PADDING).padRight(BUTTON_PADDING);
		buttonTable.add(backButton).space(BUTTON_MARGIN);
		buttonTable.add(undoButton).space(BUTTON_MARGIN);
		buttonTable.add(resetButton).space(BUTTON_MARGIN);
		
		// The button table is position in the bottom right corner and takes up 50% of the width of the screen
		buttonTable.setWidth(Prizm.STANDARD_WIDTH / 2);
		buttonTable.setX(Prizm.STANDARD_WIDTH / 2);
		buttonTable.setY(FOOTER_Y_POSITION);
		
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
		
		undoButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				level.undo();
			}
		});
		
		resetButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new GameScreen(game, skin, level.getLevelNumber()));
			}
		});
		
		stage.addActor(labelTable);
		stage.addActor(buttonTable);
		stage.addActor(popup);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.85f, 0.85f, 0.85f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
		spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
		
		level.render(delta, shapeRenderer, spriteBatch, font, glyphLayout);
		dragOverlay.render(delta, shapeRenderer);
		
		// Update the number of moves and points
		moveCountLabel.setText(level.getMoveCount() + " moves");
		pointsLabel.setText(level.getPoints() + " points");
		
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
			game.setScreen(new LevelSelectScreen(game, skin));
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
		
		if (!popup.isVisible() && !updatingLevel) {
			Triangle[][] triangles = level.getTriangles();
			
			for (Triangle[] row : triangles) {
				for (Triangle t: row) {
					if (t != null && t.getColor() != TriangleColor.BLANK && t.getColor() != TriangleColor.GREY && t.contains(screenX, screenY)) {
						selectedTriangle = t;
						dragOverlay.startDrag(selectedTriangle, screenX, screenY);
						
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
			
			dragOverlay.stopDrag();
			selectedTriangle = null;
		}
		
		return true;
	}
	
	public void updateLevel(final Triangle... updatedTriangles) {
		updatingLevel = true;
		
		// TODO The best thing to have here would be to pre-compute the state of the board,
		// then let the animations happen later. This would mean there would be no "updatingLevel"
		// variable and the user would be able to move triangles as fast as he wants.
		// Currently, the move will not initiate if one of the tasks below is in progress.
		
		Timer.schedule(new Task(){
		    @Override
		    public void run() {
		    	// Check if some colors cancel out
		    	int changes = 0;
		    	for (Triangle t: updatedTriangles)
		    		changes += level.checkCollisions(t);
		    	
		    	if (changes > 0) {
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
										game.setScreen(new WinScreen(game, skin, level.getLevelNumber(), level.getMoveCount(), level.getPoints()));
									}
								}, 0.5f);
							}
							
							updatingLevel = false;
							level.applyChanges();
							
					    }
					}, 0.5f);
		    	}
		    	else {
		    		updatingLevel = false;
		    		level.applyChanges();
		    	}
		    }
		}, 0.5f);
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		stage.touchDragged(screenX, screenY, pointer);
		
		Vector3 coords = viewport.getCamera().unproject(new Vector3(screenX, Gdx.graphics.getHeight() - screenY, 0),
				viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
		
		if (selectedTriangle != null) {
			dragOverlay.dragTo((int) coords.x, (int) coords.y);
		}
		
		return true;
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

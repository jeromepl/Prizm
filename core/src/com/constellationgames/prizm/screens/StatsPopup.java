package com.constellationgames.prizm.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.constellationgames.prizm.Level;
import com.constellationgames.prizm.Prizm;
import com.constellationgames.prizm.utils.CustomTextButton;
import com.constellationgames.prizm.utils.TriangleColor;

public class StatsPopup extends Table {
	
	public static final int WIDTH = Prizm.STANDARD_WIDTH - 140;
	public static final int HEIGHT = Prizm.STANDARD_HEIGHT - 200;
	
	private static final int STANDARD_SECTION_MARGIN = 40;
	private static final int STANDARD_PADDING = 10;
	private static final int STANDARD_BUTTON_WIDTH = 140;
	private static final int STANDARD_BUTTON_HEIGHT = 60;
	private static final float STANDARD_TITLE_SCALE = 0.2f;
	private static final float STANDARD_LABEL_SCALE = 0.12f;
	
	private Label header1, header2, upYellow, upBlue, upRed, downYellow, downBlue, downRed;
	private Image colorWheel;
	private TextButton hideButton;
	
	private Level level;

	public StatsPopup(Level level, Skin skin) {
		super();
		
		this.level = level;
		
		header1 = new Label("Color Wheel:", skin);
		header1.setColor(Color.BLACK);
		header1.setFontScale(STANDARD_TITLE_SCALE);
		
		colorWheel = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/colorWheel.gif")))));
		colorWheel.setScaling(Scaling.fit);
		
		header2 = new Label("Stats:", skin);
		header2.setColor(Color.BLACK);
		header2.setFontScale(STANDARD_TITLE_SCALE);
		
		upYellow = new Label(": 0", skin);
		upBlue = new Label(": 0", skin);
		upRed = new Label(": 0", skin);
		downYellow = new Label(": 0", skin);
		downBlue = new Label(": 0", skin);
		downRed = new Label(": 0", skin);	
		upYellow.setColor(Color.BLACK);
		upBlue.setColor(Color.BLACK);
		upRed.setColor(Color.BLACK);
		downYellow.setColor(Color.BLACK);
		downBlue.setColor(Color.BLACK);
		downRed.setColor(Color.BLACK);
		upYellow.setFontScale(STANDARD_LABEL_SCALE);
		upBlue.setFontScale(STANDARD_LABEL_SCALE);
		upRed.setFontScale(STANDARD_LABEL_SCALE);
		downYellow.setFontScale(STANDARD_LABEL_SCALE);
		downBlue.setFontScale(STANDARD_LABEL_SCALE);
		downRed.setFontScale(STANDARD_LABEL_SCALE);
		
		hideButton = new CustomTextButton("Hide", skin);
		
		// Generate buttons of all possible colors
		add(header1).colspan(4).pad(STANDARD_PADDING).center().row();
		add(colorWheel).colspan(4).center().row();
		
		add(header2).colspan(4).pad(STANDARD_PADDING).padTop(STANDARD_SECTION_MARGIN).center().row();
		add(new TriangleActor(true, TriangleColor.YELLOW.getColor())).pad(STANDARD_PADDING).center();
		add(upYellow).pad(STANDARD_PADDING).padRight(STANDARD_PADDING * 5).fill().align(Align.center); // The extra padding on this single element will move all other elements in the column
		add(new TriangleActor(false, TriangleColor.YELLOW.getColor())).pad(STANDARD_PADDING).center();
		add(downYellow).pad(STANDARD_PADDING).fill().align(Align.center).row();
		add(new TriangleActor(true, TriangleColor.BLUE.getColor())).pad(STANDARD_PADDING).center();
		add(upBlue).pad(STANDARD_PADDING).fill().align(Align.center);
		add(new TriangleActor(false, TriangleColor.BLUE.getColor())).pad(STANDARD_PADDING).center();
		add(downBlue).pad(STANDARD_PADDING).fill().align(Align.center).row();
		add(new TriangleActor(true, TriangleColor.RED.getColor())).pad(STANDARD_PADDING).center();
		add(upRed).pad(STANDARD_PADDING).fill().align(Align.center);
		add(new TriangleActor(false, TriangleColor.RED.getColor())).pad(STANDARD_PADDING).center();
		add(downRed).pad(STANDARD_PADDING).fill().align(Align.center).row();
		
		add(hideButton).size(STANDARD_BUTTON_WIDTH, STANDARD_BUTTON_HEIGHT).colspan(4).pad(STANDARD_PADDING).padTop(STANDARD_SECTION_MARGIN).center();

		hideButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				setVisible(false);
			}
		});

		// Set the size of the popup
		setWidth(WIDTH);
		setHeight(HEIGHT);
		
		setPosition((Prizm.STANDARD_WIDTH - WIDTH) / 2, (Prizm.STANDARD_HEIGHT - HEIGHT) / 2);
		
		// Add a background with a border
		NinePatch patch = new NinePatch(new Texture(Gdx.files.internal("images/popupBackground.png")), 1, 1, 1, 1);
		NinePatchDrawable background = new NinePatchDrawable(patch);
		setBackground(background);
		
		// Hide the table until necessary
		setVisible(false);
	}
	
	public void show() {
		int[][] stats = level.getTriangleStats();
		upYellow.setText(": " + stats[0][0]);
		downYellow.setText(": " + stats[0][1]);
		upBlue.setText(": " + stats[1][0]);
		downBlue.setText(": " + stats[1][1]);
		upRed.setText(": " + stats[2][0]);
		downRed.setText(": " + stats[2][1]);
		setVisible(true);
	}
	
	public void hide() {
		setVisible(false);
	}
}

class TriangleActor extends Actor {
	public static final int SIZE = 60;
	
	private ShapeRenderer shapeRenderer = new ShapeRenderer();
	private boolean overt;
	private Color color;
	
	private Vector2 p1, p2, p3;
	
	public TriangleActor(boolean overt, Color color) {
		this.color = color;
		this.overt = overt;
		this.setSize(SIZE, SIZE);
	}
	
	@Override
    public void draw(Batch batch, float parentAlpha) {
		batch.end();
		
		shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		
		if (overt) {
			p1 = new Vector2(5, 10);
			p2 = new Vector2(55, 10);
			p3 = new Vector2(30, 50);
		}
		else {
			p1 = new Vector2(5, 50);
			p2 = new Vector2(55, 50);
			p3 = new Vector2(30, 5);
		}
		
		// The triangles need to be positioned relative to the table cell
		this.localToStageCoordinates(p1);
		this.localToStageCoordinates(p2);
		this.localToStageCoordinates(p3);
		
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(color);
		shapeRenderer.triangle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);	
		shapeRenderer.end();
		
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.triangle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);	
		shapeRenderer.end();
		
		batch.begin();
    }
}

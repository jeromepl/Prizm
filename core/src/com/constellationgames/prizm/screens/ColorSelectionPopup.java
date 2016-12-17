package com.constellationgames.prizm.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture;
import com.constellationgames.prizm.Triangle;
import com.constellationgames.prizm.utils.TriangleColor;

public class ColorSelectionPopup extends Table {
	
	public static final int WIDTH = 210;
	public static final int HEIGHT = 140;
	private static final int BUTTON_SIZE = 80;
	private static final float FONT_SCALE = 0.16f;

	private ImageButton redButton, yellowButton, blueButton;
	private ClickListener leftListener, rightListener;
	
	private Label header;
	private Triangle fromTriangle, toTriangle;
	private TriangleColor color1, color2;
	
	private GameScreen gameScreen;

	public ColorSelectionPopup(final GameScreen gameScreen, Skin skin) {
		super();
		
		this.gameScreen = gameScreen;
		
		float fontScale = FONT_SCALE;
		header = new Label("Split Color", skin);
		header.setColor(Color.BLACK);
		header.setFontScale(fontScale);
		
		// Generate buttons of all possible colors
		redButton = createColorButton(TriangleColor.RED.getColor());
		blueButton = createColorButton(TriangleColor.BLUE.getColor());
		yellowButton = createColorButton(TriangleColor.YELLOW.getColor());
		
		// Initialize the click listeners
		leftListener = new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				splitColor(true);
			}
		};
		rightListener = new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				splitColor(false);
			}
		};

		// Set the size of the popup
		setWidth(WIDTH);
		setHeight(HEIGHT);
		
		// Add a background with a border
		NinePatch patch = new NinePatch(new Texture(Gdx.files.internal("images/colorSelectionBackground.png")), 1, 1, 1, 1);
		NinePatchDrawable background = new NinePatchDrawable(patch);
		setBackground(background);
		
		// Hide the table until necessary
		setVisible(false);
	}
	
	private ImageButton createColorButton(Color color) {
		Pixmap pm = new Pixmap(BUTTON_SIZE, BUTTON_SIZE, Format.RGB565);
		pm.setColor(color);
		pm.fill();
		Drawable background = new TextureRegionDrawable(new TextureRegion(new Texture(pm)));
		
		ImageButton button = new ImageButton(background);
		button.setSize(BUTTON_SIZE, BUTTON_SIZE);
		
		return button;
	}
	
	private void generateTable(Button b1, Button b2) {
		clear();
		add(header).colspan(2).pad(0, 0, 10, 0).center();
		row();
		add(b1).pad(0, 0, 0, 5);
		add(b2).pad(0, 5, 0, 0);
		
		b1.clearListeners();
		b2.clearListeners();
		b1.addListener(leftListener);
		b2.addListener(rightListener);
	}
	
	private void splitColor(boolean pickedColor1) {
		TriangleColor fromColor = pickedColor1 ? color2 : color1;
		TriangleColor toColor = pickedColor1 ? color1 : color2;
		
		fromTriangle.setColor(fromColor);
		toTriangle.setColor(TriangleColor.addColors(toTriangle.getColor(), toColor));
		setVisible(false);
		gameScreen.updateLevel(fromTriangle, toTriangle);
	}
	
	public void show(float x, float y, Triangle fromTriangle, Triangle toTriangle, TriangleColor unsplitColor) {
		this.fromTriangle = fromTriangle;
		this.toTriangle = toTriangle;
		switch (unsplitColor) {
		case GREEN:
			color1 = TriangleColor.YELLOW;
			color2 = TriangleColor.BLUE;
			generateTable(yellowButton, blueButton);
			break;
		case ORANGE:
			color1 = TriangleColor.YELLOW;
			color2 = TriangleColor.RED;
			generateTable(yellowButton, redButton);
			break;
		case PURPLE:
			color1 = TriangleColor.RED;
			color2 = TriangleColor.BLUE;
			generateTable(redButton, blueButton);
			break;
		default:
			throw new RuntimeException("Color " + unsplitColor.name() + " is not a secondary color, it cannot be split");
		}
		
		// In the case that one of the primary colors is already the destination triangle, pick the other
		if (toTriangle.getColor() == color1 || toTriangle.getColor() == color2) {
			splitColor(toTriangle.getColor() == color2);
			return;
		}
		
		setPosition(x, y);
		setVisible(true);
	}
	
	public void hide() {
		setVisible(false);
	}
}

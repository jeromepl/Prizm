package com.constellationgames.prizm.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.constellationgames.prizm.Triangle;
import com.constellationgames.prizm.utils.TriangleColor;

public class ColorSelectionPopup extends Table {
	
	private TextButton color1Button, color2Button;
	private Label header;
	private Triangle fromTriangle, toTriangle;
	private TriangleColor color1, color2;

	public ColorSelectionPopup(final GameScreen gameScreen, Skin skin) {
		super();
		
		float fontScale = 0.2f;
		header = new Label("Split Color", skin);
		header.setColor(Color.BLACK);
		header.setFontScale(fontScale);
		
		color1Button = new TextButton("Color 1", skin);
		color2Button = new TextButton("Color 2", skin);
		color1Button.pad(10);
		color2Button.pad(10);
		
		color1Button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				fromTriangle.setColor(color2);
				toTriangle.setColor(TriangleColor.addColors(toTriangle.getColor(), color1));
				setVisible(false);
				gameScreen.updateLevel(fromTriangle, toTriangle);
			}
		});
		
		color2Button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				fromTriangle.setColor(color1);
				toTriangle.setColor(TriangleColor.addColors(toTriangle.getColor(), color2));
				setVisible(false);
				gameScreen.updateLevel(fromTriangle, toTriangle);
			}
		});
		
		add(header).colspan(2).pad(0, 0, 10, 0).center();
		row();
		add(color1Button).pad(0, 0, 0, 5);
		add(color2Button).pad(0, 5, 0, 0);
		
//		Pixmap pm = new Pixmap(1, 1, Format.RGB565);
//		pm.setColor(Color.WHITE);
//		pm.fill();
//		Drawable background = new TextureRegionDrawable(new TextureRegion(new Texture(pm)));
//		setBackground(background);
		
		setVisible(false);
	}
	
	public void show(float x, float y, Triangle fromTriangle, Triangle toTriangle, TriangleColor unsplitColor) {
		this.fromTriangle = fromTriangle;
		this.toTriangle = toTriangle;
		switch (unsplitColor) {
		case GREEN:
			color1 = TriangleColor.YELLOW;
			color2 = TriangleColor.BLUE;
			break;
		case ORANGE:
			color1 = TriangleColor.YELLOW;
			color2 = TriangleColor.RED;
			break;
		case PURPLE:
			color1 = TriangleColor.RED;
			color2 = TriangleColor.BLUE;
			break;
		default:
			throw new RuntimeException("Color " + unsplitColor.name() + " is not a secondary color, it cannot be split");
		}
		
		color1Button.setText(color1.name());
		color2Button.setText(color2.name());
		
		setPosition(x, y);
		setVisible(true);
	}
	
	public void hide() {
		setVisible(false);
	}
}

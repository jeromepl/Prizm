package com.constellationgames.prizm.utils;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class CustomTextButton extends TextButton {

	public CustomTextButton(String text, Skin skin) {
		super(text, skin);
	}
	
	@Override
	public float getPrefWidth() {
		return getWidth();
	}
	
	@Override
	public float getPrefHeight() {
		return getWidth();
	}

	@Override
	public float getMinWidth() {
		return getWidth();
	}

	@Override
	public float getMinHeight() {
		return getWidth();
	}
	
	@Override
	public float getMaxWidth() {
		return getWidth();
	}

	@Override
	public float getMaxHeight() {
		return getWidth();
	}

}

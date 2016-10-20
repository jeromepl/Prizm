package com.constellationgames.prizm.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.constellationgames.prizm.Prizm;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 924; // Same aspect ratio as a nexus phone
		config.width = 576;
		config.samples = 8; // Turn on anti-aliasing
		new LwjglApplication(new Prizm(), config);
	}
}

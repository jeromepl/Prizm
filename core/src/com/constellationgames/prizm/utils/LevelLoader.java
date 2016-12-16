package com.constellationgames.prizm.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public abstract class LevelLoader {
	
	private static String[] fileNames = findAllLevels();
	
	/**
	 * Finds all level files by checking all files listed in "levels/levels.txt"
	 * Note: The order of the levels depends on the order in which they are listed in that file
	 * @return
	 */
	private static String[] findAllLevels() {
		FileHandle fileList = Gdx.files.internal("levels/levels.txt");
		return fileList.readString().split("\\n");
	}
	
	public static int getNumberOfLevels() {
		return fileNames.length;
	}
	
	public static FileHandle getLevelFile(int levelNumber) {
		levelNumber -= 1; // Use the index (starts at 0), not the number (starts at 1)
		if (levelNumber < fileNames.length) {
			return Gdx.files.internal("levels/" + fileNames[levelNumber].trim());
		}
		else
			return null;
	}

}

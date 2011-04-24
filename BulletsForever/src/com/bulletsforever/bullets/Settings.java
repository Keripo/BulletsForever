package com.bulletsforever.bullets;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.view.Display;

/**
 * This is for keeping track of app settings, drawn from SharedPreferences
 * This should be called by every Activity's onCreate()
 * Use MenuSettings to manipulate preferences
 */
public class Settings {

	private static Resources res;
	private static SharedPreferences prefs;
	private static HashMap<Integer, String> settings;
	public static int screenWidth, screenHeight;
	public static int screenXMin, screenXMax, screenYMin, screenYMax;
	
	// Manually keep this updated with settings.xml
	private static int[][] keys = {
		{R.string.bgmusic,				R.string.bgmusicDefault},
		{R.string.playerHealth,			R.string.playerHealthDefault},
		{R.string.refreshDelay,			R.string.refreshDelayDefault},
		{R.string.fpsUpdateFrequency,	R.string.fpsUpdateFrequencyDefault},
		{R.string.debugHitboxes,		R.string.debugHitboxesDefault}
	};
	
	// Reload all the settings, call this at the beginning of onCreate() in Activities
	public static void reload(Context c) {
		res = c.getResources();
		prefs = PreferenceManager.getDefaultSharedPreferences(c);
		settings = new HashMap<Integer, String>();
		for (int i = 0; i < keys.length; i++) {
			int setting = keys[i][0];
			int defaultValue = keys[i][1];
			String value = prefs.getString(res.getString(setting), res.getString(defaultValue));
			settings.put(setting, value);
		}
	}
	
	// Call this to figure out the drawable screen dimensions.
	// This is also useful for initial setup before the onDraw section
	public static void setScreenDimensions(Activity a) {
		Display display = a.getWindow().getWindowManager().getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
		int margin = (screenWidth < screenHeight) ? (screenWidth / 3) : (screenHeight / 3);
		screenXMin = -margin;
		screenXMax = screenWidth + margin;
		screenYMin = -margin;
		screenYMax = screenHeight + margin;
	}
	
	// Retrieve settings
	public static float getFloat(Integer setting) {
		return Float.parseFloat(settings.get(setting));
	}
	public static int getInt(Integer setting) {
		return Integer.parseInt(settings.get(setting));
	}
	public static boolean getBoolean(Integer setting) {
		return settings.get(setting).equals("1");
	}
	public static String getString(Integer setting) {
		return settings.get(setting);
	}
	
}

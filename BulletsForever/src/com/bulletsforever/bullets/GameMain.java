package com.bulletsforever.bullets;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * This is the app's main game Activity
 * This should be instantiated by MenuHome's startGame()
 * Multiple instances can be created/destroyed per app session, but only one can exist at a time 
 */
public class GameMain extends Activity {
	
	private DrawWorld drawWorld;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Settings.reload(this);
		Settings.setScreenDimensions(this);
		System.gc(); // Cleanup before setup
		
		// Fullscreen
		// This also solves the header problem
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// Welcome, to the World
		drawWorld = new DrawWorld(this);
		setContentView(drawWorld);
		
		// Start updating the screen
		drawWorld.startUpdating();
	}
	
	private void setupWorld() {
		// TODO
	}

}

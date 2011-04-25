package com.bulletsforever.bullets;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

/**
 * This is the app's main game Activity
 * This should be instantiated by MenuHome's startGame()
 * Multiple instances can be created/destroyed per app session, but only one can exist at a time 
 */
public class GameMain extends Activity {
	
	private DrawWorld dw;
	private AudioMusicPlayer mp;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Settings.reload(this);
		//Settings.setScreenDimensions(this);
		System.gc(); // Cleanup before setup
		
		// Fullscreen
		// This also solves the header problem
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// Music Player
		int bgmusic = -1;
		switch (Settings.getInt(R.string.bgmusic)) {
			case 1: //Techno
				bgmusic = R.raw.techno;
				break;
			case 2:  //Classical
				bgmusic = R.raw.classical;
				break;
			case 0:
			default:
				break;
		}
		if (bgmusic != -1) {
			try {
				mp = new AudioMusicPlayer();
				mp.load(getBaseContext(), bgmusic);
				mp.start();
			} catch (Exception e) {
				Log.v(e.getClass().getName(), e.getMessage());
			}
		}
		
		// Welcome, to the World
		dw = new DrawWorld(this);
		setContentView(dw);
		
		// Start updating the screen
		dw.startUpdating();
	}
	
	@Override
	protected void onDestroy() {
		if (dw != null) dw.onDestroy();
		if (mp != null) mp.onDestroy();
		super.onDestroy();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (dw.onKeyDown(keyCode, event)) {
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (dw.onKeyUp(keyCode, event)) {
			return true;
		} else {
			return super.onKeyUp(keyCode, event);
		}
	}
}

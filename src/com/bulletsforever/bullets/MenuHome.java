package com.bulletsforever.bullets;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;

/**
 * This is the app's main menu Activity
 * This should be started as the launcher Activity
 * Only a single instance should exist per app session
 */
public class MenuHome extends Activity {
	
	private static final int RESUME_MUSIC = 111;
	private AudioMusicPlayer mp;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Fullscreen
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		Settings.reload(this);
		
		// Splash screen
		setContentView(R.layout.main);
		findViewById(R.id.splash).setOnClickListener(
			new OnClickListener() {
				public void onClick(View v) {
					startGame();
				}
			}
		);
		
		startMusic();
	}
	
	private void startMusic() {
		// Music Player
		int bgmusic = -1;
		switch (Settings.getInt(R.string.bgmusic)) {
			case 1: //Techno
				bgmusic = R.raw.techno_splash;
				break;
			case 2:  //Classical
				bgmusic = R.raw.classical_splash;
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
	}
	
	private void stopMusic() {
		if (mp != null) mp.onDestroy();
		mp = null;
	}
	
	// Launch!
	private void startGame() {
		Settings.setScreenDimensions(this);
		stopMusic();
		Intent intent = new Intent();
		intent.setClass(MenuHome.this, GameMain.class);
		startActivityForResult(intent, RESUME_MUSIC);
	}
	
	// Resume menu music
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		switch (requestCode) {
			case RESUME_MUSIC:
				Settings.reload(this);
				startMusic();
				break;
			default:
				break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			stopMusic();
			Intent intent = new Intent();
			intent.setClass(MenuHome.this, MenuSettings.class);
			startActivityForResult(intent, RESUME_MUSIC);
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_SEARCH) {
			AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
			alertBuilder
				.setCancelable(true)
				.setTitle("Instructions")
				.setMessage(
					"Move the player and shoot using the touch screen.\n" +
					"Destroy all the boss parts before killing the core.\n" +
					"Player starts off with 3 lives. Get to level 8 to win!\n"
					)
				.setPositiveButton(
						"OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						}
						)
				.show()
				.setOwnerActivity(this);
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
	
	@Override
	public void onDestroy() {
		stopMusic();
		super.onDestroy();
	}
}

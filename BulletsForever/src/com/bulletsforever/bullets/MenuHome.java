package com.bulletsforever.bullets;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	// Fullscreen
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
    	// Splash screen
        setContentView(R.layout.main);
        findViewById(R.id.splash).setOnClickListener(
        	new OnClickListener() {
				@Override
				public void onClick(View v) {
					startGame();
				}
        	}
        );
    }
	
	// Launch!
	private void startGame() {
		Settings.setScreenDimensions(this);
		Intent intent = new Intent();
		intent.setClass(MenuHome.this, GameMain.class);
		MenuHome.this.startActivity(intent);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			Intent intent = new Intent();
			intent.setClass(MenuHome.this, MenuSettings.class);
			MenuHome.this.startActivity(intent);
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
}
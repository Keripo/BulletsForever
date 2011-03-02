package com.beatsportable.bullets;

import android.app.Activity;
import android.os.Bundle;

public class GameMain extends Activity {
	
	// Contains our custom GLView
	private DrawScreen screen;
	
	// Activity main
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		screen = new DrawScreen(this);
		setContentView(screen.getView());
	}
	
	// Activity lifecycle
	@Override
	protected void onPause() {
		super.onPause();
		screen.onPause();
	}
	
	// Activity lifecycle
	@Override
	protected void onResume() {
		super.onResume();
		screen.onResume();
	}
	
	
}

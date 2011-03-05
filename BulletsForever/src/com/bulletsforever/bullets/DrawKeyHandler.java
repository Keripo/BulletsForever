package com.bulletsforever.bullets;

import android.view.KeyEvent;

/**
 * This is for handling the game's key input
 * This should be instantiated by DrawWorld's initializer
 * Only a single instance should exist per GameMain instance
 */
public class DrawKeyHandler {

	private DrawWorld dw;
	
	public DrawKeyHandler(DrawWorld dw) {
		this.dw = dw;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch(keyCode) {
			case KeyEvent.KEYCODE_MENU:
				dw.mode = dw.mode.next();
				return true;
			case KeyEvent.KEYCODE_SEARCH:
				dw.removeAllBullets();
				return true;
		}
		return false;
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return false;
	}
	
}

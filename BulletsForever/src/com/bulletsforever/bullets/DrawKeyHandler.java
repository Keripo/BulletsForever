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
				if(dw.player.health <= 0)
				{
					dw.player.health = dw.player.HP_MAX;
					dw.boss= new DrawObjectDynamicBoss(dw, dw.boss.level, dw.boss.level, dw.boss.level, dw.boss.level);
					dw.startUpdating();
				}

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

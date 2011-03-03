package com.bulletsforever.bullets;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * This is for handling the game's touch input
 * This should be instantiated by DrawWorld's initializer
 * Only a single instance should exist per GameMain instance
 */
public class DrawTouchHandler implements OnTouchListener {

	private DrawWorld dw;
	
	public DrawTouchHandler(DrawWorld dw) {
		this.dw = dw;
	}
	
	// Do not add multi-touch support for simplicity (no need for it in my opinion)
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch(event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				DrawObjectBullet bullet = new DrawObjectBullet(
						event.getX(),
						event.getY(),
						(float)(Math.random() - 0.5f) * 10f,
						(float)(Math.random() - 0.5f) * 10f
						);
				dw.addBullet(bullet);
				return true;
			case MotionEvent.ACTION_UP:
				return false;
			default:
				return false;
		}
	}

}

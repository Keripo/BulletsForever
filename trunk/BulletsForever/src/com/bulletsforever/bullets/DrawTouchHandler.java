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
	public boolean onTouch(View v, MotionEvent event) {
		switch(event.getAction()) {
			case MotionEvent.ACTION_MOVE:
				if (dw.mode.equals(DrawWorld.DemoMode.MOVE)) {
					// Change player's target location
					dw.player.tx = event.getX();
					dw.player.ty = event.getY();
				}
				return true;
			case MotionEvent.ACTION_DOWN:
				if (dw.mode.equals(DrawWorld.DemoMode.MOVE)) {
					// Change player's target location
					dw.player.tx = event.getX();
					dw.player.ty = event.getY();
					
					dw.player.shooting = true;
				// For demo purposes
				} else if (dw.mode.equals(DrawWorld.DemoMode.RANDOM)) {
					// Add 100 bullets in random locations at random speeds and angles
					for (int i = 0; i < 100; i++) { 
						DrawObjectBullet bullet = new DrawObjectBullet(
								dw, true,
								(float)Math.random() * Settings.screenWidth,
								(float)Math.random() * Settings.screenHeight,
								(float)Math.random() * 10f, 0f,
								0f, 0f,
								(float)Math.random() * 360f, 0f
								);
						dw.addBullet(bullet);
					}
				} else if (dw.mode.equals(DrawWorld.DemoMode.EXPLOSION)) {
					// Add 360 bullets fanning out from the point of contact
					for (int i = 0; i < 360; i++) {
						DrawObjectBullet bullet = new DrawObjectBullet(
								dw, true,
								event.getX(), event.getY(),
								(float)Math.random() * 10f, 0f,
								0f, 0f,
								i, 0f
								);
						dw.addBullet(bullet);
					}
				} else if (dw.mode.equals(DrawWorld.DemoMode.FIREWORKS)) {
					// Add 360 bullets spouting out from the point of contact
					for (int i = 0; i < 360; i++) {
						DrawObjectBullet bullet = new DrawObjectBullet(
								dw, true,
								event.getX(), event.getY(),
								(float)Math.random() * 10f, 0f,
								0f, 0.5f,
								i, 0f
								);
						dw.addBullet(bullet);
					}
				} else if (dw.mode.equals(DrawWorld.DemoMode.SPIRALS)) {
					// Add 180 bullets spiraling out from the point of contact
					for (int i = 0; i < 360; i += 2) {
						DrawObjectBullet bullet = new DrawObjectBullet(
								dw, true,
								event.getX(), event.getY(),
								(float)Math.random() * 10f, 0.1f,
								0f, 0f,
								i, 10f
								);
						dw.addBullet(bullet);
					}
				} else if (dw.mode.equals(DrawWorld.DemoMode.RINGS)) {
					// Add 36 bullets ringing out from the point of contact
					for (int i = 0; i < 360; i += 10) {
						DrawObjectBullet bullet = new DrawObjectBullet(
								dw, true,
								event.getX(), event.getY(),
								5f, 0f,
								0f, 0f,
								i, 10f
								);
						dw.addBullet(bullet);
					}
				} else if (dw.mode.equals(DrawWorld.DemoMode.ZOOM)) {
					// Add 36 bullets zooming out from the point of contact
					for (int i = 0; i < 360; i += 10) {
						DrawObjectBullet bullet = new DrawObjectBullet(
								dw, true,
								event.getX(), event.getY(),
								0f, 0.5f,
								0f, 0f,
								i, -15f
								);
						dw.addBullet(bullet);
					}
				}
				return true;
			case MotionEvent.ACTION_UP:
				dw.player.shooting = false;
				return true;
			default:
				return false;
		}
	}

}

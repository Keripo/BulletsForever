package com.bulletsforever.bullets;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * This is the player!
 * This should be instantiated by GameMain's setupWorld()
 * Only a single instance should exist per GameMain instance
 */
public class DrawObjectBullet extends DrawObject {
	
	public boolean remove;
	private Paint bulletPaint;
	char x;
	public static float hitboxHalfWidth=5f;
	public static float hitboxHalfHeight=5f;
	// x= 'p' for player bullet and 'b' for boss bullet.
	
	public DrawObjectBullet(float x, float y, float v, float a, float gx, float gy, float angle, float angle_v, char xx) {
		
		
		
		super(x, y, v, a, gx, gy, angle, angle_v, hitboxHalfWidth, hitboxHalfHeight);
		this.remove = false;
		this.x=xx;
		
		bulletPaint = new Paint();
		bulletPaint.setColor(Color.WHITE);
	}
	
	@Override
	public void nextFrame() {
		super.nextFrame();
		
		// If off-screen
		if (x < Settings.screenXMin ||
			x > Settings.screenXMax ||
			y < Settings.screenYMin ||
			y > Settings.screenYMax
			) {
			remove = true;
		}
	}
	
	@Override
	public void draw(Canvas canvas) {
		canvas.drawRect(
				x - hitboxHalfWidth, y - hitboxHalfHeight,
				x + hitboxHalfWidth, y + hitboxHalfHeight,
				bulletPaint
				);
	}

	@Override
	public void onCollision(DrawObject object) {
		// TODO Auto-generated method stub
		
	}

}

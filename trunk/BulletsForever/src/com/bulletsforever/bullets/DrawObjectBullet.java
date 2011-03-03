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
	
	public DrawObjectBullet(float x, float y, float v, float angle) {
		super(x, y, v, angle, 5f, 5f);
		this.remove = false;
		
		bulletPaint = new Paint();
		bulletPaint.setColor(Color.WHITE);
	}
	
	@Override
	public void nextFrame() {
		super.nextFrame();
		
		// If off-screen
		if (x < 0 || y < 0 ||
			x > Settings.screenWidth ||
			y > Settings.screenHeight
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

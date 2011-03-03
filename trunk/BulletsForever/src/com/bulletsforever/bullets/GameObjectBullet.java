package com.bulletsforever.bullets;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * This is the player!
 * This should be instantiated by GameMain's setupWorld()
 * Only a single instance should exist per GameMain instance
 */
public class GameObjectBullet extends GameObject {
	
	public boolean remove;
	private float dx, dy;
	private Paint bulletPaint;
	
	public GameObjectBullet(float x, float y, float dx, float dy) {
		this.hitboxHalfWidth = 5f;
		this.hitboxHalfHeight = 5f;
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.remove = false;
		
		bulletPaint = new Paint();
		bulletPaint.setColor(Color.WHITE);
	}
	
	@Override
	public void nextFrame() {
		x += dx;
		y += dy;
		
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
	public void onCollision(GameObject object) {
		// TODO Auto-generated method stub
		
	}

}

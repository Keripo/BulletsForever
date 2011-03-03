package com.bulletsforever.bullets;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * This is the player!
 * This should be created throughout a GameMain session
 * Multiple instances should be added and removed from DrawWorld
 */
public class GameObjectPlayer extends GameObject {

	private Paint avatarPaint;
	
	public GameObjectPlayer() {
		this.hitboxHalfWidth = 25f;
		this.hitboxHalfHeight = 25f;
		this.x = Settings.screenWidth / 2;
		this.y = Settings.screenHeight / 2;
		
		avatarPaint = new Paint();
		avatarPaint.setColor(Color.YELLOW);
	}
	
	@Override
	public void nextFrame() {
		
		// Lalalala
		// This player is hyper and running off coffee, like me! (s/coffee/tea)
		// ~Phil, 4am in the morning
		x += (Math.random() - 0.5f) * 15;
		y += (Math.random() - 0.5f) * 15;
		if (x < hitboxHalfWidth ||
			y < hitboxHalfHeight ||
			x > Settings.screenWidth - hitboxHalfWidth ||
			y > Settings.screenHeight - hitboxHalfHeight
			){ // Offscreen
			x = ((float)Math.random() * (Settings.screenWidth - hitboxHalfWidth)) + hitboxHalfWidth;
			y = ((float)Math.random() * (Settings.screenHeight - hitboxHalfHeight)) + hitboxHalfHeight;
		}
	}
	
	@Override
	public void draw(Canvas canvas) {
		canvas.drawRect(
				x - hitboxHalfWidth, y - hitboxHalfHeight,
				x + hitboxHalfWidth, y + hitboxHalfHeight,
				avatarPaint
				);
	}

	@Override
	public void onCollision(GameObject object) {
		avatarPaint.setARGB(
				255,
				(int)(Math.random() * 255),
				(int)(Math.random() * 255),
				(int)(Math.random() * 255)
				);
		
	}

}

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

	private float avatarWidthHalf, avatarHeightHalf; // You're a box! (for now)
	private Paint avatarPaint;
	
	public GameObjectPlayer() {
		avatarWidthHalf = 25f;
		avatarHeightHalf = 25f;
		
		avatarPaint = new Paint();
		avatarPaint.setColor(Color.YELLOW);
		
		// Force offscreen so the start location is randomized
		x = Settings.screenWidth / 2;
		y = Settings.screenHeight / 2;
	}
	
	@Override
	public boolean checkCollision(GameObject o1, GameObject o2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void draw(Canvas canvas) {
		
		// Lalalala
		// This player is hyper and running off coffee, like me! (s/coffee/tea)
		// ~Phil, 4am in the morning
		x += (Math.random() - 0.5f) * 15;
		y += (Math.random() - 0.5f) * 15;
		if (x < avatarWidthHalf ||
			y < avatarHeightHalf ||
			x > Settings.screenWidth - avatarWidthHalf ||
			y > Settings.screenHeight - avatarHeightHalf
			){ // Offscreen
			x = ((float)Math.random() * (Settings.screenWidth - avatarWidthHalf)) + avatarWidthHalf;
			y = ((float)Math.random() * (Settings.screenHeight - avatarHeightHalf)) + avatarHeightHalf;
		}
		
		canvas.drawRect(
				x - avatarWidthHalf, y - avatarHeightHalf,
				x + avatarWidthHalf, y + avatarHeightHalf,
				avatarPaint
				);
	}

}

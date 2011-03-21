package com.bulletsforever.bullets;

import android.graphics.Canvas;

/**
 * This is the player!
 * This should be created throughout a GameMain session
 * Multiple instances should be added and removed from DrawWorld
 */
public class DrawObjectPlayer extends DrawObject {

	//private Paint avatarPaint;
	public float tx, ty;
	
	public DrawObjectPlayer(DrawWorld dw) {
		// Middle of the screen
		super(
			dw,
			Settings.screenWidth / 2, Settings.screenHeight - 100,   //Set the initial location for the player
			0f,	0f, 0f, 0f, 0f, 0f,
			40f, 50f
			);
		
		bitmap = dw.bl.getBitmap(R.drawable.iconplayer, hitboxHalfWidth, hitboxHalfHeight);
		tx = x; // Don't move
		ty = y; // Don't move
		//avatarPaint = new Paint();
		//avatarPaint.setColor(Color.BLUE);
	}
	
	public void nextFrame() {
		// Move to where the target destination is
		float dtx = (tx > x) ? (tx - x) : (x - tx);
		float dty = (ty > y) ? (ty - y) : (y - ty);
		if (dtx > 0.5f && dty > 0.5f) { // floating point precision of 0.5 pixel (possible?)
			dx = (tx - x) * 0.1f;
			dy = (ty - y) * 0.1f;
			super.nextFrame();
		}
		//super.nextFrame();
		
		// Lalalala
		// This player is hyper and running off coffee, like me! (s/coffee/tea)
		// ~Phil, 4am in the morning
		
		
		// the following lines have been commented by Austin and Yash on 17th march night.
		/*
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
		*/
	}
	
	@Override
	public void draw(Canvas canvas) {
		/*
		canvas.drawRect(
				x - hitboxHalfWidth, y - hitboxHalfHeight,
				x + hitboxHalfWidth, y + hitboxHalfHeight,
				avatarPaint
				);
		*/
		canvas.drawBitmap(bitmap, x - hitboxHalfWidth, y - hitboxHalfHeight, null);
	}

	@Override
	public void onCollision(DrawObject object) {
		/*
		avatarPaint.setARGB(
				255,
				(int)(Math.random() * 255),
				(int)(Math.random() * 255),
				(int)(Math.random() * 255)
				);
		*/
	}

}
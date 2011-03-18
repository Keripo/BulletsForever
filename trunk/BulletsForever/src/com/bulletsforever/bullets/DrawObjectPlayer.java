package com.bulletsforever.bullets;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * This is the player!
 * This should be created throughout a GameMain session
 * Multiple instances should be added and removed from DrawWorld
 */
public class DrawObjectPlayer extends DrawObject {

	private Paint avatarPaint;
	public static float x=Settings.screenWidth / 2;
	public static float y=Settings.screenHeight-100;
	public static float hitboxHalfWidth=50f;
	public static float hitboxHalfHeight=25f;
	
	
	
	public DrawObjectPlayer() {
		// Middle of the screen
		
	
		
		super(
			x,y,   //Set the initial location for the player
			0f,	0f, 0f, 0f, 0f, 0f,
			hitboxHalfWidth, hitboxHalfHeight
			);
		
		
		
		avatarPaint = new Paint();
		avatarPaint.setColor(Color.BLUE);
	}
	
	public void nextFrame() {
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
		canvas.drawRect(
				x - hitboxHalfWidth, y - hitboxHalfHeight,
				x + hitboxHalfWidth, y + hitboxHalfHeight,
				avatarPaint
				);
		
	}

	@Override
	public void onCollision(DrawObject object) {
		avatarPaint.setARGB(
				255,
				(int)(Math.random() * 255),
				(int)(Math.random() * 255),
				(int)(Math.random() * 255)
				);
		
	}

}

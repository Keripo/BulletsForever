package com.bulletsforever.bullets;

import android.graphics.Canvas;

/**
 * This is the player!
 * This should be created throughout a GameMain session
 * Multiple instances should be added and removed from DrawWorld
 */
public class DrawObjectPlayer extends DrawObject {

	public float tx, ty;
	private static final float MAX_SPEED = 20f;
	private static final float DAMP_RATIO = 0.75f;
	
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
	}
	
	public void nextFrame() {
		// Move to where the target destination is
		float dtx = ((tx > x) ? (tx - x) : (x - tx)) * DAMP_RATIO;
		float dty = ((ty > y) ? (ty - y) : (y - ty)) * DAMP_RATIO;
		if (dtx > 0.5f || dty > 0.5f) { // floating point precision of 0.5 pixel (possible?)
			dx = (tx - x) * DAMP_RATIO;
			dy = (ty - y) * DAMP_RATIO;
			if (dtx > MAX_SPEED || dty > MAX_SPEED) {
				float ratio = MAX_SPEED / (float)Math.sqrt(dx * dx + dy * dy);
				dx *= ratio;
				dy *= ratio;
			}
			super.nextFrame();
		} else {
			dx = 0;
			dy = 0;
		}
	}
	
	@Override
	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap, x - hitboxHalfWidth, y - hitboxHalfHeight, null);
	}

	@Override
	public void onCollision(DrawObject object) {
		// TODO
	}

}

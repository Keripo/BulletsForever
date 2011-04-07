package com.bulletsforever.bullets;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

/**
 * This is the player!
 * This should be created throughout a GameMain session
 * Multiple instances should be added and removed from DrawWorld
 */
public class DrawObjectPlayer extends DrawObject {

	public int health;
	public float tx, ty;
	public boolean shooting;
	
	private int hit_frames;
	private Paint hit_filter;	
	
	private static final int HP_MAX = 100;
	private static final int HP_YELLOW = 50;
	private static final int HP_RED = 25;
	private static final int MAX_HIT_FRAMES = 5;
	private static final float MAX_SPEED = 20f;
	private static final float DAMP_RATIO = 0.75f;
	
	public DrawObjectPlayer(DrawWorld dw) {
		// Middle of the screen
		super(
			dw,
			Settings.screenWidth / 2, Settings.screenHeight - 100,   //Set the initial location for the player
			0f,	0f, 0f, 0f, 0f, 0f,
			20f, 25f
			);
		
		// Note: Image is bigger than hitbox on purpose (4x the size)
		bitmap = dw.bl.getBitmap(R.drawable.iconplayer, hitboxHalfWidth * 2, hitboxHalfHeight * 2);
		drawOffsetX = hitboxHalfWidth * 2;
		drawOffsetY = hitboxHalfHeight * 4;
		tx = x; // Don't move
		ty = y; // Don't move
		shooting = false; // Don't shoot at first
		
		// Damaged
		health = HP_MAX;
		hit_frames = 0;
		
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
		} else {
			dx = 0;
			dy = 0;
		}
		
		// Add player bullets
		if (shooting && frame % 2 == 0) { // Fire every other frames
			for (int i = 0; i < dw.boss.level; i++) {
				// Randomly spray bullets in a 20 degree cone
				float angle_offset = (float)(Math.random() - 0.5f) * 20f;
				DrawObjectBullet bullet = new DrawObjectBullet(
						dw, false,
						x,
						y,
						10f, 5f,
						0f, 0f,
						180f + angle_offset, 0f
				);	
				dw.addBullet(bullet);
			}
		}
		
		super.nextFrame();
	}
	
	@Override
	public void draw(Canvas canvas) {
		if (hit_frames > 0) {
			hit_filter = new Paint();
			hit_filter.setColorFilter(new PorterDuffColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY));
			canvas.drawBitmap(bitmap, x - drawOffsetX, y - drawOffsetY, hit_filter);
			hit_frames--;
		} else if(health <= HP_YELLOW && health > HP_RED){
			hit_filter = new Paint();
			hit_filter.setColorFilter(new PorterDuffColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY));
			canvas.drawBitmap(bitmap, x - drawOffsetX, y - drawOffsetY, hit_filter);
		} else if(health <= HP_RED){
			hit_filter = new Paint();
			hit_filter.setColorFilter(new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY));
			canvas.drawBitmap(bitmap, x - drawOffsetX, y - drawOffsetY, hit_filter);
		} else {
			canvas.drawBitmap(bitmap, x - drawOffsetX, y - drawOffsetY, null);
		}
	}

	@Override
	public void onCollision(DrawObject object) {
		hit_frames = MAX_HIT_FRAMES;
		health--;
		
	}

}
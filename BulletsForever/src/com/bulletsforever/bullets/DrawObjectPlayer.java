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
	private Paint collision_filter;
	
	public int HP_MAX;
	private int HP_YELLOW;
	private int HP_RED;
	
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
		
		// Health
		HP_MAX = Settings.getInt(R.string.playerHealth);
		HP_YELLOW = HP_MAX / 3;
		HP_RED = HP_MAX / 5;
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
						dw, false, false,
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
	
//	int flag_red = 0;
//	int flag_green=0;
	int flag_blue=0;
	
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
			
		} else if(dw.bigCollision){
			collision_filter = new Paint();
			
			collision_filter.setColorFilter(new PorterDuffColorFilter(Color.rgb(255, 153, 255- flag_blue), PorterDuff.Mode.MULTIPLY));
			canvas.drawBitmap(bitmap, x - drawOffsetX, y - drawOffsetY, collision_filter);
			
			//if(flag_red<99)
			//flag_red++;
			
			//if(flag_green<184)
				//flag_green++;
			
			if(flag_blue< 255)
			flag_blue = flag_blue+1;
			
		} else
			canvas.drawBitmap(bitmap, x - drawOffsetX, y - drawOffsetY, null);
	}

	@Override
	public void onCollision(DrawObject object) {
		hit_frames = MAX_HIT_FRAMES;
		health--;
		
	}

}

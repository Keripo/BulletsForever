package com.bulletsforever.bullets;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Paint.Align;

/**
 * This is DrawWorld's HUD
 * This should be instantiated by DrawWorld's onCreate()
 * Only a single instance should exist per DrawWorld instance
 */
public class DrawObjectHUD extends DrawObject {
	
	// Stuff
	private ToolsFPSCounter fpsCounter;
	private Paint textPaint;
	
	// FPS
	private float fpsX, fpsY;
	
	// Info
	private float infoX1, infoY1;
	private float infoX2, infoY2;
	
	// Box
	private Paint boxPaint;
	private Rect boxRect;
	
	public DrawObjectHUD(DrawWorld dw) {
		super(dw, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f); // dummy
		
		// Text
		textPaint = new Paint();
		textPaint.setColor(Color.WHITE);
		textPaint.setTextSize(20f);
		textPaint.setTextAlign(Align.LEFT);
		textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
		
		// Info
		infoX1 = 10f;
		infoY1 = textPaint.getFontSpacing();
		infoX2 = 10f;
		infoY2 = infoY1 + textPaint.getFontSpacing();
		
		// FPS counter
		fpsCounter = new ToolsFPSCounter(Settings.getInt(R.string.fpsUpdateFrequency));
		fpsX = 10f;
		fpsY = infoY2 + textPaint.getFontSpacing();
		
		// Box
		boxPaint = new Paint();
		boxPaint.setARGB(255/2, 32, 128, 32); // Dark green
		boxRect = new Rect();
		boxRect.set(0, 0, Settings.screenWidth, (int)(fpsY + textPaint.getFontSpacing() / 2));
		
	}
	
	@Override
	public void nextFrame() {
		// FPS counter
		fpsCounter.nextFrame();
	}
	
	@Override
	public void draw(Canvas canvas) {
		// Box
		canvas.drawRect(boxRect, boxPaint);
		
		// Info
		canvas.drawText(
				String.format(
						"Player: HP: %d, Bullets: %d, Collisions: %d",
						dw.player.health,
						dw.player_bullets.size(),
						dw.collisionCountPlayer),
				infoX1, infoY1, textPaint);
		canvas.drawText(
				String.format(
						"Boss: HP: %d, Bullets: %d, Collisions: %d",
						dw.boss.health,
						dw.boss_bullets.size(),
						dw.collisionCountBoss),
				infoX2, infoY2, textPaint);
		
		// FPS counter
		canvas.drawText(fpsCounter.getDisplayedFPS(), fpsX, fpsY, textPaint);
	
	}
	
	@Override
	public void onCollision(DrawObject object) {
		// Do nothing		
	}
}

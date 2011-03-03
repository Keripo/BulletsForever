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
	private DrawWorld dw;
	private ToolsFPSCounter fpsCounter;
	
	// FPS
	private Paint fpsPaint;
	private float fpsX, fpsY;
	
	// Info
	private float infoX, infoY;
	public int bulletCount, collisionCount;
	
	// Box
	private Paint boxPaint;
	private Rect boxRect;
	
	public DrawObjectHUD(DrawWorld dw) {
		this.dw = dw;
		
		// FPS counter
		fpsCounter = new ToolsFPSCounter(Settings.getInt(R.string.fpsUpdateFrequency));
		fpsPaint = new Paint();
		fpsPaint.setColor(Color.WHITE);
		fpsPaint.setTextSize(20f);
		fpsPaint.setTextAlign(Align.LEFT);
		fpsPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
		fpsX = 10f;
		fpsY = 10f + fpsPaint.getFontSpacing();
		
		// Info
		infoX = 10f;
		infoY = fpsY + 10f + fpsPaint.getFontSpacing();
		bulletCount = 0;
		collisionCount = 0;
		
		// Box
		boxPaint = new Paint();
		boxPaint.setARGB(255/2, 32, 128, 32); // Dark green
		boxRect = new Rect();
		boxRect.set(0, 0, Settings.screenWidth, (int)(infoY + fpsPaint.getFontSpacing()));
		
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
		
		// FPS counter
		canvas.drawText(fpsCounter.getDisplayedFPS(), fpsX, fpsY, fpsPaint);
		
		// Info
		canvas.drawText(
				String.format(
						"Bullets: %d, Collisions: %d, Bullets/frame: %d",
						bulletCount, collisionCount, dw.randomBulletsPerFrame),
				infoX, infoY, fpsPaint);
	}
	
	@Override
	public void onCollision(DrawObject object) {
		// Do nothing		
	}
}

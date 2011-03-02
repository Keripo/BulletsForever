package com.bulletsforever.bullets;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Align;

/**
 * This is DrawWorld's HUD
 * This should be instantiated by DrawWorld's onCreate()
 * Only a single instance should exist per DrawWorld instance
 */
public class GameObjectHUD extends GameObject {
	
	private ToolsFPSCounter fpsCounter;
	private Paint fpsPaint;
	private float fpsX, fpsY;
	
	public GameObjectHUD() {
		
		// FPS counter
		fpsCounter = new ToolsFPSCounter(Settings.getInt(R.string.fpsUpdateFrequency));
		fpsPaint = new Paint();
		fpsPaint.setColor(Color.WHITE);
		fpsPaint.setTextSize(20f);
		fpsPaint.setTextAlign(Align.LEFT);
		fpsPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
		fpsX =  10f;
		fpsY = 10f + fpsPaint.getFontSpacing();
	}
	
	@Override
	public void update(int frame) {
		
		// FPS counter
		fpsCounter.nextFrame();
	}
	
	@Override
	public void draw(Canvas canvas) {
		
		// FPS counter
		canvas.drawText(fpsCounter.getDisplayedFPS(), fpsX, fpsY, fpsPaint);
		
	}
	
	@Override
	public void onCollision(GameObject object) {
		// Do nothing		
	}
}

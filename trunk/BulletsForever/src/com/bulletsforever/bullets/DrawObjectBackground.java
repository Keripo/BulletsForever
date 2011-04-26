package com.bulletsforever.bullets;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;

/**
 * This is DrawWorld's background grid
 * This should be instantiated by DrawWorld's onCreate()
 * Only a single instance should exist per DrawWorld instance
 */
public class DrawObjectBackground extends DrawObject {

	private Paint colourPaint;
	private RectF[] ovals;
	private int driftMode;
	private final int DRIFT_FRAMES = 150; // ~3s
	private final int NUM_OVALS = 10;
	
	public DrawObjectBackground(DrawWorld dw) {
		super(dw, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f); // dummy
		
		driftMode = 0;
		
		colourPaint = new Paint();
		colourPaint.setColor(Color.GREEN);
		colourPaint.setAlpha(64);
		colourPaint.setStyle(Style.STROKE);
		colourPaint.setStrokeWidth(2);
		ovals = new RectF[NUM_OVALS];
		int dimension = (Settings.screenHeight < Settings.screenWidth) ? Settings.screenHeight : Settings.screenWidth; 
		for (int i = 0; i < NUM_OVALS / 2; i++) {
			float width = i * dimension / (NUM_OVALS / 2);
			float height = dimension;
			ovals[i] = new RectF(
					Settings.screenWidth / 2 - width,
					Settings.screenHeight / 2 - height,
					Settings.screenWidth / 2 + width,
					Settings.screenHeight / 2 + height
					);
		}
		for (int i = 0; i < NUM_OVALS / 2; i++) {
			float width = dimension;
			float height = i * dimension / (NUM_OVALS / 2);
			ovals[i + NUM_OVALS / 2] = new RectF(
					Settings.screenWidth / 2 - width,
					Settings.screenHeight / 2 - height,
					Settings.screenWidth / 2 + width,
					Settings.screenHeight / 2 + height
					);
		}
		
	}
	
	@Override
	public void nextFrame() {
		frame++;
		if (frame % DRIFT_FRAMES == 0) {
			if (driftMode == 7) {
				driftMode = 0;
			} else {
				driftMode++;
			}
		}
		switch (driftMode) {
		/*	 __
		 *  |__|__
		 *     |__| 
		 */
			default:
			case 0: for (RectF oval : ovals) oval.offset(0, 0.3f); break;
			case 1: for (RectF oval : ovals) oval.offset(0.3f, 0); break;
			case 2: for (RectF oval : ovals) oval.offset(0, -0.3f); break;
			case 3: for (RectF oval : ovals) oval.offset(-0.3f, 0); break;
			case 4: for (RectF oval : ovals) oval.offset(-0.3f, 0); break;
			case 5: for (RectF oval : ovals) oval.offset(0, -0.3f); break;
			case 6: for (RectF oval : ovals) oval.offset(0.3f, 0); break;
			case 7: for (RectF oval : ovals) oval.offset(0, 0.3f); break;
			
		}
	}
	
	@Override
	public void draw(Canvas canvas) {
		for (RectF oval : ovals) {
			canvas.drawOval(oval, colourPaint);
		}
	}
	
	@Override
	public void onCollision(DrawObject object) {
		// Do nothing		
	}
}

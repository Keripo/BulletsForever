package com.bulletsforever.bullets;

import android.graphics.Canvas;

/**
 * This is the boss!
 * This should be instantiated by GameMain's setupWorld()
 * Only a single instance should exist per GameMain instance
 */
public class DrawObjectBoss extends DrawObject {
	
	public DrawObjectBoss(float x, float y, float v, float angle) {
		// TODO Auto-generated constructor stub
		super(x, y, v, angle, 100f, 100f);
	}

	public void nextFrame() {
		// TODO Auto-generated method stub
		super.nextFrame();
	}
	
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCollision(DrawObject object) {
		// TODO Auto-generated method stub
		
	}


}

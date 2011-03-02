package com.bulletsforever.bullets;

import android.graphics.Canvas;

/**
 * This is DrawWorld's drawable objects
 * This should be created throughout a GameMain session
 * Multiple instances should be added and removed from DrawWorld
 * Don't forget to update coords and properties in the draw method!
 */
public abstract class GameObject {

	public float x, y; // Its faster for direct access than using unnecessary get/set methods
	public abstract boolean checkCollision(GameObject o1, GameObject o2);
	public abstract void draw(Canvas canvas);
	
}

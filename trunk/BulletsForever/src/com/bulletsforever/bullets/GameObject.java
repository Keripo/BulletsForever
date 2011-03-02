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
	public float hitboxHalfWidth, hitboxHalfHeight;
	public boolean hasCollided(GameObject object) {
		return (object.x > x - hitboxHalfWidth &&
				object.x < x + hitboxHalfWidth &&
				object.y > y - hitboxHalfHeight &&
				object.y < y + hitboxHalfHeight);
	}
	public abstract void draw(Canvas canvas);
	public abstract void update(int frame);
	public abstract void onCollision(GameObject object);
	
}
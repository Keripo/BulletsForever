package com.bulletsforever.bullets;

import android.graphics.Canvas;

/**
 * This is DrawWorld's drawable objects
 * This should be created throughout a GameMain session
 * Multiple instances should be added and removed from DrawWorld
 * Don't forget to update coords and properties in the draw method!
 */
public abstract class DrawObject {

	// Path movement
	public float x, y; // Its faster for direct access than using unnecessary get/set methods
	public float v; // absolute velocity per frame, range [0.0f,1.0f)
	//public float a; // absolute acceleration per frame, range [0.0f,1.0f) - NOT USED AT THE MOMENT!
	public float angle; // out of 360* for easy understanding
	public float dx, dy; // change in x and y per frame, calculated vased on velocity and angle
	
	// Hitbox
	public float hitboxHalfWidth, hitboxHalfHeight;
	
	// Initializer
	public DrawObject(
		float x, float y, float v, float angle,
		float hitboxHalfWidth, float hitboxHalfHeight
		) {
		this.x = x;
		this.y = y;
		this.dx = (float)Math.cos(angle / 360f) * v;
		this.dy = (float)Math.sin(angle / 360f) * v;
		this.hitboxHalfWidth = hitboxHalfWidth;
		this.hitboxHalfHeight = hitboxHalfHeight;
	}
	
	// Canvas-specific drawing
	public abstract void draw(Canvas canvas);
	
	// Update coords, etc.
	; // Synchronous frame by frame - no skipping!
	public void nextFrame() {
		x += dx;
		y += dy;
	}
	
	// Collision check
	public boolean hasCollided(DrawObject object) {
		return (object.x > x - hitboxHalfWidth &&
				object.x < x + hitboxHalfWidth &&
				object.y > y - hitboxHalfHeight &&
				object.y < y + hitboxHalfHeight);
	}
	
	// Call on collisions
	public abstract void onCollision(DrawObject object);
	
}

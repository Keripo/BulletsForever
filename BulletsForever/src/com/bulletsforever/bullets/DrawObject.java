package com.bulletsforever.bullets;

import android.graphics.Canvas;

/**
 * This is DrawWorld's drawable objects
 * This should be created throughout a GameMain session
 * Multiple instances should be added and removed from DrawWorld
 * Don't forget to update coords and properties in the draw method!
 */
public abstract class DrawObject {

	// Tracking progress
	public int frame;
	
	// Path movement
	public float x, y; // Its faster for direct access than using unnecessary get/set methods
	public float v; // absolute velocity per frame
	public float vr; // angular velocity per frame
	public float ax, ay; // absolute acceleration per frame
	public float angle; // out of 360* for easy understanding
	public float rad; // angle * pi / 180
	public float dx, dy; // change in x and y per frame, calculated based on velocity and angle
	
	// Hitbox
	public float hitboxHalfWidth, hitboxHalfHeight;
	
	// Initializer
	public DrawObject(
		float x, float y, float v, float vr, float ax, float ay, float angle,
		float hitboxHalfWidth, float hitboxHalfHeight
		) {
		this.frame = 0;
		this.x = x;
		this.y = y;
		this.v = v;
		this.vr = vr;
		this.ax = ax;
		this.ay = ay;
		this.angle = angle;
		this.rad = angle * (float)Math.PI / 180f;
		this.hitboxHalfWidth = hitboxHalfWidth;
		this.hitboxHalfHeight = hitboxHalfHeight;
		recalculate();
	}
	
	// Force recalculate projection path
	public void recalculate() {
		dx = (float)(v * Math.sin(rad));
		dy = (float)(v * Math.cos(rad));
		angle += vr;
		rad = angle * (float)Math.PI / 180f;
		vr *= 0.95f; // decay to allow for spiraling
	}
	
	// Canvas-specific drawing
	public abstract void draw(Canvas canvas);
	
	// Update coords, etc.
	// Synchronous frame by frame - no skipping!
	public void nextFrame() {
		frame++;
		x += dx;
		y += dy;
		dx += ax;
		dy += ay;
		if (vr > 0f) {
			recalculate();
		}
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

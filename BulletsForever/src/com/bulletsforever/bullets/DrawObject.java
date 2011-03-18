package com.bulletsforever.bullets;

import android.graphics.Canvas;

/**
 * This is DrawWorld's drawable objects
 * This should be created throughout a GameMain session
 * Multiple instances should be added and removed from DrawWorld
 * Don't forget to call super.nextFrame() if the object has motion
 */
public abstract class DrawObject {

	// Tracking progress
	public int frame;
	
	// Public accessible path info
	public float x, y; // Its faster for direct access than using unnecessary get/set methods
	public float v, a; // absolute velocity and acceleration per frame
	public float gx, gy; // absolute gravity per frame
	public float angle; // out of 360* for easy understanding
	public float angle_v; // angular velocity per frame
	public float dx, dy; // change in x and y per frame, calculated based on velocity and angle
	
	// Private calculated path info
	private float dgx, dgy;
	private float rad; // angle * pi / 180 - for internal calculations
	
	// Hitbox
	public float hitboxHalfWidth, hitboxHalfHeight;
	
	// Initializer
	public DrawObject(
		float x, float y, float v, float a, float gx, float gy, float angle, float angle_v,
		float hitboxHalfWidth, float hitboxHalfHeight
		) {
		// Start object's individual frame counter at 0
		this.frame = 0;
		
		// Setup public info
		this.x = x;
		this.y = y;
		this.v = v;
		this.a = a;
		this.gx = gx;
		this.gy = gy;
		this.angle = angle;
		this.angle_v = angle_v;
		this.hitboxHalfWidth = hitboxHalfWidth;
		this.hitboxHalfHeight = hitboxHalfHeight;
		
		// Calculate initial velocity
		rad = angle * (float)Math.PI / 180f;
		dx = (float)(v * Math.sin(rad));
		dy = (float)(v * Math.cos(rad));
	}// GOOD THINKING PHIL.
	
	// Canvas-specific drawing
	public abstract void draw(Canvas canvas);
	
	// Update coords, etc.
	// Synchronous frame by frame - no skipping!
	public void nextFrame() {
		// Next frame
		frame++;

		// Apply acceleration
		if (a != 0f) {
			v += a;
		}
		
		// Apply angular velocity
		if (angle_v != 0f) {
			// Recalculate angles
			angle_v *= 0.95f; // decay to allow for spiraling
			angle += angle_v;
			rad = angle * (float)Math.PI / 180f;
		
			// Recalculate x/y velocities
			dx = (float)(v * Math.sin(rad));
			dy = (float)(v * Math.cos(rad));
		}
		
		// Apply gravity
		if (gx != 0f || gy != 0f) {
			if (angle_v != 0f) { // If dx/dy was recalculated
				dgx += gx;
				dgy += gy;
				dx += dgx;
				dy += dgy;
			} else { // If no angular velocity
				dx += gx;
				dy += gy;
			}
		}
		
		// Update x/y coords
		x += dx;
		y += dy;
		
	}
	
	// Collision check
	/*
	public boolean hasCollided(DrawObject object) {
		return (object.x > x - hitboxHalfWidth &&
				object.x < x + hitboxHalfWidth &&
				object.y > y - hitboxHalfHeight &&
				object.y < y + hitboxHalfHeight);
	}
	*/
	
	// Call on collisions
	public abstract void onCollision(DrawObject object);
	
}

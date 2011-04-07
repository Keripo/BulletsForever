package com.bulletsforever.bullets;

import android.graphics.Canvas;
import java.util.*;

/**
 * This is the boss!
 * This should be instantiated by GameMain's setupWorld()
 * Only a single instance should exist per GameMain instance
 */
public class DrawObjectDynamicBoss extends DrawObject {
	
	public int health;
	public int level;
	
	public DrawObjectDynamicArm left, right, front;
	public int side_power, front_power, max_power;
	private int leftcount, rightcount, frontcount;
	
	private DrawWorld dw;
	private Random rand;
	
	private float maxX, minX, maxY, minY;
	
	public DrawObjectDynamicBoss(DrawWorld dw, int level, int side_power, int front_power) {
		super(dw, 
			Settings.screenWidth / 2, Settings.screenHeight / 8,
			0f,	0f, 0f, 0f, 0f, 0f, 
			Settings.screenWidth / 8, Settings.screenWidth / 8 //scale boss with screen size
			);

		this.bitmap = dw.bl.getBitmap(R.drawable.icon, this.drawOffsetX, this.drawOffsetY);
		
		this.dw = dw;
		this.rand = new Random();
		
		this.level = level;
		this.health = level * 400;
		
		// Calculate number of body parts for left/right and front
		this.max_power = (int)((Settings.screenHeight / 2 - 2 * this.hitboxHalfHeight) / this.hitboxHalfWidth); 
		/* this.side_power = side_power > this.max_power ? this.max_power : side_power; */
		this.side_power = side_power > 2 ? 2 : side_power; //temp, easier to implement
		this.front_power = front_power > this.max_power ? this.max_power : front_power;	
		
		// Calculate movement boundaries based on size including body parts
		switch(this.side_power) {
		case 0:		this.minX = this.hitboxHalfWidth; 		break;
		case 1:		this.minX = 2 * this.hitboxHalfWidth;	break;
		default:	this.minX = 3 * this.hitboxHalfWidth;	break; //due to scaling only two arm pieces can extend sideways
		}
		this.maxX = Settings.screenWidth - this.minX;
		this.minY = this.hitboxHalfHeight;
		this.maxY = Settings.screenHeight / 2 - (this.front_power + 1) * this.hitboxHalfHeight;
		
		this.makeArms();
	}	
	
	/* Helper method for constructor */
	private void makeArms() {
		
		DrawObjectDynamicArm prevl = null, prevr = null, prev = null;
		DrawObjectDynamicArm currl, currr, curr;
		
		// Left and right arms
		for (int i = side_power; i > 0; i--) {
			currl = new DrawObjectDynamicArm(this.dw, this, 
					-(this.hitboxHalfWidth + (i+1/2) * this.hitboxHalfWidth), 0);
			currl.child = prevl;
			prevl = currl;
			
			currr = new DrawObjectDynamicArm(this.dw, this, 
					this.hitboxHalfWidth + (i+1/2) * this.hitboxHalfWidth, 0);
			currr.child = prevr;
			prevr = currr;
		}
		
		// Front arm
		for (int j = front_power; j > 0; j--) {
			curr = new DrawObjectDynamicArm(this.dw,this,
					0, this.hitboxHalfHeight + (j+1/2) * this.hitboxHalfHeight);
			curr.child = prev;
			prev = curr;
		}
		
		this.left = prevl; 	this.leftcount = side_power;
		this.right = prevr;	this.rightcount = side_power;
		this.front = prev;	this.frontcount = front_power;
	}
	
	public void nextFrame() {
		super.nextFrame();
		
		// Correct for movement off-screen
		x = x > this.maxX ? this.maxX : x < this.minX ? this.minX : x;
		y = y > this.maxY ? this.maxY : y < this.minY ? this.minY : y; 
		
		// Chance of random movement every second (~60 frames) 
		if (rand.nextInt(60) == 0) { 
			this.v = (float)rand.nextDouble() * 5; //new random speed
			this.angle = rand.nextInt(360); //new random trajectory
			this.calcAngle();
		}
		
		// Fire a bullet
		if (rand.nextInt(120) == 0)
			this.fire();
		
		int destroyed_left = this.nextFrameArm(this.left),
			destroyed_right = this.nextFrameArm(this.right),
			destroyed_front = this.nextFrameArm(this.front);
		
		// Update movement boundaries depending on what's been destroyed
		this.minX -= destroyed_left * this.hitboxHalfWidth;
		this.leftcount -= destroyed_left;
		if (this.leftcount == 0) {
			this.left = null;
			if (this.front != null && this.right != null) 
				this.side_power++;
		}
		this.maxX += destroyed_right * this.hitboxHalfWidth;
		this.rightcount -= this.nextFrameArm(this.right);
		if (this.rightcount == 0) {
			this.right = null;
			if (this.front != null && this.left != null)
				this.side_power++;
		}
		this.maxY += destroyed_front * this.hitboxHalfHeight;
		this.frontcount -= this.nextFrameArm(this.front);
		if (this.frontcount == 0) {
			this.front = null;
			if (this.left != null && this.right != null)
				this.front_power++;
		}
	}	
	
	/* Helper function for nextFrame()
	 * Returns the number of parts that have been destroyed */
	private int nextFrameArm(DrawObjectDynamicArm part) {
		
		DrawObjectDynamicArm curr = part, prev = null;
		while (curr != null) {
			
			// Part not destroyed; fire bullet
			if (curr.health > 0) {
				if (rand.nextInt(60) == 0)
					curr.nextFrame(true);
				else	
					curr.nextFrame(false);
				prev = curr;
				curr = curr.child;
			}
			// Part destroyed; destroy all children
			else
			{
				if (prev != null)
					prev.child = null;
				return curr.numChildren() + 1;
			}
		}
		return 0;
	}
	
	private void fire() {
		
		// Calculate angle between this and player
		float dx = this.x - dw.player.x;
		float dy = Math.abs(this.y - dw.player.y);
		float angle;
		if (dy != 0)
			angle = (float)(Math.atan(dx/dy) * 180f / (float)Math.PI); 
		else {
			if (dx > 0) angle = 90;
			else if (dx == 0) angle = 0;
			else angle = 270;
		}
		
		dw.addBullet(new DrawObjectBullet(dw, 
				true, 
				this.x, this.y, 
				4f, 0f, 0f, 0f, angle, 0f
				));
	}
	
	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap, x - drawOffsetX, y - drawOffsetY, null);
		
		drawArm(this.left, canvas);
		drawArm(this.right, canvas);
		drawArm(this.front, canvas);
	}
	
	private void drawArm(DrawObjectDynamicArm part, Canvas canvas) {
		DrawObjectDynamicArm curr = part;
		while (curr != null) {
			curr.draw(canvas);
			curr = curr.child;
		}
	}

	public void onCollision(DrawObject object) {
		this.health--;	
		if (this.health == 0) {
			level++;        
			if (this.side_power == 0) 	this.side_power = 1;
			if (this.front_power == 0)	this.front_power = 1;
		}
	}
}

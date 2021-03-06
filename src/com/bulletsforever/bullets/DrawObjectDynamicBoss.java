package com.bulletsforever.bullets;

import android.graphics.Canvas;
import java.util.*;

/**
 * This is the boss!
 * This should be instantiated by GameMain's setupWorld()
 * Only a single instance should exist per GameMain instance
 */
public class DrawObjectDynamicBoss extends DrawObject {
	
	public enum Arm {
		LEFT, RIGHT, FRONT
	}
	
	public int health;
	public int level;
	public Arm next_evolution;
	
	public DrawObjectDynamicArm left, right, front;
	public int side_power, front_power, max_power;
	public boolean side_maxed, front_maxed;
	
	private DrawWorld dw;
	private Random rand;
	
	private float maxX, minX, maxY, minY;
	private float abs_maxX, abs_minX;
	
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
		this.health = level * 100;
		this.next_evolution = Arm.FRONT; //to avoid multi-leveling
		
		// Calculate number of body parts for left/right and front
		this.max_power = (int)((Settings.screenHeight / 2 - 2 * this.hitboxHalfHeight) / this.hitboxHalfWidth); 
		this.side_power = side_power > max_power ? max_power : side_power;
		this.front_power = front_power > this.max_power ? this.max_power : front_power;
		if (this.side_power == max_power) this.side_maxed = true;
		else this.side_maxed = false;
		if (this.front_power == this.max_power) this.front_maxed = true;
		else this.front_maxed = false;
		
		// Calculate movement boundaries based on size including body parts
		switch(this.side_power) {
		case 0:		this.minX = 2 * this.hitboxHalfWidth; 	break;
		case 1:		this.minX = 3 * this.hitboxHalfWidth;	break;
		default:	this.minX = 4 * this.hitboxHalfWidth;	break; //due to scaling only two arm pieces can extend sideways
		}
		this.maxX = Settings.screenWidth - this.minX;
		this.minY = this.hitboxHalfHeight;
		this.maxY = Settings.screenHeight / 2 - (this.front_power + 1) * this.hitboxHalfHeight;
		
		this.abs_minX = 2 * this.hitboxHalfWidth;
		this.abs_maxX = Settings.screenWidth - this.abs_minX;
		
		this.makeArms();
	}	
	
	/* Helper method for constructor */
	private void makeArms() {
		
		DrawObjectDynamicArm prevl = null, prevr = null, prev = null;
		DrawObjectDynamicArm currl, currr, curr;
		
		for (int i = side_power; i > 0; i--) {
			
			if (i <= 2) {
				currl = new DrawObjectDynamicArm(this.dw, this, 
						-(this.hitboxHalfWidth + (i-3/2) * this.hitboxHalfWidth), 0);
				currr = new DrawObjectDynamicArm(this.dw, this, 
						this.hitboxHalfWidth + (i-3/2) * this.hitboxHalfWidth, 0);
			}
			else {
				currl = new DrawObjectDynamicArm(this.dw, this,
						-((5/2) * this.hitboxHalfWidth), (i-2) * this.hitboxHalfHeight);
				currr = new DrawObjectDynamicArm(this.dw, this,
						(5/2) * this.hitboxHalfWidth, (i-2) * this.hitboxHalfHeight);
			}
			
			currl.child = prevl;
			prevl = currl;
			currr.child = prevr;
			prevr = currr;
		}
		for (int j = front_power; j > 0; j--) {
			curr = new DrawObjectDynamicArm(this.dw,this,
					0, this.hitboxHalfHeight + (j-3/2) * this.hitboxHalfHeight);
			curr.child = prev;
			prev = curr;
		}
		this.left = prevl;
		this.right = prevr;
		this.front = prev;
	}
	
	public void nextFrame() {
		super.nextFrame();
		
		// Correct for movement off-screen
		x = x > this.maxX ? this.maxX : x < this.minX ? this.minX : x;
		y = y > this.maxY ? this.maxY : y < this.minY ? this.minY : y; 
		
		// Chance of random movement every second (~60 frames) 
		if (rand.nextInt(60) == 0) { 
			this.v = (float)rand.nextDouble() * 5 * this.level; //new random speed
			this.angle = rand.nextInt(360); //new random trajectory
			this.calcAngle();
		}
		
		// Fire a bullet
		if (rand.nextInt(100) < this.level)
				this.fire();
		
		int destroyed_left = this.nextFrameArm(this.left, Arm.LEFT),
			destroyed_right = this.nextFrameArm(this.right, Arm.RIGHT),
			destroyed_front = this.nextFrameArm(this.front, Arm.FRONT);
		
		// Update movement boundaries & evolution 
		this.minX -= destroyed_left * this.hitboxHalfWidth;
		this.minX = this.minX < this.abs_minX ? this.abs_minX : this.minX;
		if (this.left == null && this.front != null && this.right != null) 
			this.next_evolution = Arm.LEFT;

		this.maxX += destroyed_right * this.hitboxHalfWidth;
		this.maxX = this.maxX > this.abs_maxX ? this.abs_maxX : this.maxX;
		if (this.right == null && this.front != null && this.left != null)
				this.next_evolution = Arm.RIGHT;

		this.maxY += destroyed_front * this.hitboxHalfHeight;
		if (this.front == null && this.left != null && this.right != null)
			this.next_evolution = Arm.FRONT;
	}	
	
	/* Helper function for nextFrame()
	 * Returns the number of parts that have been destroyed */
	private int nextFrameArm(DrawObjectDynamicArm part, Arm arm) {
		
		DrawObjectDynamicArm curr, prev = null;
		boolean maxed;
	
		switch (arm) {
		case LEFT: 
			curr = this.left;
			maxed = this.side_maxed;
			break;
		case RIGHT:
			curr = this.right;
			maxed = this.side_maxed;
			break;
		default:	
			curr = this.front;
			maxed = this.front_maxed;
			break;			
		}

		while (curr != null) {
			
			// Part not destroyed; fire bullet
			if (curr.health > 0) {
				
				boolean rings = false;
				if (maxed) rings = this.rand.nextBoolean();
				
				if (rand.nextInt(50) < this.level)
					curr.nextFrame(true, rings);
				else	
					curr.nextFrame(false, rings);
				prev = curr;
				curr = curr.child;
			}
			// Part destroyed; destroy all children
			else
			{
				if (prev != null)
					prev.child = null;
				else {
					switch (arm) {
					case FRONT:	this.front = null;	break;
					case LEFT: 	this.left = null;	break;
					default: 	this.right = null;
					}
				}
				return curr.destroyChildren() + 1;
			}
		}
		return 0;
	}
	
	private void fire() {
		
		// Max powered boss core shoots spirals
		if (this.front_maxed && this.side_maxed) {
			for (int i = 0; i < 360; i += 15) {
				DrawObjectBullet bullet = new DrawObjectBullet(
						dw, true, true,
						this.x, this.y,
						0f, 0.1f,
						0f, 0f,
						i, 10f
						);
				dw.addBullet(bullet);
			}
		}
		else {
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
					true, false,
					this.x, this.y, 
					4f, 0f, 0f, 0f, angle, 0f
					));
		}
	}
	
	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap, x - drawOffsetX, y - drawOffsetY, null);
		
		drawArm(this.left, canvas);
		drawArm(this.right, canvas);
		drawArm(this.front, canvas);
	}
	
	public void drawDebug(Canvas canvas) {
		super.drawDebug(canvas);
		drawArmDebug(this.left, canvas);
		drawArmDebug(this.right, canvas);
		drawArmDebug(this.front, canvas);
	}
	
	private void drawArm(DrawObjectDynamicArm part, Canvas canvas) {
		DrawObjectDynamicArm curr = part;
		while (curr != null) {
			curr.draw(canvas);
			curr = curr.child;
		}
	}
	
	private void drawArmDebug(DrawObjectDynamicArm part, Canvas canvas) {
		DrawObjectDynamicArm curr = part;
		while (curr != null) {
			curr.drawDebug(canvas);
			curr = curr.child;
		}
	}

	public void onCollision(DrawObject object) {
		if (this.left == null && this.right == null && this.front == null) {
			this.health--;	
			if (this.health == 0) {
				//release suicide bullets
				for (int i = 0; i < 360; i += 15) {
					DrawObjectBullet bullet = new DrawObjectBullet(
							dw, true, true,
							this.x, this.y,
							4f, 0f, 0f, 0f, i, 10f
							);
					dw.addBullet(bullet);
				}
				this.level++;
			}
		}
	}
	
}

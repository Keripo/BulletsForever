package com.bulletsforever.bullets;

import android.graphics.Canvas;
import java.util.*;

/**
 * This is the boss!
 * This should be instantiated by GameMain's setupWorld()
 * Only a single instance should exist per GameMain instance
 */
public class DrawObjectBoss extends DrawObject {
	
	public int health;
	public int level;
	public int num_turrets;
	private LinkedList<Turret> turrets;
	
	private DrawWorld dw;
	private Random rand;
	private float maxX, maxY;
	
	private class Turret {
		
		private float x;
		private float y;	
		
		private Turret(float x, float y) {
			this.x = x;
			this.y = y;
		}
		
		private void fire() {
			
			// Calculate angle between turret and player
			float dx = dw.boss.x - dw.player.x;
			float dy = dw.boss.y - dw.player.y;
			float angle;
			if (dx != 0)
				angle = (float)(Math.atan(dx/dy) * 180f / (float)Math.PI); 
			else {
				if (dy > 0) angle = 90;
				else if (dy == 0) angle = 0;
				else angle = 270;
			}
			
			dw.addBullet(new DrawObjectBullet(dw, 
					true, 
					dw.boss.x, dw.boss.y, 
					10f, 0f, 0f, 0f, angle, 0f
					));
		}
	}
	
	public DrawObjectBoss(DrawWorld dw, int level) {
		super(dw, 
			Settings.screenWidth / 2, Settings.screenHeight / 8,
			0f,	0f, 0f, 0f, 0f, 0f, 
			100f, 100f //not sure what would be a good size
			);
		
		this.bitmap = dw.bl.getBitmap(R.drawable.icon, hitboxHalfWidth, hitboxHalfHeight);
		this.dw = dw;
		this.rand = new Random();
		this.maxX = Settings.screenWidth - hitboxHalfWidth;
		this.maxY = Settings.screenHeight / 2;
		
		
		this.level = level;
		this.num_turrets = this.level * 8; //arbitrary 
		this.health = this.num_turrets * 100;  
		
		this.turrets = new LinkedList<Turret>();
		for (int i = 0; i < this.num_turrets; i++) {			
			// Randomize the order in which turrets are stored to avoid the turrets being destroyed in a predictable order
			if (rand.nextBoolean()) 
				this.turrets.addFirst(new Turret(this.x, this.y)); //how to decide where on the boss to position turrets?
			else
				this.turrets.addLast(new Turret(this.x, this.y));
		}
	}

	public void nextFrame() {
		super.nextFrame();
		
		// Correct for movement offscreen
		/*
		if (this.x < Settings.screenXMin)
			x = Settings.screenXMin;
		else if (this.x > Settings.screenXMax)
			x = Settings.screenXMax;
		if (this.y < Settings.screenYMin) 
			y = Settings.screenYMin;
		else if (this.y > (Settings.screenYMax / 2)) //boss must stay on top half of screen
			y = Settings.screenYMax / 2;
		*/
		x = x > maxX ? maxX : x < hitboxHalfWidth ? hitboxHalfWidth : x;
		y = y > maxY ? maxY : y < hitboxHalfHeight ? hitboxHalfHeight : y;
		
		// Chance of random movement every second (~60 frames) 
		if (rand.nextInt(60) == 0) { 
			this.v = (float)rand.nextDouble() * 5; //new random speed
			this.angle = rand.nextInt(360); //new random trajectory
			this.calcAngle();
		}
		
		// Fire bullets
		for (Turret t : this.turrets) 
			if (rand.nextBoolean())
				t.fire();
	}	
	
	@Override
	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap, x - hitboxHalfWidth, y - hitboxHalfHeight, null);
	}

	@Override
	public void onCollision(DrawObject object) {
		this.health--;
		
		if (this.health == 0)
			return; //evolution not implemented!!!
		
		// A random turret is destroyed every 10 health lost
		else if (this.health % 100 == 0 && !turrets.isEmpty()) {
			if (rand.nextBoolean())
				this.turrets.removeFirst();
			else
				this.turrets.removeLast();
		}
	}


}

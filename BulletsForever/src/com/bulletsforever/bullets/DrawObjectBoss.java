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
		private float offsetx;
		private float offsety;
		
		private Turret(float x, float y, float offsetx, float offsety) {
			this.offsetx = offsetx;
			this.offsety = offsety;
			this.x = x + offsetx;
			this.y = y + offsety;
		}
		
		private void fire() {
			
			// Calculate angle between turret and player
			float dx = this.x - dw.player.x;
			float dy = this.y - dw.player.y;
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
	}
	
	public DrawObjectBoss(DrawWorld dw, int level) {
		super(dw, 
			Settings.screenWidth / 2, Settings.screenHeight / 8,
			0f,	0f, 0f, 0f, 0f, 0f, 
			75f, 75f //not sure what would be a good size
			);
		//draw boss icon depending on level
		// Note: Image is bigger than hitbox on purpose (1.44x the size)
		this.level = level;
		this.drawOffsetX = hitboxHalfWidth * 1.2f;
		this.drawOffsetY = hitboxHalfHeight * 1.2f;
		switch(level) {
			default:
			case 1:
				bitmap = dw.bl.getBitmap(R.drawable.icon, drawOffsetX, drawOffsetY);
				break;
			case 2:
				bitmap = dw.bl.getBitmap(R.drawable.boss2f, drawOffsetX, drawOffsetY);
				break;
			case 3:
				bitmap = dw.bl.getBitmap(R.drawable.boss3f, drawOffsetX, drawOffsetY);
				break;
			case 4:
				bitmap = dw.bl.getBitmap(R.drawable.boss4f, drawOffsetX, drawOffsetY);
				break;
		}
		this.dw = dw;
		this.rand = new Random();
		this.maxX = Settings.screenWidth - hitboxHalfWidth;
		this.maxY = Settings.screenHeight / 2;
		
		
		this.level = level;
		this.num_turrets = this.level * 8; //arbitrary 
		this.health = this.num_turrets * 50;  //also arbitrary - game balance
		
		this.turrets = new LinkedList<Turret>();
		for (int i = 0; i < this.num_turrets; i++) {			
			// Randomize the order in which turrets are stored to avoid the turrets being destroyed in a predictable order
			if (rand.nextBoolean()) 
				this.turrets.addFirst(new Turret(this.x, this.y,
									10 * rand.nextInt(11),
									10 * rand.nextInt(11)));
			else
				this.turrets.addLast(new Turret(this.x, this.y,
									-10 * rand.nextInt(11),
									-10 * rand.nextInt(11)));
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
		x = x > maxX ? maxX : x < drawOffsetX ? drawOffsetY : x;
		y = y > maxY ? maxY : y < drawOffsetY ? drawOffsetY : y;
		
		
		// Chance of random movement every second (~60 frames) 
		if (rand.nextInt(60) == 0) { 
			this.v = (float)rand.nextDouble() * 5; //new random speed
			this.angle = rand.nextInt(360); //new random trajectory
			this.calcAngle();
		}
		
		// Update turret position and fire bullets
		for (Turret t : this.turrets) {
			t.x = this.x + t.offsetx;
			t.y = this.y + t.offsety;
			if (rand.nextInt(60) == 0)
				t.fire();
		}
	}	
	
	@Override
	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap, x - drawOffsetX, y - drawOffsetY, null);
	}

	@Override
	public void onCollision(DrawObject object) {
		this.health--;
		
		if (this.health == 0){
			level++;          //if health reaches 0, increment boss level
			return;
		}
		
		// A random turret is destroyed every 10 health lost
		else if (this.health % 100 == 0 && !turrets.isEmpty()) {
			if (rand.nextBoolean())
				this.turrets.removeFirst();
			else
				this.turrets.removeLast();
		}
	}
	public int getLevel(){
		return level;
	}


}

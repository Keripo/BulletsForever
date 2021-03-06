package com.bulletsforever.bullets;

import android.graphics.Canvas;

public class DrawObjectDynamicArm extends DrawObject {
	
	public DrawObjectDynamicBoss core;
	public float displacex, displacey;
	public int health;
	public DrawObjectDynamicArm child;
	
	private DrawWorld dw;

	public DrawObjectDynamicArm (DrawWorld dw, DrawObjectDynamicBoss core, float displacex, float displacey) {
		super(dw, 
			core.x + displacex, core.y + displacey,
			0f,	0f, 0f, 0f, 0f, 0f, 
			core.hitboxHalfWidth / 2, core.hitboxHalfHeight / 2 //half as large as boss core
			);
		
		this.bitmap = dw.bl.getBitmap(R.drawable.bossarm, this.drawOffsetX, this.drawOffsetY);
		
		this.core = core;
		this.displacex = displacex;
		this.displacey = displacey;
		this.health = this.core.health / 2;
		this.child = null;
		this.dw = dw;
	}
	
	public void nextFrame(boolean fire, boolean rings) {
		super.nextFrame();

		this.x = this.core.x + this.displacex;
		this.y = this.core.y + this.displacey;
		if (fire)
			this.fire(rings);
	}	
	
	private void fire(boolean rings) {
		
		if(!rings) {
			// Calculate angle between this and player  //For random direction: float angle = this.core.rand.nextInt(360);
			float dx = dw.player.x - this.x;
			float dy = Math.abs(dw.player.y - this.y); 
			float angle;
			if (dy != 0)
				angle = (float)(Math.atan(dx/dy) * 180f / (float)Math.PI); 
			else {
				if (dx > 0) angle = 90;
				else if (dx == 0) angle = 0;
				else angle = 270;
			}
		
			dw.addBullet(new DrawObjectBullet(dw, true, false,
					this.x, this.y, 
					4f, 0f, 0f, 0f, angle, 0f
					));
		}
		else {
			// Max powered arms shoot ring-pattern bullets
			for (int i = 0; i < 360; i += 36) {
				DrawObjectBullet bullet = new DrawObjectBullet(
						dw, true, true,
						this.x, this.y,
						0f, 0.1f, 0f, 0f, i, 10f
						);
				dw.addBullet(bullet);
			}
		}
	}
	
	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap, x - drawOffsetX, y - drawOffsetY, null);
	}

	public void onCollision(DrawObject object) {
		this.health--;
	}

	/* Returns number of children destroyed */
	public int destroyChildren() {
		// suicide bullets in ring-pattern
		for (int i = 0; i < 360; i += 15) {
			DrawObjectBullet bullet = new DrawObjectBullet(
					dw, true, true,
					this.x, this.y,
					4f, 0f, 0f, 0f, i, 10f
					);
			dw.addBullet(bullet);
		}
		AudioSoundPool sp = dw.sp;
		sp.play(dw.sfxBossArm);
		if (this.child == null) 
			return 0;
		return this.child.destroyChildren() + 1;
	}
}

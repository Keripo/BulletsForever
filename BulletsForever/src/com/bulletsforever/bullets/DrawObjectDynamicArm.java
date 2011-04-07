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
		
		this.bitmap = dw.bl.getBitmap(R.drawable.jms, this.drawOffsetX, this.drawOffsetY);
		
		this.core = core;
		this.displacex = displacex;
		this.displacey = displacey;
		this.health = this.core.health / 2;
		this.child = null;
		this.dw = dw;
	}
	
	public void nextFrame(boolean fire) {
		super.nextFrame();

		this.x = this.core.x + this.displacex;
		this.y = this.core.y + this.displacey;
		if (fire)
			this.fire();
	}	
	
	private void fire() {
		
		// Calculate angle between this and player
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
		
		dw.addBullet(new DrawObjectBullet(dw, 
				true, 
				this.x, this.y, 
				4f, 0f, 0f, 0f, angle, 0f
				));
	}
	
	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap, x - drawOffsetX, y - drawOffsetY, null);
		drawDebugCircle(canvas); // FIXME For debugging
	}

	public void onCollision(DrawObject object) {
		this.health--;
	}

	public int numChildren() {
		if (this.child == null)
			return 0;
		return this.child.numChildren() + 1;
	}
}

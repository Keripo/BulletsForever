package com.bulletsforever.bullets;

import java.util.Iterator;
import java.util.LinkedList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.view.KeyEvent;
import android.view.View;

/**
 * This is GameMain's custom View
 * This should be instantiated by GameMain's onCreate()
 * Only a single instance should exist per GameMain instance
 */
public class DrawWorld extends View {

	// For demostration purposes
	protected enum DemoMode {
		MOVE,
		RANDOM,
		EXPLOSION,
		FIREWORKS,
		SPIRALS,
		RINGS,
		ZOOM;
		
		// Next mode
		protected DemoMode next() {
			int i = (this.ordinal() + 1) % (DemoMode.values().length);
			return DemoMode.values()[i];
		}
	}
	
	// DrawWorld variables
	private DrawRefreshHandler refreshHandler;
	private DrawKeyHandler keyHandler;
	
	protected DemoMode mode;
	protected int frame;
	protected int collisionCount;
	
	// DrawObjects
	protected DrawBitmapLoader bl;
	protected DrawObjectHUD hud;
	protected DrawObjectPlayer player;
	/*protected DrawObjectBoss boss;*/
	protected DrawObjectDynamicBoss boss;
	protected LinkedList<DrawObjectBullet> player_bullets;
	protected LinkedList<DrawObjectBullet> boss_bullets;
	
	// Initializer
	public DrawWorld(Context c) {
		super(c);
		this.setFocusable(true);
		this.requestFocus();
		
		// Setup
		setupDraw();
		mode = DemoMode.MOVE;
		frame = 0;
		collisionCount = 0;
		
		// Handlers
		refreshHandler = new DrawRefreshHandler(this, Settings.getInt(R.string.refreshDelay));
		keyHandler = new DrawKeyHandler(this);
		setOnTouchListener(new DrawTouchHandler(this));
		
		// Start game!
		refreshHandler.start();
	}
	
	// Clear all bullets
	public void removeAllBullets() {
		player_bullets = new LinkedList<DrawObjectBullet>();
		boss_bullets = new LinkedList<DrawObjectBullet>();
		System.gc();
	}
	
	// Exit game
	public void onDestroy() {
		removeAllBullets();
		bl.onDestroy();
	}
	
	// Add new drawable objects
	public void addBullet(DrawObjectBullet bullet) {
		if (!bullet.boss)
			player_bullets.add(bullet);
		else
			boss_bullets.add(bullet);
	}
	
	// Called by initializer
	private void setupDraw() {
		bl = new DrawBitmapLoader(this.getContext());
		hud = new DrawObjectHUD(this);
		player = new DrawObjectPlayer(this);
		/*boss = new DrawObjectBoss(this, 1);*/
		boss = new DrawObjectDynamicBoss(this, 1, 1, 1);
		player_bullets = new LinkedList<DrawObjectBullet>();
		boss_bullets = new LinkedList<DrawObjectBullet>();
	}
	
	// Control
	public void startUpdating() {
		refreshHandler.start();
	}
	public void stopUpdating() {
		refreshHandler.stop();
	}
	
	// Called by onDraw
	// Synchronous frame by frame - no skipping!
	// If we want asynchronous, we will need to keep a timer
	// and potentially call nextFrame() multiple times per onDraw cycle
	private void nextFrame() {
		
		// Update bullets
		for (DrawObjectBullet bullet : player_bullets)	bullet.nextFrame();
		for (DrawObjectBullet bullet : boss_bullets)	bullet.nextFrame();
		
		// Update player
		player.nextFrame();
		
		// Update boss
		boss.nextFrame();
		
		// Update HUD
		hud.nextFrame();
		
		// Check for collisions
		checkCollisions();
	}
	
	// Called by update
	private void checkCollisions() {
		
		// Bullets with players
		float pxMin = player.x - player.hitboxHalfWidth;
		float pxMax = player.x + player.hitboxHalfWidth;
		float pyMin = player.y - player.hitboxHalfHeight;
		float pyMax = player.y + player.hitboxHalfHeight;
		
		for (DrawObjectBullet bullet : boss_bullets) {
			float bx = bullet.x;
			float by = bullet.y;
			if (!bullet.remove && bx > pxMin && bx < pxMax && by > pyMin && by < pyMax) { 
				player.onCollision(bullet);
				//bullet.onCollision(player); // Does nothing
				bullet.remove = true;
				collisionCount++;
			}
		}
		
		// Bullets with boss
		
		float boxMin, boxMax, boyMin, boyMax;
		
		/*for (DrawObjectBullet bullet: player_bullets)
			if (!bullet.remove) {
				float bx = bullet.x, by = bullet.y;
				boxMin = boss.x - boss.hitboxHalfWidth;
				boxMax = boss.x + boss.hitboxHalfWidth;
				boyMin = boss.y - boss.hitboxHalfHeight;
				boyMax = boss.y + boss.hitboxHalfHeight;
				if (bx > boxMin && bx < boxMax && by > boyMin && by < boyMax) {
					boss.onCollision(bullet);
					bullet.remove = true;
					collisionCount++;
					if (boss.health == 0) 
						boss = new DrawObjectBoss(this, boss.level);
				}
			}*/

		for (DrawObjectBullet bullet: player_bullets)
			if (!bullet.remove) 
				if (!checkCollisionArm(boss.left, bullet))
					if (!checkCollisionArm(boss.right, bullet))
						if (!checkCollisionArm(boss.front, bullet)) {
							float bx = bullet.x, by = bullet.y;
							boxMin = boss.x - boss.hitboxHalfWidth;
							boxMax = boss.x + boss.hitboxHalfWidth;
							boyMin = boss.y - boss.hitboxHalfHeight;
							boyMax = boss.y + boss.hitboxHalfHeight;
							if (bx > boxMin && bx < boxMax && by > boyMin && by < boyMax) {
								boss.onCollision(bullet);
								bullet.remove = true;
								collisionCount++;
								if (boss.health == 0) 
									boss = new DrawObjectDynamicBoss(this, boss.level, boss.side_power, boss.front_power);
							}
						}
						
								
		// Cleanup collided bullets or bullets off-screen
		// Use an iterator to prevent concurrency issues
		Iterator<DrawObjectBullet> it1 = player_bullets.iterator();
		while (it1.hasNext()) {
			DrawObjectBullet bullet = it1.next();
			if (bullet.remove) {
				it1.remove();
			}
		}
		Iterator<DrawObjectBullet> it2 = boss_bullets.iterator();
		while (it2.hasNext()) {
			DrawObjectBullet bullet = it2.next();
			if (bullet.remove) {
				it2.remove();
			}
		}
		
		// Player bullets with boss
		// TODO - Do something
		
	}
	
	/* Helper method for checkCollisions()
	 * Returns true if a collision happened */
	private boolean checkCollisionArm(DrawObjectDynamicArm part, DrawObjectBullet bullet) {
		float bx = bullet.x, by = bullet.y;
		float boxMin, boxMax, boyMin, boyMax;
		DrawObjectDynamicArm curr = part;

		while (curr != null) {
			boxMin = curr.x - curr.hitboxHalfWidth;
			boxMax = curr.x + curr.hitboxHalfWidth;
			boyMin = curr.y - curr.hitboxHalfHeight;
			boyMax = curr.y + curr.hitboxHalfHeight;
			
			if (bx > boxMin && bx < boxMax && by > boyMin && by < boyMax) {
				curr.onCollision(bullet);
				bullet.remove = true;
				collisionCount++;
				return true;
			}
			curr = curr.child;
		}
		return false;
	}
	
	// Called as a result of the refresh handler
	@Override
	protected void onDraw(Canvas canvas) {
		
		// Update everything
		frame++;
		nextFrame();
		
		// Clear screen by drawing background
		canvas.drawColor(Color.BLACK);
		
		// Draw boss
		boss.draw(canvas);
		
		// Draw bullets
		for (DrawObjectBullet bullet : player_bullets) 	bullet.draw(canvas);
		for (DrawObjectBullet bullet : boss_bullets) 	bullet.draw(canvas);

		// Draw player
		player.draw(canvas);
		
		// Draw HUD last
		hud.draw(canvas);
		
		// Game Over check
		if(player.health == 0){
			Paint textPaint = new Paint();
			textPaint.setColor(Color.WHITE);
			textPaint.setTextSize(Settings.screenWidth / 7); // Relative to screen width
			textPaint.setTextAlign(Align.CENTER);
			textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));
			canvas.drawText("GAME OVER", Settings.screenWidth / 2, Settings.screenHeight / 2, textPaint);
			// Stop game after this last draw
			stopUpdating();
		}
	}
	
	// Move this code to DrawKeyHandler for better code organization
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return keyHandler.onKeyDown(keyCode, event);
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return keyHandler.onKeyUp(keyCode, event);
	}
}

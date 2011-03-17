package com.bulletsforever.bullets;

import java.util.Iterator;
import java.util.LinkedList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
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
	protected DrawObjectHUD hud;
	protected DrawObjectPlayer player;
	protected LinkedList<DrawObjectBullet> bullets;
	
	// Initializer
	public DrawWorld(Context c) {
		super(c);
		this.setFocusable(true);
		this.requestFocus();
		
		// Setup
		setupDraw();
		mode = DemoMode.RANDOM;
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
		bullets = new LinkedList<DrawObjectBullet>();
		System.gc();
	}
	
	// Add new drawable objects
	public void addBullet(DrawObjectBullet bullet) {
		bullets.add(bullet);
	}
	
	// Called by initializer
	private void setupDraw() {
		hud = new DrawObjectHUD(this);
		player = new DrawObjectPlayer();
		bullets = new LinkedList<DrawObjectBullet>();
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
		for (DrawObjectBullet bullet : bullets) {
			bullet.nextFrame();
		}
		
		// Update player
		player.nextFrame();
		
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
		
		for (DrawObjectBullet bullet : bullets) {
			float bx = bullet.x;
			float by = bullet.y;
			if (!bullet.remove && //player.hasCollided(bullet)) {
				bx > pxMin && bx < pxMax && by > pyMin && by < pyMax) { // Faster
				player.onCollision(bullet);
				//bullet.onCollision(player); // Does nothing
				bullet.remove = true;
				collisionCount++;
			}
		}
		// Cleanup collided bullets or bullets off-screen
		// Use an iterator to prevent concurrency issues
		Iterator<DrawObjectBullet> it = bullets.iterator();
		while (it.hasNext()) {
			DrawObjectBullet bullet = it.next();
			if (bullet.remove) {
				it.remove();
			}
		}
		
		// Game Over check
		// TODO - Do something
		
		// Player bullets with boss
		// TODO - Do something
		
	}
	
	// Called as a result of the refresh handler
	@Override
	protected void onDraw(Canvas canvas) {
		
		// Update everything
		frame++;
		nextFrame();
		
		// Clear screen by drawing background
		canvas.drawColor(Color.BLACK);
		
		// Draw bullets
		for (DrawObjectBullet bullet : bullets) {
			bullet.draw(canvas);
		}
		
		// Draw player
		player.draw(canvas);
		
		// Draw HUD last
		hud.draw(canvas);
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

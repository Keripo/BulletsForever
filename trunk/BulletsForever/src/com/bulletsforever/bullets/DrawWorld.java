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

	// DrawWorld variables
	private DrawRefreshHandler refreshHandler;
	private DrawKeyHandler keyHandler;
	private int frame;
	protected int collisionCount;
	protected int targetBulletCount;
	
	// DrawObjects
	DrawObjectHUD hud;
	DrawObjectPlayer player;
	LinkedList<DrawObjectBullet> bullets;
	
	// Initializer
	public DrawWorld(Context c) {
		super(c);
		this.setFocusable(true);
		this.requestFocus();
		
		// Setup
		setupDraw();
		frame = 0;
		collisionCount = 0;
		targetBulletCount = 100;
		
		
		// Handlers
		refreshHandler = new DrawRefreshHandler(this, Settings.getInt(R.string.refreshDelay));
		keyHandler = new DrawKeyHandler(this);
		setOnTouchListener(new DrawTouchHandler(this));
		
		// Start game!
		refreshHandler.start();
	}
	
	// Add new drawable objects
	public void addBullet(DrawObjectBullet bullet) {
		bullets.add(bullet);
	}
	
	// For testing
	public void addRandomBullet() {
		DrawObjectBullet bullet = new DrawObjectBullet(
				(float)Math.random() * Settings.screenWidth,
				(float)Math.random() * Settings.screenHeight,
				(float)(Math.random() * 2f - 1.0f),
				(float)(Math.random() * 360)
				);
		addBullet(bullet);
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
		
		// TODO - for testing
		int addBulletCount = targetBulletCount - bullets.size();
		for (int i = 0; i < addBulletCount; i++) { 
			addRandomBullet();
		}
		
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
		for (DrawObjectBullet bullet : bullets) {
			if (!bullet.remove && player.hasCollided(bullet)) {
				player.onCollision(bullet);
				bullet.onCollision(player);
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

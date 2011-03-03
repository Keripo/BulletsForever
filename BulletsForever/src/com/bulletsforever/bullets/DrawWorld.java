package com.bulletsforever.bullets;

import java.util.Iterator;
import java.util.LinkedList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

/**
 * This is GameMain's custom View
 * This should be instantiated by GameMain's onCreate()
 * Only a single instance should exist per GameMain instance
 */
public class DrawWorld extends View {

	// DrawWorld variables
	private DrawRefreshHandler refreshHandler;
	private int frame;
	
	// DrawObjects
	GameObjectHUD hud;
	GameObjectPlayer player;
	LinkedList<GameObjectBullet> bullets;
	
	// Initializer
	public DrawWorld(Context c) {
		super(c);
		this.setFocusable(true);
		this.requestFocus();
		
		// Setup
		setupDraw();
		frame = 0;
		refreshHandler = new DrawRefreshHandler(this, Settings.getInt(R.string.refreshDelay));
		refreshHandler.start();
	}
	
	// Called by initializer
	private void setupDraw() {
		hud = new GameObjectHUD();
		player = new GameObjectPlayer();
		bullets = new LinkedList<GameObjectBullet>();
		
		// TODO - for testing
		for (int i = 0; i < 50; i++) {
			GameObjectBullet bullet = new GameObjectBullet(
					(float)Math.random() * Settings.screenWidth,
					(float)Math.random() * Settings.screenHeight,
					(float)(Math.random() - 0.5f) * 10f,
					(float)(Math.random() - 0.5f) * 10f
					);
			bullets.add(bullet);
		}
	}
	
	// Control
	public void startUpdating() {
		refreshHandler.start();
	}
	public void stopUpdating() {
		refreshHandler.stop();
	}
	
	// Add new drawable objects
	public void addBullet(GameObjectBullet bullet) {
		bullets.add(bullet);
	}
	
	// Called by onDraw
	// Synchronous frame by frame - no skipping!
	// If we want asynchronous, we will need to keep a timer
	// and potentially call nextFrame() multiple times per onDraw cycle
	private void nextFrame() {
		
		// TODO - for testing
		//if (frame % 2 == 0) {
			GameObjectBullet newBullet = new GameObjectBullet(
					(float)Math.random() * Settings.screenWidth,
					(float)Math.random() * Settings.screenHeight,
					(float)Math.random() * 5f,
					(float)Math.random() * 5f
					);
			bullets.add(newBullet);
		//}
		
		// Update bullets
		for (GameObjectBullet bullet : bullets) {
			bullet.nextFrame();
		}
		
		// Update player
		player.nextFrame();
		
		// Update HUD
		hud.bulletCount = bullets.size();
		hud.nextFrame();
		
		// Check for collisions
		checkCollisions();
	}
	
	// Called by update
	private void checkCollisions() {
		
		// Bullets with players
		for (GameObjectBullet bullet : bullets) {
			if (!bullet.remove && player.hasCollided(bullet)) {
				player.onCollision(bullet);
				bullet.onCollision(player);
				bullet.remove = true;
				hud.collisionCount++;
			}
		}
		// Cleanup collided bullets or bullets off-screen
		// Use an iterator to prevent concurrency issues
		Iterator<GameObjectBullet> it = bullets.iterator();
		while (it.hasNext()) {
			GameObjectBullet bullet = it.next();
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
		for (GameObjectBullet bullet : bullets) {
			bullet.draw(canvas);
		}
		
		// Draw player
		player.draw(canvas);
		
		// Draw HUD last
		hud.draw(canvas);
	}
}

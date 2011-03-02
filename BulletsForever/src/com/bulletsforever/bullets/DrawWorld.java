package com.bulletsforever.bullets;

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
	private void update(int frame) {
		
		// Update bullets
		for (GameObjectBullet bullet : bullets) {
			bullet.update(frame);
		}
		
		// Update player
		player.update(frame);
		
		// Update HUD
		hud.update(frame);
		
		// Check for collisions
		checkCollisions();
	}
	
	// Called by update
	private void checkCollisions() {
		
		// Bullets with players
		for (GameObjectBullet bullet : bullets) {
			if (player.hasCollided(bullet)) {
				player.onCollision(bullet);
				bullet.onCollision(player);
				bullets.remove(bullet);
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
		update(frame);
		
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

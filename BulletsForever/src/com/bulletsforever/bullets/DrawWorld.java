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
	
	// Called as a result of the refresh handler
	@Override
	protected void onDraw(Canvas canvas) {
		
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

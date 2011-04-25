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
	boolean bigCollision = false;
	
	
	protected DemoMode mode;
	protected int frame;
	protected int collisionCountBoss;
	protected int collisionCountPlayer;
	
	// DrawObjects
	protected DrawBitmapLoader bl;
	protected DrawObjectHUD hud;
	protected DrawObjectPlayer player;
	/*protected DrawObjectBoss boss;*/
	protected DrawObjectDynamicBoss boss;
	protected LinkedList<DrawObjectBullet> player_bullets;
	protected LinkedList<DrawObjectBullet> boss_bullets;
	protected boolean drawDebugHitboxes;
	
	// SoundPool
	protected AudioSoundPool sp;
	protected int sfxBoss, sfxGameOver;
	
	// Initializer
	public DrawWorld(Context c) {
		super(c);
		this.setFocusable(true);
		this.requestFocus();
		
		// Setup
		setupSound();
		setupDraw();
		mode = DemoMode.MOVE;
		frame = 0;
		collisionCountBoss = 0;
		collisionCountPlayer = 0;
		
		// Handlers
		refreshHandler = new DrawRefreshHandler(this, Settings.getInt(R.string.refreshDelay));
		keyHandler = new DrawKeyHandler(this);
		setOnTouchListener(new DrawTouchHandler(this));
		
		// Start game!
		refreshHandler.start();
	}
	
	// Setup Sound
	private void setupSound() {
		sp = new AudioSoundPool(getContext());
		sfxBoss = sp.load(R.raw.fanfare);
		sfxGameOver = sp.load(R.raw.oyasumi);
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
		sp.onDestroy();
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
		boss = new DrawObjectDynamicBoss(this, 1, 1, 1, 1);
		player_bullets = new LinkedList<DrawObjectBullet>();
		boss_bullets = new LinkedList<DrawObjectBullet>();
		drawDebugHitboxes = Settings.getBoolean(R.string.debugHitboxes);
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
		bigCollision = false;
		
		float boxMin, boxMax, boyMin, boyMax;
		
		
		float pxMin = player.x - player.hitboxHalfWidth;
		float pxMax = player.x + player.hitboxHalfWidth;
		float pyMin = player.y - player.hitboxHalfHeight;
		float pyMax = player.y + player.hitboxHalfHeight;
		
		boxMin = boss.x - boss.hitboxHalfWidth;
		boxMax = boss.x + boss.hitboxHalfWidth;
		boyMin = boss.y - boss.hitboxHalfHeight;
		boyMax = boss.y + boss.hitboxHalfHeight;
		
		
// Collision of boss with Player
		
		if(  (pxMax>boxMin && pxMin<boxMax)&& (pyMin<boyMax && pyMax>boyMin))
		{
			bigCollision = true;
			player.health -= 0.00000000000005;
		}
		
		
		
		
		// Bullets with player
		for (DrawObjectBullet bullet : boss_bullets) {
			float bx = bullet.x;
			float by = bullet.y;
			if (!bullet.remove && bx > pxMin && bx < pxMax && by > pyMin && by < pyMax) { 
				player.onCollision(bullet);
				//bullet.onCollision(player); // Does nothing
				bullet.remove = true;
				collisionCountPlayer++;
			}
		}
		
		// Bullets with boss
		
		
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
								collisionCountBoss++;
								if (boss.health == 0) {									
									switch (boss.next_evolution) {
									case LEFT:
										boss = new DrawObjectDynamicBoss(this, 
												boss.level, 
												boss.left_power+1, 
												boss.right_power,
												boss.front_power);
									case RIGHT:
										boss = new DrawObjectDynamicBoss(this, 
												boss.level, 
												boss.left_power, 
												boss.right_power+1,
												boss.front_power);
									default: 
										boss = new DrawObjectDynamicBoss(this, 
											boss.level, 
											boss.left_power,
											boss.right_power,
											boss.front_power+1);
										break;
									}
									sp.play(sfxBoss);
								}
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
				collisionCountBoss++;
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
		if(player.health <= 0){
			Paint textPaint = new Paint();
			textPaint.setColor(Color.argb(255, 238, 0, 0));
			textPaint.setTextSize(Settings.screenWidth / 7); // Relative to screen width
			textPaint.setTextAlign(Align.CENTER);
			textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));
			canvas.drawText("GAME OVER", Settings.screenWidth / 2, Settings.screenHeight / 2, textPaint);
			
		
			Paint textPaint2 = new Paint();
			textPaint2.setColor(Color.argb(255, 255, 165, 0));
			textPaint2.setTextSize(Settings.screenWidth / 20); // Relative to screen width
			textPaint2.setTextAlign(Align.CENTER);
			textPaint2.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));
			canvas.drawText("To restart game press 'back' button", Settings.screenWidth / 2 , (  (Settings.screenHeight / 2)+(Settings.screenWidth / 7)) , textPaint2);
				
				
				
			Paint textPaint3 = new Paint();
			textPaint3.setColor(Color.rgb(0, 205, 0));
			textPaint3.setTextSize(Settings.screenWidth / 20); // Relative to screen width
			textPaint3.setTextAlign(Align.CENTER);
			textPaint3.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));
			canvas.drawText("To restart level press 'menu' button", Settings.screenWidth / 2 , (  (Settings.screenHeight / 2)+(2*Settings.screenWidth / 7)) , textPaint3);
				
			

			
			
			
			
			
			
			// Stop game after this last draw
			sp.play(sfxGameOver);
			stopUpdating();
			

			
		}
		
		// Debug hitboxes
		if (drawDebugHitboxes) {
			boss.drawDebug(canvas);
			player.drawDebug(canvas);
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

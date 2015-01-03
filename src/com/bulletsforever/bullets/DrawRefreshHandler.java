package com.bulletsforever.bullets;

import android.os.Handler;
import android.os.Message;

/**
 * This is DrawWorld's refresh handler
 * This should be instantiated by DrawWorld's onCreate()
 * Only a single instance should exist per DrawWorld instance
 */
public class DrawRefreshHandler extends Handler {
	
	private long delay; // in milliseconds
	private DrawWorld dw;
	
	// Initializer
	public DrawRefreshHandler(DrawWorld dw, int delay) {
		this.dw = dw;
		this.delay = (long)delay;
	}
	
	// Delay
	private void sleep() {
		this.removeMessages(0);
		this.sendMessageDelayed(obtainMessage(0), delay);
	}
	// Update after delay
	public void handleMessage(Message msg) {
		this.update();
		this.sleep();
	}
	// Update function
	private void update() {
		dw.invalidate();
	}
	
	// Start updating
	public void start() {
		this.sleep();
	}
	// Stop updating
	public void stop() {
		this.removeCallbacksAndMessages(null);
	}
}

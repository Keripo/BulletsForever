package com.bulletsforever.bullets;

import android.os.SystemClock;

/**
 * This is for keeping track of the game's FPS
 * This should be instantiated by GameMain's onCreate()
 * Only a single instance should exist per GameMain instance
 */
public class GameFPSCounter {
	
	// Stuff
	private boolean fpsTotalStarted = false; // To account for inaccurate measurements during initial few frames
	private long fpsStartTime = 0;
	private long fpsTotalTime = 0;
	private int fpsFrameCount = 0;
	private int fpsFrameCountTotal = 0;
	private float fpsCalculated = 0.0f;
	private float fpsCalculatedTotal = 0.0f;
	private String fpsDisplayed = "";
	private String fpsDisplayedTotal = "";
	
	// Call this on every frame
	public void nextFrame() {
		// Calculate FPS based on elapsed time every 60 frames
		fpsFrameCount++;
		if (fpsFrameCount > 60) {
			// Recalculate everything
			long fpsCurrentTime = SystemClock.elapsedRealtime(); 
			fpsCalculated = ((float)(fpsFrameCount) / ((float)(fpsCurrentTime - fpsStartTime) / 1000f));
			if (fpsTotalStarted) {
				fpsFrameCountTotal += fpsFrameCount;
				fpsTotalTime += fpsCurrentTime - fpsStartTime;
				fpsCalculatedTotal = ((float)(fpsFrameCountTotal) / ((float)(fpsTotalTime) / 1000f));
			} else {
				if (fpsCalculated > 0 && fpsCalculated < 99) { // Sanity
					fpsTotalStarted = true;
				}
			}	
			fpsFrameCount = 0;
			fpsStartTime = SystemClock.elapsedRealtime();
			
			// Truncate displayed value to 2 decimal places
			fpsDisplayed = String.format("%.2f", fpsCalculated);
			fpsDisplayedTotal = String.format("%.2f", fpsCalculatedTotal);
		}
	}
	
	// Returns last FPS value
	public String getDisplayedFPS() { return fpsDisplayed; }
	public String getDisplayedTotalFPS() { return fpsDisplayedTotal; }
	public float getFPS() { return fpsCalculated; }
	public float getTotalFPS() { return fpsCalculatedTotal; }
}

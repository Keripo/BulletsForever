package com.bulletsforever.bullets;

import android.os.SystemClock;

/**
 * This is for keeping track of the game's FPS
 * This should be instantiated by GameMain's onCreate()
 * Only a single instance should exist per GameMain instance
 */
public class ToolsFPSCounter {
	
	// Stuff
	private boolean fpsTotalStarted; // To account for inaccurate measurements during initial few frames
	private long fpsStartTime;
	private long fpsTotalTime;
	private int fpsFrameCount;
	private int fpsFrameCountTotal;
	private float fpsCalculated;
	private float fpsCalculatedTotal;
	private String fpsDisplayed;
	private int updateFrequency; // number of frames
	
	public ToolsFPSCounter(int updateFrequency) {
		this.updateFrequency = updateFrequency;
		this.fpsDisplayed = "";
	}
	
	// Call this on every frame
	public void nextFrame() {
		// Calculate FPS based on elapsed time every n frames
		fpsFrameCount++;
		if (fpsFrameCount >= updateFrequency) {
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
			fpsDisplayed = String.format("FPS %.2f/%.2f", fpsCalculated, fpsCalculatedTotal);
		}
	}
	
	// Returns last FPS value
	public String getDisplayedFPS() { return fpsDisplayed; }
	public float getFPS() { return fpsCalculated; }
	public float getTotalFPS() { return fpsCalculatedTotal; }
}

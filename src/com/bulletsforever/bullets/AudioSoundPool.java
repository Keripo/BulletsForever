
package com.bulletsforever.bullets;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * This is for playing sound effects
 * This should be set up by GameMain
 */
public class AudioSoundPool {
	
	private SoundPool p;
	private Context c;
	
	// Constructor
	public AudioSoundPool(Context c, int numStreams) {
		this.c = c;
		p = new SoundPool(numStreams, AudioManager.STREAM_MUSIC, 0);
	}
	
	// Load a new wav file, returns a sound ID to reference
	public int load(int resId) {
		return p.load(c, resId, 1);
	}
	public int load(String musicFilePath) {
		return p.load(musicFilePath, 1);
	}
	
	// Control
	public void pauseAll() {
		p.autoPause();
	}
	public void resumeAll() {
		p.autoResume();
	}
	public void play(int soundId) {
		p.play(soundId, 1, 1f, 1, 0, 1);
	}
	public void playLoop(int soundId) {
		p.play(soundId, 1, 1f, 1, -1, 1);
	}
	public void pause(int soundId) {
		p.pause(soundId);
	}
	public void resume(int soundId) {
		p.resume(soundId);
	}
	public void stop(int soundId) {
		p.stop(soundId);
	}
	public void onDestroy() {
		p.release();
	}

}

package com.bulletsforever.bullets;

import android.content.Context;
import android.media.MediaPlayer;

public class AudioMusicPlayer {
	
	private MediaPlayer p;
	
	public boolean load(String musicFilePath) {
		try {
			if (p != null) {
				p.stop();
				p.release();
				p = null;
			}
			p = new MediaPlayer();
			p.setDataSource(musicFilePath);
			p.setLooping(true);
			p.prepare();
			return true;
		} catch (Exception e) {
			p = null;
			return false;
		}
	}
	
	public boolean load(Context c, int id) {
		try {
			if (p != null) {
				p.stop();
				p.release();
				p = null;
			}
			p = MediaPlayer.create(c, id); 
			p.setLooping(true);
			return true;
		} catch (Exception e) {
			p = null;
			return false;
		}
	}
	
	public int getCurrentPosition() {
		if (p != null) {
			return p.getCurrentPosition();
		} else {
			return 0;
		}
	}
	
	public boolean isPlaying() {
		return p != null && p.isPlaying();
	}
	
	public void start() {
		if (p != null) p.start();
	}
	
	public void pause() {
		if (p != null) p.pause();
	}
	
	public void onDestroy() {
		if (p != null) {
			p.stop();
			p.release();
			p = null;
		}
	}
}

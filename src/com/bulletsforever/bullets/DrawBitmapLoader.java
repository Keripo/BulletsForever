package com.bulletsforever.bullets;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseArray;

/**
 * This is for storing bitmap images
 * This should be instantiated by DrawWorld's initializer
 * Only a single instance should exist per GameMain instance
 */
public class DrawBitmapLoader {

	private Resources res;
	private SparseArray<Bitmap> bitmaps; 
	
	public DrawBitmapLoader(Context c) {
		res = c.getResources();
		bitmaps = new SparseArray<Bitmap>();
	}
	
	// Get a Bitmap to draw
	// Note: do not try to load the same image resource with different dimensions
	public Bitmap getBitmap(int id, float halfWidth, float halfHeight) {
		Bitmap b = bitmaps.get(id);
		if (b == null) {
			b = Bitmap.createScaledBitmap(
					BitmapFactory.decodeResource(res, id),
					(int)(halfWidth * 2), (int)(halfHeight * 2),
					true
					);
			bitmaps.put(id, b);
		}
		return b;
	}
	
	// Call this on game exit
	public void onDestroy() {
		//for (Bitmap b : bitmaps) { // SparseArray does not implement Iterable interface
		for (int i = 0; i < bitmaps.size(); i++) {
			bitmaps.valueAt(i).recycle(); // Manually call cause GC's stupid
		}
		bitmaps.clear();
	}
}

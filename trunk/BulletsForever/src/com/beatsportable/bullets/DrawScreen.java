package com.beatsportable.bullets;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

/**
 * Wrapper for our OpenGL View, with extras
 */

public class DrawScreen {

	private CustomGLView mGLView;
	
	// Setup mGLView and mRenderer
	public DrawScreen(Context c) {
		mGLView = new CustomGLView(c);
	}
	
	// for GameMain.setContentView(View v)
	public GLSurfaceView getView() {
		return mGLView;
	}
	
	// Call this from GameMain.onPause() 
	public void onPause() {
		mGLView.onPause();
	}
	
	// Call this from GameMain.onResume()
	public void onResume() {
		mGLView.onResume();
	}
	
	// Custom SurfaceView for our DrawScreen
	class CustomGLView extends GLSurfaceView {
		private CustomRenderer mRenderer;
		
		public CustomGLView(Context c) {
			super(c);
			mRenderer = new CustomRenderer();
			setRenderer(mRenderer);
		}
		
		// Call on touch events
		public boolean onTouchEvent(final MotionEvent event) {
			mGLView.queueEvent(new Runnable(){
				public void run() {
					// Do something here
					//mRenderer.doSomething(event.getX(), event.getY());
				}});
			return true;
		}
		
	}
	
	// Custom Renderer for our mGLView
	class CustomRenderer implements GLSurfaceView.Renderer
	{

		/*
		 * This is the main "update frame" method! See javadocs
		 */
		@Override
		public void onDrawFrame(GL10 gl) {
			// Clear screen
			gl.glClearColor(0f, 0f, 0f, 1.0f); // black
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
	        gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_REPLACE);
			
		}
		
		/*
		 * I _think_ this is called when the screen is rotated? See javadocs
		 */
		@Override
		public void onSurfaceChanged(GL10 gl, int w, int h) {
			gl.glViewport(0, 0, w, h);			
		}

		/*
		 * Do initial setup stuff here, see javadocs
		 */
		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			
			// Some OpenGL setup stuff that I copy-pasted
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
	        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
			
		}
		
	}
}

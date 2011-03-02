package com.beatsportable.bullets;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

/**
 * OpenGL is too complicated
 * I am a software engineer, not graphics programmer.
 * I don't want to bother learning this from scratch if no one else bothers even trying.
 * ~Phil, 2011/03/01
 * 
 * Wrapper for our OpenGL View, with extras
 */

@Deprecated
public class DrawScreen {

	private Context c;
	private CustomGLView mGLView;
	
	// Setup mGLView and mRenderer
	public DrawScreen(Context c) {
		this.c = c;
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
					
					// TODO - For testing purposes
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						mRenderer.randomizeBackgroundColour();
					}
				}});
			return true;
		}
		
	}
	
	// Custom Renderer for our mGLView
	class CustomRenderer implements GLSurfaceView.Renderer
	{
		/*
		 * This is for FPS testing
		 */
		private boolean showFPS;
		private DrawFPSCounter fpsCounter;
		//private DrawTextbox fpsTextbox;
		
		/*
		 * Do initial setup stuff here, see javadocs
		 */
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			
			// Some OpenGL setup stuff that I copy-pasted
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
			gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
			
			// TODO - For testing purposes
			randomizeBackgroundColour();
			//try {
				showFPS = true; // Disable in final version?
				fpsCounter = new DrawFPSCounter();
				//fpsTextbox = new DrawTextbox(DrawScreen.this.c, gl);
				//fpsTextbox.LoadFontAlt("fonts/HappyKiller.ttf", gl);
			//} catch (IOException e) {
				// blah
				//showFPS = false;
				//Log.e("BLAHHHH", e.getMessage());
			//}
			
		}
		
		/*
		 * I _think_ this is called when the screen is rotated? See javadocs
		 */
		public void onSurfaceChanged(GL10 gl, int w, int h) {
			gl.glViewport(0, 0, w, h);			
		}
		
		
		// TODO - For testing purposes
		private float bgR, bgG, bgB;
		public void randomizeBackgroundColour() {
			bgR = (float)Math.random();
			bgG = (float)Math.random();
			bgB = (float)Math.random();
		}
		
		/*
		 * This is the main "update frame" method! See javadocs
		 */
		public void onDrawFrame(GL10 gl) {
			
			// Clear screen
			//gl.glClearColor(0f, 0f, 0f, 1.0f); // black
			gl.glClearColor(bgR, bgG, bgB, 1.0f); // TODO - For testing purposes
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
	        gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_REPLACE);
			

			// Show FPS counter
			if (showFPS) {
				fpsCounter.nextFrame();
				
				// Draw values
				//fpsTextbox.PrintAt(gl, fpsCounter.getDisplayedFPS(), 50, 50);
			}
			
		}
		
	}
}

package com.techstorm.androidgame.framework.impl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.techstorm.androidgame.framework.Audio;
import com.techstorm.androidgame.framework.FileIO;
import com.techstorm.androidgame.framework.Game;
import com.techstorm.androidgame.framework.Graphics;
import com.techstorm.androidgame.framework.Input;
import com.techstorm.androidgame.framework.Screen;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;

public abstract class GLGame extends Activity implements Game, Renderer {
	enum GlGameState {
		Initialized, 
		Running, 
		Paused, 
		Finished, 
		Idle
	}
	
	GLSurfaceView glView;
	GLGraphics glGraphics;
	Audio audio;
	Input input;
	FileIO fileIO;
	Screen screen;
	GlGameState state = GlGameState.Initialized;
	Object stateChanged = new Object();
	long startTime = System.nanoTime();
	WakeLock wakeLock;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		glView = new GLSurfaceView(this);
		glView.setRenderer(this);
		setContentView(glView);
		
		glGraphics = new GLGraphics(glView);
		fileIO = new AndroidFileOI(getAssets());
		audio = new AndroidAudio(this);
		input = new AndroidInput(this, glView, 1, 1);
		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "GLGame");
	}
	
	public void onResume() {
		super.onResume();
		glView.onResume();
		wakeLock.acquire();
	}
	
	public void onPause() {
		synchronized (stateChanged) {
			if(isFinishing())
				state = GlGameState.Finished;
			else
				state = GlGameState.Paused;
			while(true) {
				try {
					stateChanged.wait();
					break;
				} catch(InterruptedException e) {
					
				}
			}
			wakeLock.release();
			glView.onPause();
			super.onPause();
		}
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub
		GlGameState state = null;
		
		synchronized (stateChanged) {
			state = this.state;
		}
		
		if(state == GlGameState.Running) {
			float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;
			startTime = System.nanoTime();
			
			screen.update(deltaTime);
			screen.present(deltaTime);
		}
		
		if(state == GlGameState.Paused) {
			screen.pause();
			synchronized (stateChanged) {
				this.state = GlGameState.Idle;
				stateChanged.notifyAll();
			}
		}
		
		if(state == GlGameState.Finished) {
			screen.pause();
			screen.dispose();
			
			synchronized (stateChanged) {
				this.state = GlGameState.Finished;
				stateChanged.notifyAll();
			}
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {

	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		glGraphics.setGL(gl);
		
		synchronized (stateChanged) {
			if(state == GlGameState.Initialized)
				screen = getStartScreen();
			state = GlGameState.Running;
			screen.resume();
			startTime = System.nanoTime();
		}
	}
	
	public GLGraphics getGLGraphics() {
		return glGraphics;
	}

	@Override
	public Input getInput() {
		// TODO Auto-generated method stub
		return input;
	}

	@Override
	public FileIO getFileIO() {
		// TODO Auto-generated method stub
		return fileIO;
	}

	@Override
	public Graphics getGraphics() {
		// TODO Auto-generated method stub
		throw new IllegalStateException("Dang su dung OpenGL");
	}

	@Override
	public Audio getAudio() {
		// TODO Auto-generated method stub
		return audio;
	}

	@Override
	public void setScreen(Screen screen) {
		// TODO Auto-generated method stub
		if(screen == null) {
			throw new IllegalArgumentException("Screen khong duoc null");
		}
		this.screen.pause();
		this.screen.dispose();
		screen.resume();
		screen.update(0);
		this.screen = screen;
	}

	@Override
	public Screen getCurrentScreen() {
		// TODO Auto-generated method stub
		return screen;
	}
}

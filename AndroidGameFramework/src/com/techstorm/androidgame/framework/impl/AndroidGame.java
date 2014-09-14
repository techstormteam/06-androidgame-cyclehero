package com.techstorm.androidgame.framework.impl;

import com.techstorm.androidgame.framework.Audio;
import com.techstorm.androidgame.framework.FileIO;
import com.techstorm.androidgame.framework.Game;
import com.techstorm.androidgame.framework.Graphics;
import com.techstorm.androidgame.framework.Input;
import com.techstorm.androidgame.framework.Screen;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Point;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

public abstract class AndroidGame extends Activity implements Game {
	AndroidFastRenderView renderView;
	Graphics graphics;
	Audio audio;
	Input input;
	FileIO fileIO;
	Screen screen;
	WakeLock wakeLock;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		boolean isLandscape = getResources().getConfiguration().orientation 
				== Configuration.ORIENTATION_LANDSCAPE;
		int frameBufferWidth = isLandscape ? 960 : 540;
		int frameBufferHeight = isLandscape ? 540 : 960;
		Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth, frameBufferHeight, Config.ARGB_8888);
		
		
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		float scaleX = (float) frameBufferWidth / width;
		float scaleY = (float) frameBufferHeight / height;
		
		renderView = new AndroidFastRenderView(this, frameBuffer);
		graphics = new AndroidGraphics(getAssets(), frameBuffer);
		fileIO = new AndroidFileOI(getAssets());
		audio = new AndroidAudio(this);
		input = new AndroidInput(this, renderView, scaleX, scaleY);
		screen = getStartScreen();
		setContentView(renderView);
		
		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "GLGame");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		wakeLock.acquire();
		screen.resume();
		renderView.resume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		wakeLock.release();
		renderView.pause();
		screen.pause();
		
		if(isFinishing()) {
			screen.dispose();
		}
	}
	
	@Override
	public Input getInput() {
		return input;
	}
	
	@Override
	public FileIO getFileIO() {
		return fileIO;
	}
	
	@Override
	public Graphics getGraphics() {
		return graphics;
	}
	
	@Override
	public Audio getAudio() {
		return audio;
	}
	
	@Override
	public void setScreen(Screen screen) {
		if(screen == null) {
			throw new IllegalArgumentException("Screen khong duoc null");
		}
		this.screen.pause();
		this.screen.dispose();
		screen.resume();
		screen.update(0);
		this.screen = screen;
	}
}

package com.techstorm.androidgame.framework.impl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AndroidFastRenderView extends SurfaceView implements Runnable {
	AndroidGame game;
	Bitmap frameBuffer;
	Thread renderThread = null;
	SurfaceHolder holder;
	volatile boolean running = false;

	public AndroidFastRenderView(AndroidGame game, Bitmap frameBufer) {
		super(game);
		this.game = game;
		this.frameBuffer = frameBufer;
		this.holder = getHolder();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Rect dstrRect = new Rect();
		long startTime = System.nanoTime();
		while(running) {
			if(!holder.getSurface().isValid()) {
				continue;
			}
			float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;
			startTime = System.nanoTime();
			game.getCurrentScreen().update(deltaTime);
			game.getCurrentScreen().present(deltaTime);
			
			Canvas canvas =  holder.lockCanvas();
			canvas.getClipBounds(dstrRect);
			canvas.drawBitmap(frameBuffer, null, dstrRect, null);
			holder.unlockCanvasAndPost(canvas);
		}
	}
	
	public void resume() {
		running = true;
		renderThread = new Thread(this);
		renderThread.start();
	}
	
	public void pause() {
		running = false;
		while(true) {
			try {
				renderThread.join();
				break;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

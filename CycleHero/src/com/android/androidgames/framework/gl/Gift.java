package com.android.androidgames.framework.gl;

import java.util.Random;

class Gift {
	static final Random rand = new Random();
	public float x, y;
	float dirX, dirY;
	
	public Gift() {
		x = rand.nextFloat() * 540;
		y = rand.nextFloat() * 960;
		dirX = 50;
		dirY = 50;
	}
	
	public void update(float deltaTime) {
		x = x + dirX * deltaTime;
		y = y + dirY * deltaTime;
		
		if(x < 0) {
			dirX = -dirX;
			x = 0;
		}
		
		if(x > 540) {
			dirX = -dirX;
			x = 540;
		}
		
		if(y < 0) {
			dirY = -dirY;
			y = 0;
		}
		
		if(y > 960) {
			dirY = -dirY;
			y = 960;
		}
	}
}

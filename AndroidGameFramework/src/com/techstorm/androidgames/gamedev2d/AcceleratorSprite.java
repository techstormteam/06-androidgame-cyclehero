package com.techstorm.androidgames.gamedev2d;

import com.techstorm.androidgames.framework.math.Vector2;

public class AcceleratorSprite extends Sprite{
	public final Vector2 velocity;
	public final Vector2 accel;

	public AcceleratorSprite(float x, float y, float width, float height) {
		super(x, y, width, height);
		velocity = new Vector2();
		accel = new Vector2();
	}
}

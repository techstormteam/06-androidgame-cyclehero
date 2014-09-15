package com.techstorm.androidgames.gamedev2d;

import com.techstorm.androidgames.framework.math.Rectangle;
import com.techstorm.androidgames.framework.math.Vector2;

public abstract class Sprite {
	public final Vector2 position;
	public final Rectangle bounds;
	
	public Sprite(float x, float y, float width, float height) {
		this.position = new Vector2(x, y);
		this.bounds = new Rectangle(x - width/2, y - height/2, width, height);
	}
}

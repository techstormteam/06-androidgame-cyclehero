package com.android.androidgames.collisiontest;

import com.techstorm.androidgame.framework.Screen;
import com.techstorm.androidgame.framework.impl.GLGame;

public class MainActivity extends GLGame {

	@Override
	public Screen getStartScreen() {
		// TODO Auto-generated method stub
		return new CollisionScreen(this);
	}
}

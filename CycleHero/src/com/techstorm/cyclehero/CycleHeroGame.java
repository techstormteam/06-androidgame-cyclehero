package com.techstorm.cyclehero;

import com.techstorm.androidgames.framework.Screen;
import com.techstorm.androidgames.framework.impl.GLGame;



public class CycleHeroGame extends GLGame {

	public GameKernel gameKernel;
	
	@Override
	public Screen getStartScreen() {
		gameKernel = new GameKernel(this);
		return gameKernel.getStartScreen();
	}
}

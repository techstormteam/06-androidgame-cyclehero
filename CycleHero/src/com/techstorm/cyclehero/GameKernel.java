package com.techstorm.cyclehero;

import java.util.ArrayList;
import java.util.List;

import com.techstorm.androidgames.framework.Game;

public class GameKernel {

	public Game game;
	
	// properties: level, list of screen, obstacles, speed, hero.
	public int levelNumber = 1;
	public List<GameScreen> screens = new ArrayList<GameScreen>();
	public int screenIndex = 0;
	public int playerSpeed = 1;
	public Counter playerLives = new Counter();
	public Counter playerScores = new Counter();
	
	
	public GameKernel(Game game) {
		playerScores.reset();
		playerLives.reset();
		playerLives.increase(3);
		screens.add(new CollisionScreen(game));
	}
	
	public GameScreen getStartScreen() {
		screenIndex = 0;
		return screens.get(screenIndex);
	}
	
	
}

package com.techstorm.cyclehero;

import java.util.ArrayList;
import java.util.List;

import com.techstorm.androidgames.framework.Game;
import com.techstorm.androidgames.framework.Screen;
import com.techstorm.cyclehero.sprite.Hero;
import com.techstorm.cyclehero.sprite.Obstacle;

public abstract class GameScreen extends Screen {

	public Hero player;
	public List<Obstacle> obstacles = new ArrayList<Obstacle>();
	
	public GameScreen(Game game) {
		super(game);
	}
	
	// move left
	public void moveLeft() {
		
	}
	
	// move right
	public void moveRight() {
		
	}
	
}

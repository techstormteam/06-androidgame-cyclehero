package com.techstorm.cyclehero;

import com.techstorm.androidgames.gamedev2d.Sprite;

public class LivesIndicator extends Sprite {

    private final Counter numberOfLives;

    /**
     * @param numberOfLives
     *            numberOfLives
     */
    public LivesIndicator(
        Counter numberOfLives) {
    	super(100, 20, 200, 200);
        this.numberOfLives = numberOfLives;
    }
    
}

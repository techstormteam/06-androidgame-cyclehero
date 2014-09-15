package com.techstorm.cyclehero;

import com.techstorm.androidgames.gamedev2d.Sprite;

public class ScoreIndicator extends Sprite {

    private final Counter currentScore;

    /**
     * @param currentScore
     *            currentScore
     */
    public ScoreIndicator(
        Counter currentScore) {
    	super(300, 20, 300, 100);
        this.currentScore = currentScore;
    }

}
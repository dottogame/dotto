package com.dotto.cli.util.manager;

/**
 * The {@code Score} class is responsible for holding and keeping track of a player's score
 * throughout the game that he plays.
 * 
 * @author SoraKatadzuma
 */
public final class Score {
    public double noteCount = 0;

    /** The player's current combo. */
    public int Combo;

    /** The player's current score. */
    public double currentScore;

    /** The player's current accuracy. */
    public double currentAccuracy;

    public Score() {
        noteCount = 0;
        currentAccuracy = 100.0;
        currentScore = 0;
    }

    /**
     * Calculates the score based on the timing of the current {@code Beat}, combo, and current
     * score.
     * 
     * @return The score based on the timing of the current {@code Beat}, combo, and current score.
     */
    public void adjustScore(double acc) {
        // TODO
    }

    /**
     * Calculate the player accuracy based off the ms they were off by
     * 
     * @param ms
     * @return
     */
    public double calculateAccuracy(long ms) {
        // acc = 1.01^(-0.33 * x + 460) + 3
        double result = Math.pow(1.01, -0.33 * Math.abs(ms) + 460.0) + 3.0;
        return result <= 100.0 ? result : 100.0;
    }

    /**
     * Adjust the player's accuracy after clicking a {@code Beat}.
     * 
     * @param acc the accuracy of the click to adjust to
     * @return The calculated accuracy the player currently has.
     */
    public void adjustAccuracy(double acc) {
        if (noteCount == 0) currentAccuracy = acc;
        else currentAccuracy += ((acc - currentAccuracy) / noteCount);
        noteCount++;
    }

    /**
     * Reset the score manager (for recyling)
     */
    public void reset() {
        noteCount = 0;
        currentAccuracy = 100.0f;
    }
}

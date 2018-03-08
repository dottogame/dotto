package com.dotto.cli.util.manager;

/**
 * The {@code Score} class is responsible for holding and keeping track of a player's score
 * throughout the game that he plays.
 * 
 * @author SoraKatadzuma
 */
public final class Score {
    public int noteCount;

    /** The player's current combo. */
    public int Combo;

    /** The player's current score. */
    public int CurrentScore;

    /** The player's current accuracy. */
    public double currentAccuracy;

    public Score() {
        noteCount = 0;
        currentAccuracy = 100.0;
    }

    /**
     * Calculates the score based on the timing of the current {@code Beat}, combo, and current
     * score.
     * 
     * @return The score based on the timing of the current {@code Beat}, combo, and current score.
     */
    public int CalculateScore() {
        int result;
        return 0;
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
     * @param ms the miliseconds the click was off by
     * @return The calculated accuracy the player currently has.
     */
    public void adjust(double acc) {
        // new_acc = x * (n / (n - 1)) + acc / n
        currentAccuracy = currentAccuracy * (noteCount / (noteCount + 1))
            + acc / noteCount;
        noteCount++;
    }

    public void reset() {
        noteCount = 0;
        currentAccuracy = 100.0f;
    }
}

package com.dotto.cli.util.manager;

/**
 * The {@code Score} class is responsible for holding and keeping track of a player's
 * score throughout the game that he plays.
 * 
 * @author SoraKatadzuma
 */
public final class Score {
    /** The accuracy score the player achieved. */
    public static float ScoreAccuracy;
    /** The player's current combo. */
    public static int Combo;
    /** The player's current score. */
    public static int CurrentScore;
    /** The player's current accuracy. */
    private static float CurrentAccuracy = 100f;
    
    /**
     * Calculates the score based on the timing of the current
     * {@code Beat}, combo, and current score.
     * 
     * @return The score based on the timing of the current
     *      {@code Beat}, combo, and current score.
     */
    public static int CalculateScore() {
        int result;
        
        return 0;
    }
    
    /***/
    public static float CalculateScoreAccuracy(int ms) {
        float result;

        if (ms > 250) return 0;

        result = (float)Math.pow(ms - 650, 2) / 4000;

        return result <= 100f ? result : 100f;
    }
    
    /**
     * Calculates the player's accuracy after clicking a {@code Beat}.
     * 
     * @param beatCount The current number of beats that have played in the map.
     * @return The calculated accuracy the player currently has.
     */
    public static float CalculateAccuracy(int beatCount) {
        return CurrentAccuracy;
    }
}

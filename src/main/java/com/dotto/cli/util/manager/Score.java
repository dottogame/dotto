package com.dotto.cli.util.manager;

/**
 * The {@code Score} class is responsible for holding and keeping track of a player's
 * score throughout the game that he plays.
 * 
 * @author SoraKatadzuma
 */
public final class Score {
    /** The accuracy score the player achieved. */
    public static int AccuracyScore;
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
     * @param ms The time in milliseconds it took for the player to click the {@code Beat}.
     *      It should always be positive.
     * @return The score based on the timing of the current
     *      {@code Beat}, combo, and current score.
     */
    public static int CalculateScore(int ms) {
        AccuracyScore = (ms <= 75) ? 500 :
                (ms <= 115) ? 300 :
                (ms <= 150) ? 150 :
                (ms <= 300) ? 75 :
                (ms <= 500) ? 30 : 0;
        
        return AccuracyScore * Combo + CurrentScore;
    }
    
    /**
     * Calculates the player's accuracy after clicking a {@code Beat}.
     * 
     * @param noOfBeats The number of beats that have been played.
     * @return The calculated accuracy the player currently has.
     */
    public static float CalculateAccuracy(int noOfBeats) {
        int accReduction;
        
        accReduction = (AccuracyScore == 500) ? 0 :
                       (AccuracyScore == 300) ? 15 :
                       (AccuracyScore == 150) ? 30 :
                       (AccuracyScore == 75) ? 50 :
                       (AccuracyScore == 30) ? 75 :
                       (AccuracyScore == 0) ? 100 : 100;
        
        return CurrentAccuracy = CurrentAccuracy - (float)(accReduction / noOfBeats);
    }
}

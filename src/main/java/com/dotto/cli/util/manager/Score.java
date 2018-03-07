package com.dotto.cli.util.manager;

/**
 * The {@code Score} class is responsible for holding and keeping track of a player's
 * score throughout the game that he plays.
 * 
 * @author SoraKatadzuma
 */
public final class Score {    
    /**
     * Calculates the score based on the timing of the current
     * {@code Beat}, combo, and current score.
     * 
     * @param ms The time in milliseconds it took for the player to click the {@code Beat}.
     *      It should always be positive.
     * @param combo The current combo the player is holding.
     * @param currentScore The score the player is currently holding.
     * @return The score based on the timing of the current
     *      {@code Beat}, combo, and current score.
     */
    public static int CalculateScore(int ms, int combo, int currentScore) {
        int score;
        
        score = (ms <= 75) ? 500 :
                (ms <= 115) ? 300 :
                (ms <= 150) ? 150 :
                (ms <= 300) ? 75 :
                (ms <= 500) ? 30 : 0;
        
        return score * combo + currentScore;
    }
}

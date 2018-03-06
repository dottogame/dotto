package com.dotto.cli.util.asset;

import java.util.List;

/**
 * Type 1 {@code Beat} appears to be a note in the rhythm.
 * 
 * @author SoraKatadzuma
 */
public class Click extends Beat {
    /**
     * Constructs a new {@code Click Beat}.
     *
     * @param InitTimeStamp The time in milliseconds from the start of the map this beat should appear.
     * @param EndTimeStamp The time in milliseconds from the start of the map this beat should be clicked.
     * @param Positions The position this {@code Beat} appears at.
     */
    public Click(int InitTimeStamp, int EndTimeStamp, List<Integer> Positions) {
        super(InitTimeStamp, EndTimeStamp, 0, Positions);
    }
}

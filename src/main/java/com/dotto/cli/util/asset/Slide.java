package com.dotto.cli.util.asset;

import java.util.List;

/**
 * Type 2 {@code Beat} designed to appear to slide to the rhythm.
 * 
 * @author SoraKatadzuma
 */
public class Slide extends Beat {
    /**
     * Constructs a new {@code Slide Beat}.
     * 
     * @param InitTimeStamp The time in milliseconds from the start of the map this beat should appear.
     * @param EndTimeStamp The time in milliseconds from the start of the map this beat should be clicked.
     * @param Positions The positions this {@code Beat} appears at.
     */
    public Slide(int InitTimeStamp, int EndTimeStamp, List<Integer> Positions) {
        super(InitTimeStamp, EndTimeStamp, 1, Positions);
    }
}

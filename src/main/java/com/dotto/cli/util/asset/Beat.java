package com.dotto.cli.util.asset;

import java.util.List;

/**
 * Defines a single beat of any type.
 * 
 * @author SoraKatadzuma
 */
public abstract class Beat {
    /** The initial timestamp of this {@code Beat}. */
    private final int InitTimeStamp;
    /** The click timestamp of this {@code Beat}. */
    private final int EndTimeStamp;
    /** The type of {@code beat} this is. */
    private final int Type;
    /**
     * The position of this {@code Beat}. If this is a type 2 beat then it will have many positions.
     * Specifically type 1 beats will only have 2 values for position [x, y] where as a type 2
     * will have as little as 8 values for a position [sx, sy, ex, ey, c1x, c1y, c2x, c2y] and
     * type 2 beats could have many of these.
     */
    private final List<Integer> Positions;
    
    /**
     * Constructs a new instance of {@code Beat}.
     * 
     * @param InitTimeStamp The initial timestamp of this {@code Beat}.
     * @param EndTimeStamp The click timestamp of this {@code Beat}.
     * @param Type The type of {@code Beat} this is.
     * @param Positions The list of positions this {@code Beat} is located at.
     *          Type 2 beats use these positions for their curves.
     */
    protected Beat(int InitTimeStamp, int EndTimeStamp, int Type, List<Integer> Positions) {
        this.InitTimeStamp = InitTimeStamp;
        this.EndTimeStamp = EndTimeStamp;
        this.Type = Type;
        this.Positions = Positions;
    }
    
    /**
     * Gets the initial timestamp of this beat.
     * 
     * @return The first column (column 0) value, a.k.a initial timestamp.
     */
    public int GetTimeStamp() {
        return InitTimeStamp;
    }
    
    /**
     * Gets the click timestamp of this beat.
     * 
     * @return The second column (column 1) value, a.k.a click timestamp.
     */
    public int GetEndStamp() {
        return EndTimeStamp;
    }
    
    /**
     * Returns the Type of {@code Beat} this is.
     * 
     * @return The Type of {@code Beat} this is.
     */
    public int GetType() {
        return Type;
    }
    
    /**
     * Returns the array of integers that this {@code Beat} positions at.
     * Type 2 beats have much larger arrays because theirs reflect curve positions.
     * 
     * @return The array of integers that this {@code Beat} positions at.
     */
    public int[] GetPositions() {
        return Positions.stream().mapToInt(i -> i).toArray();
    }
}

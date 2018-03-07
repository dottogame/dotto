package com.dotto.cli.util.asset;

import java.util.List;

/**
 * Defines a single beat of any type.
 * 
 * @author SoraKatadzuma
 */
public abstract class Beat {
    public static final int CLICK = 0;
    public static final int SLIDE = 1;

    /** The initial timestamp of this {@code Beat}. */
    public final long InitTimestamp;

    /** The click timestamp of this {@code Beat}. */
    public final long ClickTimestamp;

    /** The type of {@code beat} this is. */
    public final int Type;

    /**
     * The position of this {@code Beat}. If this is a type 2 beat then it will have many positions.
     * Specifically type 1 beats will only have 2 values for position [x, y] where as a type 2 will
     * have as little as 8 values for a position [sx, sy, ex, ey, c1x, c1y, c2x, c2y] and type 2
     * beats could have many of these.
     */
    public final List<Integer> Positions;

    /**
     * Constructs a new instance of {@code Beat}.
     * 
     * @param InitTimeStamp The initial timestamp of this {@code Beat}.
     * @param ClickTimeStamp The click timestamp of this {@code Beat}.
     * @param Type The type of {@code Beat} this is.
     * @param Positions The list of positions this {@code Beat} is located at. Type 2 beats use
     *        these positions for their curves.
     */
    protected Beat(
        long InitTimeStamp, long ClickTimeStamp, int Type,
        List<Integer> Positions
    ) {
        this.InitTimestamp = InitTimeStamp;
        this.ClickTimestamp = ClickTimeStamp;
        this.Type = Type;
        this.Positions = Positions;
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
     * Returns the array of integers that this {@code Beat} positions at. Type 2 beats have much
     * larger arrays because theirs reflect curve positions.
     * 
     * @return The array of integers that this {@code Beat} positions at.
     */
    public int[] GetPositions() {
        return Positions.stream().mapToInt(i -> i).toArray();
    }

    /**
     * Inherited method.
     * 
     * @return The string form of a beat.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "[" + InitTimestamp + " : " + ClickTimestamp + " : " + Type + " "
            + Positions + "]";
    }
}

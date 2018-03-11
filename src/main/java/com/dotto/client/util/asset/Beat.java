package com.dotto.client.util.asset;

import java.util.ArrayList;
import java.util.List;

import com.dotto.client.util.Casteljau;

/**
 * Defines a single beat of any type.
 * 
 * @author SoraKatadzuma
 */
public class Beat {
    /** Easy use of Comparison. */
    public static final int CLICK = 0;
    /** Easy use of Comparison. */
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
    private final List<Integer> Positions;

    public List<float[]> sliderPoints;

    public final int x;
    public final int y;

    public final String color;

    /**
     * Constructs a new instance of {@code Beat}.
     * 
     * @param InitTimeStamp The initial timestamp of this {@code Beat}.
     * @param ClickTimeStamp The click timestamp of this {@code Beat}.
     * @param Type The type of {@code Beat} this is.
     * @param Positions The list of positions this {@code Beat} is located at. Type 2 beats use
     *        these positions for their curves.
     */
    public Beat(
        long InitTimeStamp, long ClickTimeStamp, String color, int Type,
        List<Integer> Positions
    ) {
        this.InitTimestamp = InitTimeStamp;
        this.ClickTimestamp = ClickTimeStamp;
        this.color = color;
        this.Type = Type;
        this.Positions = Positions;
        this.x = Positions.get(0);
        this.y = Positions.get(1);
        // bake slider points if slider
        if (Type == SLIDE) bakeSlide();
    }

    private void bakeSlide() {
        this.sliderPoints = new ArrayList<>();
        int curveCount = (Positions.size() - 2) / 6;
        float[] start = new float[2];
        float[] startc = new float[2];
        float[] endc = new float[2];
        float[] end = new float[2];
        for (int z = 0; z < curveCount; z++) {
            start[0] = Positions.get(z * 3);
            start[1] = Positions.get((z * 3) + 1);

            startc[0] = Positions.get((z * 3) + 2);
            startc[1] = Positions.get((z * 3) + 3);

            endc[0] = Positions.get((z * 3) + 4);
            endc[1] = Positions.get((z * 3) + 5);

            end[0] = Positions.get((z * 3) + 6);
            end[1] = Positions.get((z * 3) + 7);

            for (int t = 0; t < 1000; t += 100) {
                sliderPoints.add(
                    Casteljau.bezier(start, startc, endc, end, t / 999.0f)
                );
            }
        }
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
     * @return The string form of a
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "[" + InitTimestamp + " : " + ClickTimestamp + " : " + color
            + ":" + Type + ":" + x + "x" + y + "]";
    }
}
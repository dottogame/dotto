package com.dotto.cli.util.asset;

import java.awt.Point;

/**
 * Contains all the important map data for a {@code BeatMap}.
 * 
 * @author lite20 (Ephraim BilsON)
 * @author SoraKatadzuma
 */
public final class MapData {
    /** The name of this {@code BeatMap} edit. */
    public final String MapName;
    /** The number of click objects in this map. */
    public final int ClickCount;
    /** The number of slide objects in this map. */
    public final int SlideCount;
    /** The number of hops in this map. */
    public final int HopCount;
    /** The amount of acceleration in this map. */
    public final double acceleration;
    /** The path to this map. */
    public final String MapId;
    /** The distance in pixels from the leftmost note to the right most note **/
    public final Point bound;
    /** The colors in a map */
    public final String[] colors;

    /**
     * Constructs a new {@code MapData}.
     * 
     * @param MapName The name of the map.
     * @param ClickCount The number of clicks in this map.
     * @param SlideCount The number of slides in this map.
     * @param HopCount The number of hops in this map.
     * @param bound The position of this map in space.
     * @param acceleration The acceleration of this map.
     * @param MapId The map id.
     */
    public MapData(
        String MapName, int ClickCount, int SlideCount, int HopCount,
        Point bound, double acceleration, String MapId, String[] colors
    ) {
        this.MapName = MapName;
        this.ClickCount = ClickCount;
        this.SlideCount = SlideCount;
        this.HopCount = HopCount;
        this.acceleration = acceleration;
        this.MapId = MapId;
        this.bound = bound;
        this.colors = colors;
    }
}

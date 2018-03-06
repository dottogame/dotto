package com.dotto.cli.util.asset;

/**
 * Contains all the important map data for a {@code BeatMap}.
 * 
 * @author SoraKatadzuma
 */
public final class MapData {
    /** The name of this {@code BeatMap} edit. */
    private final String MapName;
    /** The number of click objects in this map. */
    private final int ClickCount;
    /** The number of slide objects in this map. */
    private final int SlideCount;
    /** The number of hops in this map. */
    private final int HopCount;
    /** The amount of acceleration in this map. */
    private final byte AccelerationSpeed;
    /** The path to this map. */
    private final String MapPath;

    /**
     * @param MapName
     * @param ClickCount
     * @param SlideCount
     * @param HopCount
     * @param AccelerationSpeed
     * @param MapPath
     */
    public MapData(
        String MapName, int ClickCount, int SlideCount, int HopCount,
        byte AccelerationSpeed, String MapPath
    ) {
        this.MapName = MapName;
        this.ClickCount = ClickCount;
        this.SlideCount = SlideCount;
        this.HopCount = HopCount;
        this.AccelerationSpeed = AccelerationSpeed;
        this.MapPath = MapPath;
    }
}

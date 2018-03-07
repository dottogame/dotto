package com.dotto.cli.util.asset;

/**
 * Contains all the important map data for a {@code BeatMap}.
 * 
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
    public final byte AccelerationSpeed;
    /** The path to this map. */
    public final String MapId;

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
        byte AccelerationSpeed, String MapId
    ) {
        this.MapName = MapName;
        this.ClickCount = ClickCount;
        this.SlideCount = SlideCount;
        this.HopCount = HopCount;
        this.AccelerationSpeed = AccelerationSpeed;
        this.MapId = MapId;
    }
}

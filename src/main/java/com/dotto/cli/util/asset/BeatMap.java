package com.dotto.cli.util.asset;

/**
 * Contains all the necessary data for a {@code BeatMap}.
 * 
 * @author SoraKatadzuma
 */
public final class BeatMap {
    /** The original version of this map. */
    private final byte Version;
    /** The updated version of this map. */
    private final byte Revision;
    /** *  The {@code TrackData} data for this {@code BeatMap}. */
    private final TrackData TrackData;
    /** The maps that represent this {@code BeatMap}. */
    private final MapData[] Maps;
    
    /**
     * Constructs a new instance of a {@code BeatMap} with the given
     * {@code TrackData} data and the given maps.
     * 
     * @param Version The original version of this map.
     * @param Revision The updated version of this map.
     * @param TrackData The song information collected from the {@code index.json} file.
     * @param Maps The Individual maps collected from the {@code index.json} file.
     */
    public BeatMap(byte Version, byte Revision, TrackData TrackData, MapData[] Maps) {
        this.Version = Version;
        this.Revision = Revision;
        this.TrackData = TrackData;
        this.Maps = Maps;
    }
}

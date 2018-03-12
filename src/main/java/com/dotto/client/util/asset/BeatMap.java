package com.dotto.client.util.asset;

import java.util.ArrayList;

/**
 * Contains all the necessary data for a {@code BeatMap}.
 * 
 * @author SoraKatadzuma
 */
public final class BeatMap {
    /** The original version of this map. */
    public final byte Version;
    /** The updated version of this map. */
    public final byte Revision;
    /** * The {@code TrackData} data for this {@code BeatMap}. */
    public final TrackData TrackData;
    /** The maps that represent this {@code BeatMap}. */
    public final ArrayList<MapData> Maps;

    /**
     * Constructs a new instance of a {@code BeatMap} with the given {@code TrackData} data and the
     * given maps.
     * 
     * @param Version The original version of this map.
     * @param Revision The updated version of this map.
     * @param TrackData The song information collected from the {@code index.json} file.
     * @param arrayList The Individual maps collected from the {@code index.json} file.
     */
    public BeatMap(
        byte Version, byte Revision, TrackData TrackData,
        ArrayList<MapData> arrayList
    ) {
        this.Version = Version;
        this.Revision = Revision;
        this.TrackData = TrackData;
        this.Maps = arrayList;
    }
}

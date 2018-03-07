package com.dotto.cli.util.manager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dotto.cli.util.asset.BeatMap;
import com.dotto.cli.util.asset.MapData;
import com.dotto.cli.util.asset.TrackData;

/**
 * Parses and loads data about a beat map.
 * 
 * @author SoraKatadzuma
 */
public final class MapConfigure {
    /**
     * Takes the path to a map folder and returns the data inside the {@code index.json} file as a
     * {@code BeatMap} Object.
     * 
     * @param PathToMapFolder The path to the {@code BeatMap} folder.
     * @return A {@code BeatMap} object representing the contents of the {@code index.json} file.
     * @throws IOException
     */
    public static BeatMap MapFromFolder(String PathToMapFolder)
        throws IOException {
        Path path = Paths.get(PathToMapFolder + "/index.json");
        String mapIndex = new String(Files.readAllBytes(path));
        JSONObject object = new JSONObject(mapIndex);

        byte version = (byte) object.getInt("version");
        byte revision = (byte) object.getInt("revision");

        return new BeatMap(
            version, revision, TrackDataFrom(object), MapDataFrom(object)
        );
    }

    /**
     * Returns a {@code TrackData} object containing all the data about a {@code BeatMap}'s track.
     * 
     * @param jo The json object that we are collecting data from.
     * @return A {@code TrackData} object with all the {@code BeatMap}'s track data.
     */
    private static TrackData TrackDataFrom(JSONObject jo) {
        JSONObject track = jo.getJSONObject("track");

        String trackName, trackArtist, trackPath, source;
        byte bpm;

        trackName = track.getString("name");
        trackArtist = track.getString("artist");
        trackPath = track.getString("file");
        source = track.getString("source");
        bpm = (byte) track.getInt("bpm");

        JSONObject previewData = track.getJSONObject("preview");

        int[] preview = { previewData.getInt("start"),
            previewData.getInt("length") };

        return new TrackData(
            trackName, trackArtist, trackPath, source, bpm, preview
        );
    }

    /**
     * Returns a {@code MapData[]} object containing all the data about a {@code BeatMap}'s maps.
     * 
     * @param jo The json object that we are collecting data from.
     * @return A {@code MapData[]} object with all the {@code BeatMap}'s map data.
     */
    private static HashMap<String, MapData> MapDataFrom(JSONObject jo) {
        JSONArray maps = jo.getJSONArray("maps");
        HashMap<String, MapData> mapData = new HashMap<>();

        for (int i = 0; i < maps.length(); i++) {
            JSONObject jso = maps.getJSONObject(i);

            String mapName, id;
            int clicks, slides, hops;
            byte accelSpeed;

            mapName = jso.getString("name");
            clicks = jso.getInt("clicks");
            slides = jso.getInt("slides");
            hops = jso.getInt("hops");
            accelSpeed = (byte) jso.getInt("acceleration");
            id = jso.getString("id");

            mapData.put(
                id, new MapData(mapName, clicks, slides, hops, accelSpeed, id)
            );
        }

        return mapData;
    }
}

package com.dotto.client.util.manager;

import java.awt.Point;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dotto.client.util.Util;
import com.dotto.client.util.asset.BeatMap;
import com.dotto.client.util.asset.MapData;
import com.dotto.client.util.asset.TrackData;

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
     * @throws URISyntaxException
     */
    public static BeatMap MapFromFolder(String mapName)
        throws IOException, URISyntaxException {
        Path path = Paths
            .get(Util.getLocal() + "/maps/" + mapName + "/index.json");
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

        String trackName, trackArtist, source;
        byte bpm;

        trackName = track.getString("name");
        trackArtist = track.getString("artist");
        source = track.getString("source");
        bpm = (byte) track.getInt("bpm");

        JSONObject previewData = track.getJSONObject("preview");

        int[] preview = { previewData.getInt("start"),
            previewData.getInt("length") };

        return new TrackData(trackName, trackArtist, source, bpm, preview);
    }

    /**
     * Returns a {@code MapData[]} object containing all the data about a {@code BeatMap}'s maps.
     * 
     * @param jo The json object that we are collecting data from.
     * @return A {@code MapData[]} object with all the {@code BeatMap}'s map data.
     */
    private static ArrayList<MapData> MapDataFrom(JSONObject jo) {
        JSONArray maps = jo.getJSONArray("maps");
        ArrayList<MapData> mapData = new ArrayList<>();
        for (int i = 0; i < maps.length(); i++) {
            JSONObject jso = maps.getJSONObject(i);

            String mapName, id;
            int clicks, slides, hops;

            double accelSpeed;

            mapName = jso.getString("name");
            clicks = jso.getInt("clicks");
            slides = jso.getInt("slides");
            hops = jso.getInt("hops");
            JSONObject bjson = jso.getJSONObject("bound");
            Point bound = new Point(bjson.getInt("x"), bjson.getInt("y"));
            accelSpeed = jso.getDouble("acceleration");
            id = jso.getString("id");
            JSONArray clrs = jso.getJSONArray("colors");
            String[] colors = new String[clrs.length()];
            for (int z = 0; z < clrs.length(); z++)
                colors[z] = clrs.getString(z);

            mapData.add(
                new MapData(
                    mapName, clicks, slides, hops, bound, accelSpeed, id, colors
                )
            );
        }

        return mapData;
    }
}

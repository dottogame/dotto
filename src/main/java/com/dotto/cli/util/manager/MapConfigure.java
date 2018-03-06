package com.dotto.cli.util.manager;

import com.dotto.cli.util.asset.BeatMap;
import com.dotto.cli.util.asset.MapData;
import com.dotto.cli.util.asset.Track;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Parses and loads data about a beat map.
 * 
 * @author SoraKatadzuma
 */
public final class MapConfigure {
    /**
     * Takes the path to a map folder and returns the data inside the {@code index.json} file
     * as a {@code BeatMap} Object.
     * 
     * @param PathToMapFolder The path to the {@code BeatMap} folder.
     * @return A {@code BeatMap} object representing the contents of the {@code index.json} file.
     */
    public static BeatMap MapFromFolder(String PathToMapFolder) {
        // our Json object that we will read from.
        JSONObject object = new JSONObject(PathToMapFolder);
        // the original map version.
        byte version = (byte)object.getInt("version");
        // the revised map version.
        byte revision = (byte)object.getInt("revision");
        // return result
        return new BeatMap(version, revision, TrackDataFrom(object), MapDataFrom(object));
    }
    
    /**
     * Returns a {@code Track} object containing all the data about a {@code BeatMap}'s track.
     * 
     * @param jo The json object that we are collecting data from.
     * @return A {@code Track} object with all the {@code BeatMap}'s track data.
     */
    private static Track TrackDataFrom(JSONObject jo) {
        JSONObject track = jo.getJSONObject("track");
        
        String trackName, trackAuthor, trackPath, source;
        byte bpm;
        
        trackName = track.getString("name");
        trackAuthor = track.getString("author");
        trackPath = track.getString("file");
        source = track.getString("source");
        bpm = (byte)track.getInt("bpm");
        
        JSONArray arr = track.getJSONArray("preview");
        
        List<Integer> previewList = new ArrayList<>();
        
        arr.toList().forEach((o) -> {
            Integer i = (Integer)o;
            previewList.add(i);
        });
        
        int[] preview = previewList.stream().mapToInt(i -> i).toArray();
        
        return new Track(trackName, trackAuthor, trackPath, source, bpm, preview);
    }
    
    /**
     * Returns a {@code MapData[]} object containing all the data about a {@code BeatMap}'s maps.
     * 
     * @param jo The json object that we are collecting data from.
     * @return A {@code MapData[]} object with all the {@code BeatMap}'s map data.
     */
    private static MapData[] MapDataFrom(JSONObject jo) {
        JSONArray maps = jo.getJSONArray("maps");
        List<MapData> mapData = new ArrayList<>();
        
        maps.toList().forEach((map) -> {
            JSONObject jso = (JSONObject)map;
            String mapName, mapPath;
            int clicks, slides, hops;
            byte accelSpeed;
        
            mapName = jso.getString("name");
            clicks = jso.getInt("clicks");
            slides = jso.getInt("slides");
            hops = jso.getInt("hops");
            accelSpeed = (byte)jso.getInt("acceleration");
            mapPath = jso.getString("file");
            
            mapData.add(new MapData(mapName, clicks, slides, hops, accelSpeed, mapPath));
        });
        
        return mapData.toArray(new MapData[0]);
    }
}

package com.dotto.cli.util.manager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.jsfml.audio.Music;

/**
 * Audio manager responsible for loading and managing audio tracks
 * 
 * @author lite20 (Ephraim Bilson)
 *
 */
public class Audio {
    public final HashMap<String, Music> tracks;

    public Music track;

    /**
     * Initialize the Audio manager
     */
    public Audio() {
        tracks = new HashMap<>();
    }

    /**
     * Load track Using JSFML (mp3 not supported)
     * 
     * @param path
     * @throws IOException
     */
    public void load(String path) throws IOException {
        track = new Music();
        track.openFromFile(new File(path).toPath());
        tracks.put(path, track);
    }
}

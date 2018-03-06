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
    private final HashMap<String, Music> tracks;

    private String current;

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
        Music track = new Music();
        track.openFromFile(new File(path).toPath());
        tracks.put(path, track);
    }

    /**
     * Unloads a track
     * 
     * @param path
     */
    public void unload(String path) {
        tracks.remove(path);
    }

    /**
     * Plays audio track. Only one can be played at a time (makes management easier as SFML has
     * limit of 256 tracks loaded at the same time)
     * 
     * @param path
     */
    public void play(String path) {
        if (current != null) tracks.get(current).stop();
        tracks.get(path).play();
    }

    /**
     * Stop currently playing track
     */
    public void stop() {
        tracks.get(current).stop();
    }

    /**
     * Pauses currently playing track
     */
    public void pause() {
        tracks.get(current).pause();
    }

    /**
     * Sets whether currently playing track should loop or not
     * 
     * @param shouldLoop
     */
    public void loop(boolean shouldLoop) {
        tracks.get(current).setLoop(shouldLoop);
    }
}

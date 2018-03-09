package com.dotto.cli.util.asset;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * /// Class Description ///
 * 
 * @author lite20 (Ephraim Bilson)
 */
public class Audio {
    /** The audio clip this class will play. */
    public Clip clip;

    /**
     * Creates a new instance of {@code Audio}.
     * 
     * @param path The path to the audio file to play.
     */
    public Audio(String path) {
        try {
            AudioInputStream ais = AudioSystem
                .getAudioInputStream(new File(path));

            AudioFormat baseFormat = ais.getFormat();

            AudioFormat decodeFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16,
                baseFormat.getChannels(), baseFormat.getChannels() * 2,
                baseFormat.getSampleRate(), false
            );

            AudioInputStream dais = AudioSystem
                .getAudioInputStream(decodeFormat, ais);

            clip = AudioSystem.getClip();
            clip.open(dais);
        } catch (
            IOException | LineUnavailableException
            | UnsupportedAudioFileException ex
        ) {
            ex.printStackTrace();
        }
    }

    /**
     * Plays the audio clip it has.
     */
    public void play() {
        if (clip == null) return;
        stop();
        clip.setFramePosition(0);
        clip.start();
    }

    /**
     * Stops playing the audio clip.
     */
    public void stop() {
        if (clip.isRunning()) clip.stop();
    }

    /**
     * Closes the audio file.
     */
    public void close() {
        stop();
        clip.close();
    }
}

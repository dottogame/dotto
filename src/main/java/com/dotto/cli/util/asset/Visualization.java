package com.dotto.cli.util.asset;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Visualization {
    /** The audio clip this class will play. */
    public Clip clip;

    /**
     * Creates a new instance of {@code Audio}.
     * 
     * @param path The path to the audio file to play.
     */
    public Visualization(String path) {
        try {
            AudioInputStream xais = AudioSystem
                .getAudioInputStream(new File(path));

            AudioFormat baseFormat = xais.getFormat();

            AudioFormat decodeFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16,
                baseFormat.getChannels(), baseFormat.getChannels() * 2,
                baseFormat.getSampleRate(), false
            );

            AudioInputStream ais = AudioSystem.getAudioInputStream(
                decodeFormat, xais
            );

            /*
             * byte[] frame = new byte[16]; int i = 0; while (ais.read(frame) > 0) {
             * System.out.println("-"); for (int x = 0; x < frame.length; x++) {
             * System.out.println(frame[x]); } }
             */

        } catch (IOException | UnsupportedAudioFileException ex) {
            ex.printStackTrace();
        }
        // 1918448
    }
}

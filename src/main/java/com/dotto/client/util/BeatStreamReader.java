package com.dotto.client.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.dotto.client.util.asset.Beat;

/**
 * Reads a single {@code *.to} file for beats in it and returns them for as long as the caller
 * needs.
 * 
 * @author SoraKatadzuma
 */
public class BeatStreamReader {
    /** The scanner for this {@code BeatStreamReader}. */
    private Scanner scanner;
    /** Tells if the this reader has reached the end of the file. */
    private boolean EOF;
    /** The file that this reader will be reading. */
    private final File BeatmapFile;

    /**
     * Constructs a new instance of {@code BeatStreamReader}.
     * 
     * @param BeatmapFile The file this reader will be reading from.
     * @throws java.io.FileNotFoundException If the reader is unable to find the file.
     */
    public BeatStreamReader(File BeatmapFile) throws FileNotFoundException {
        this.BeatmapFile = BeatmapFile;
        scanner = new Scanner(BeatmapFile);
    }

    /**
     * Collects all the necessary data to return a {@code Beat} from the {@code BeatmapFile} this
     * class is reading.
     * 
     * @return The next beat in {@code BeatmapFile}.
     */
    public Beat GetNextBeat() {
        Beat result = null;
        List<String> tokens = new ArrayList<>();

        if (!scanner.hasNext()) {
            EOF = true;
            return null;
        }

        String line = scanner.nextLine();

        // Safety percaution
        try (Scanner lineScanner = new Scanner(line)) {
            while (lineScanner.hasNext())
                tokens.add(lineScanner.next());
        }

        // Collecting timestamp data.
        int initTimeStamp = Integer.parseInt(tokens.get(0));
        int clickTimeStamp = Integer.parseInt(tokens.get(1));
        String color = tokens.get(2);
        List<Integer> positions = new ArrayList<>();

        switch (tokens.get(3)) {
        // Click
        case "0":
            positions.add(Integer.parseInt(tokens.get(4)));
            positions.add(Integer.parseInt(tokens.get(5)));

            result = new Beat(
                initTimeStamp, clickTimeStamp, color, Beat.CLICK, positions
            );
            break;
        // Slider
        case "1":
            tokens.subList(4, tokens.size()).forEach(
                (token) -> {
                    positions.add(Integer.parseInt(token));
                }
            );

            result = new Beat(
                initTimeStamp, clickTimeStamp, color, Beat.SLIDE, positions
            );
            break;
        }

        return result;
    }

    /**
     * Tells if there is anything more to get from the {@code BeatmapFile}.
     * 
     * @return {@code false} if {@code EOF == true}.
     */
    public boolean HasNextBeat() {
        return !EOF;
    }

    /**
     * Resets the file {@code Scanner}'s position in the file if necessary.
     * 
     * @throws java.io.FileNotFoundException If the file does not exists or cannot be found.
     */
    public void reset() throws FileNotFoundException {
        scanner.close();
        scanner = new Scanner(BeatmapFile);
    }
}

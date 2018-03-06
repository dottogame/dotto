package com.dotto.cli.util;

import com.dotto.cli.util.asset.Beat;
import com.dotto.cli.util.asset.Click;
import com.dotto.cli.util.asset.Slide;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Reads a single {@code *.to} file for beats in it and returns them for as long as the caller needs. 
 * 
 * @author SoraKatadzuma
 */
public class BeatStreamReader {
    /** The file we will be retrieving data from. */
    private final File BeatmapFile;
    /** The scanner for this {@code BeatStreamReader}. */
    private final Scanner scanner;
    /** Tells if the this reader has reached the end of the file. */
    private boolean EOF;
    
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
     * Collects all the necessary data to return a {@code Beat} from the {@code BeatmapFile}
     * this class is reading.
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
        Scanner lineScanner = new Scanner(line);
        
        while (lineScanner.hasNext())
            tokens.add(lineScanner.next());
        
        int initTimeStamp = Integer.parseInt(tokens.get(0));
        int clickTimeStamp = Integer.parseInt(tokens.get(1));
        List<Integer> positions = new ArrayList<>();
        
        switch (tokens.get(2)) {
            // Click
            case "0":        
                positions.add(Integer.parseInt(tokens.get(3)));
                positions.add(Integer.parseInt(tokens.get(4)));                
                
                result = new Click(initTimeStamp, clickTimeStamp, positions);
                break;
            // Slider
            case "1":
                tokens.subList(3, tokens.size()).forEach((token) -> {
                    positions.add(Integer.parseInt(token));
                });
                
                result = new Slide(initTimeStamp, clickTimeStamp, positions);
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
}

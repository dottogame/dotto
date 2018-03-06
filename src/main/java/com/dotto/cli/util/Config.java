package com.dotto.cli.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONObject;

import com.dotto.cli.Core;

/**
 * Used to load and manage configuration file
 * 
 * @author lite20 (Ephraim Bilson)
 *
 */
public class Config {
    /**
     * Class responsible for loading game settings. Stores commonly accessed values in their own
     * variables for convenient naming and potential compiler optimization
     */

    public static int WIDTH = 720;
    public static int HEIGHT = 480;

    public static boolean FULLSCREEN = false;

    public static void load() throws IOException {
        Path path = Paths
            .get(Core.rootDirectory.getAbsolutePath() + "/data/config.json");
        String configContents = new String(Files.readAllBytes(path));
        JSONObject json = new JSONObject(configContents);
        WIDTH = json.getInt("width");
        HEIGHT = json.getInt("height");
        FULLSCREEN = json.getBoolean("fullscreen");
    }
}

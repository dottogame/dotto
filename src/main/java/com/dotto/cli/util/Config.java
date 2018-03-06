package com.dotto.cli.util;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

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

    public static int UP_KEY = KeyEvent.VK_UP;
    public static int DOWN_KEY = KeyEvent.VK_DOWN;
    public static int LEFT_KEY = KeyEvent.VK_LEFT;
    public static int RIGHT_KEY = KeyEvent.VK_RIGHT;

    public static ArrayList<Integer> TAP_KEYS = new ArrayList<>();

    public static void load() throws IOException {
        Path path = Paths.get(
            Core.rootDirectory.getAbsolutePath() + "/bin/data/config.json"
        );

        String configContents = new String(Files.readAllBytes(path));
        JSONObject json = new JSONObject(configContents);
        WIDTH = json.getInt("width");
        HEIGHT = json.getInt("height");
        FULLSCREEN = json.getBoolean("fullscreen");
    }
}

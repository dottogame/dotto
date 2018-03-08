package com.dotto.cli.util;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dotto.cli.Core;

/**
 * Class responsible for loading game settings. Stores commonly accessed values in their own
 * variables for convenient naming and potential compiler optimization.
 * 
 * <p>
 * <b>Used to load and manage configuration file.</b>
 * </p>
 * 
 * @author lite20 (Ephraim Bilson)
 * @author SoraKatadzuma
 */
public class Config {
    /** The configured width of the screen. */
    public static int WIDTH = 720;

    /** The configured height of the screen. */
    public static int HEIGHT = 480;

    /** Whether or not the screen should be full. */
    public static boolean FULLSCREEN = false;

    /** Whether or not to antialias **/
    public static boolean ANTIALIAS = true;

    public static int UP_KEY = KeyEvent.VK_UP;
    public static int DOWN_KEY = KeyEvent.VK_DOWN;
    public static int LEFT_KEY = KeyEvent.VK_LEFT;
    public static int RIGHT_KEY = KeyEvent.VK_RIGHT;

    public static ArrayList<Integer> TAP_KEYS = new ArrayList<>();

    public static float BACK_DIM;

    /**
     * Loads in the configuration file.
     * 
     * @throws IOException If the config.json file cannot be found.
     */
    public static void load() throws IOException {
        Path path = Paths
            .get(Core.rootDirectory.getAbsolutePath() + "/data/config.json");

        String configContents = new String(Files.readAllBytes(path));
        JSONObject json = new JSONObject(configContents);
        WIDTH = json.getInt("width");
        HEIGHT = json.getInt("height");
        FULLSCREEN = json.getBoolean("fullscreen");
        ANTIALIAS = json.getBoolean("antialias");
        BACK_DIM = json.getFloat("backDim");
        JSONObject controls = json.getJSONObject("controls");
        UP_KEY = controls.getInt("up");
        DOWN_KEY = controls.getInt("down");
        LEFT_KEY = controls.getInt("left");
        RIGHT_KEY = controls.getInt("right");
        JSONArray taps = controls.getJSONArray("tapKeys");
        TAP_KEYS.clear();
        for (int i = 0; i < taps.length(); i++)
            TAP_KEYS.add(taps.getInt(i));

    }
}

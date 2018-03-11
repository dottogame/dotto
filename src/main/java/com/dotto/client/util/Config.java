package com.dotto.client.util;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dotto.client.Core;

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
    public static int WIDTH;
    /** The configured height of the screen. */
    public static int HEIGHT;
    /** Half the width and height pre-computed */
    public static int HALF_WIDTH;
    public static int HALF_HEIGHT;
    /** Whether or not the screen should be full. */
    public static boolean FULLSCREEN = false;
    /** Whether or not to antialias. **/
    public static boolean ANTIALIAS = true;
    /** Represents the when the up key for the game is pressed. */
    public static int UP_KEY = KeyEvent.VK_UP;
    /** Represents the when the down key for the game is pressed. */
    public static int DOWN_KEY = KeyEvent.VK_DOWN;
    /** Represents the when the left key for the game is pressed. */
    public static int LEFT_KEY = KeyEvent.VK_LEFT;
    /** Represents the when the right key for the game is pressed. */
    public static int RIGHT_KEY = KeyEvent.VK_RIGHT;
    /** A list of clicking keys. */
    public static ArrayList<Integer> TAP_KEYS = new ArrayList<>();
    /** The amount of back light that should be applied to the game. */
    public static float BACK_DIM;
    /** The current skin name. */
    public static String SKIN;

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
        HALF_WIDTH = (int) (WIDTH / 2);
        HALF_HEIGHT = (int) (HEIGHT / 2);
        FULLSCREEN = json.getBoolean("fullscreen");
        ANTIALIAS = json.getBoolean("antialias");
        BACK_DIM = json.getFloat("backDim");
        SKIN = json.getString("skin");
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

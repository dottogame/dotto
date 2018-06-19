package com.dotto.client.ui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

/**
 * This class represents the single, and/or array, of image(s) that create a single graphic in the
 * game.
 * 
 * @author lite20 (Ephraim Bilson)
 * @author SoraKatadzuma
 */
public class Graphic {
    /** The image(s) that represents this {@code Graphic}. */
    private final ArrayList<Texture> frames;

    /** The name of this {@code Graphic}. */
    public final String name;

    /** Tells if this {@code Graphic} is animated. */
    public final boolean isAnimated;

    /** The amount of frames per second an animation should be played. */
    public final int framesTick;

    /** The current game frames count. */
    private static int currentFrame = 0;
 
    /**
     * Constructs a new {@code Graphic} class object with a {@code BufferedImage} as it's
     * representation.
     * 
     * @param path The path to an image that will be stored.
     * @throws java.io.IOException If the {@code Image} could not be read.
     */
    public Graphic(String path) throws IOException {
        String[] parts = path.split("/");
        this.name = parts[parts.length - 1];
        File imgFile = new File(path);
        if (imgFile.isDirectory()) {
            int x = imgFile.list().length;
            frames = new ArrayList<>(x);
            for (int i = 0; i < x - 1; i++)
                frames.add(
                    new Texture(imgFile.getAbsolutePath() + "\\" + i + ".png")
                );

            isAnimated = true;
        } else {
            frames = new ArrayList<>(1);
            imgFile = new File(path + ".png");
            frames.add(new Texture(imgFile.getAbsolutePath()));
            isAnimated = false;
        }

        framesTick = isAnimated ? getFrameTick(path) : 0;
    }

    /**
     * Gets the texture at the current frame and binds it
     */
    public void bind() {
        frames.get(0).bind();
    }

    /**
     * @return The height of the first frames.
     */
    public int getHeight() {
        return frames.get(0).height;
    }

    /**
     * @return The width of the first frame of specific edition
     */
    public int getWidth() {
        return frames.get(0).width;
    }

    /**
     * @param path The path to the animation.
     * @return The frames tick speed as assigned by the {@code anim.json} file.
     */
    public final int getFrameTick(String path) {
        int result = 0;

        try {
            Path anim = Paths.get(path, "anim.json");
            String contents = new String(Files.readAllBytes(anim));
            JSONObject jo = new JSONObject(contents);

            result = jo.getInt("frameTick");
        } catch (IOException ex) {
            Logger.getLogger(Graphic.class.getName())
                .log(Level.SEVERE, null, ex);
        }

        return result;
    }
}

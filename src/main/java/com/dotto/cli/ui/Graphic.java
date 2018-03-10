package com.dotto.cli.ui;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.json.JSONObject;

/**
 * This class represents the single, and/or array, of image(s) that create a single graphic in the
 * game.
 * 
 * @author lite20 (Ephraim Bilson)
 * @author SoraKatadzuma
 */
public class Graphic implements AutoCloseable {
    /** The image(s) that represents this {@code Graphic}. */
    private final ArrayList<BufferedImage> frame;
    /** A map of all tints for this graphic **/
    private final HashMap<String, ArrayList<BufferedImage>> tintMap;
    /** The name of this {@code Graphic}. */
    public final String name;
    /** Tells if this {@code Graphic} is animated. */
    private final boolean animated;
    /** The amount of frames per second an animation should be played. */
    public final int frameTick;
    /** The current game frame count. */
    private static int currentFrame = 0;

    /**
     * Constructs a new {@code Graphic} class object with a {@code BufferedImage} as it's
     * representation.
     * 
     * @param path The path to an image that will be stored.
     * @throws java.io.IOException If the {@code Image} could not be read.
     */
    public Graphic(String path) throws IOException {
        tintMap = new HashMap<>();
        String[] parts = path.split("/");
        this.name = parts[parts.length - 1];
        File imgFile = new File(path);
        boolean isDirectory = false;

        if (imgFile.isDirectory()) {
            int x = imgFile.list().length;
            frame = new ArrayList<>(x);

            for (int i = 0; i < x - 1; i++) {
                frame.add(ImageIO.read(new File(imgFile, i + ".png")));
            }

            isDirectory = true;
        } else {
            frame = new ArrayList<>(1);
            imgFile = new File(path + ".png");
            frame.add(ImageIO.read(imgFile));
        }

        animated = isDirectory;
        frameTick = animated ? getFrameTick(path) : 0;
    }

    /**
     * Returns the {@code BufferedImage} that represents this {@code Graphic}.
     * 
     * @return The {@code BufferedImage} that represents this {@code Graphic}.
     */
    public BufferedImage getBuffer() {
        return frame.get(0);
    }

    /**
     * @return The height of the first frame.
     */
    public int getHeight() {
        return frame.get(0).getHeight();
    }

    /**
     * @return The width of the first frame.
     */
    public int getWidth() {
        return frame.get(0).getWidth();
    }

    /**
     * @param path The path to the animation.
     * @return The frame tick speed as assigned by the {@code anim.json} file.
     */
    public final int getFrameTick(String path) {
        int result = 0;

        try {
            Path anim = Paths.get(path, "anim.json");
            String contents = new String(Files.readAllBytes(anim));
            JSONObject jo = new JSONObject(contents);

            result = jo.getInt("frameTick");
        } catch (IOException ex) {
            Logger.getLogger(Graphic.class.getName()).log(
                Level.SEVERE, null, ex
            );
        }

        return result;
    }

    /**
     * @return Whether or not this {@code Graphic} is animated.
     */
    public boolean isAnimated() {
        return animated;
    }

    /**
     * Inherited method.
     * 
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() throws Exception {}

    /**
     * Clears all the tints stored for this Graphic to save memory
     */
    public void clearTints() {
        tintMap.clear();
    }

    public BufferedImage getTint(String color) {
        // TODO use frame index
        if (tintMap.containsKey(color)) return tintMap.get(color).get(0);
        else {
            ArrayList<BufferedImage> tintFrames = new ArrayList<>();
            Color clr = Color.decode(color);
            for (int i = 0; i < frame.size(); i++)
                tintFrames.add(GraphKit.tintGrayMap(getBuffer(), clr));

            tintMap.put(color, tintFrames);
            // TODO use frame index
            return tintFrames.get(0);
        }
    }
}

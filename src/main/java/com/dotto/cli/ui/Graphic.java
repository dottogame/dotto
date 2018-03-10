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
    private final HashMap<String, ArrayList<BufferedImage>> frames;
    /** The name of this {@code Graphic}. */
    public final String name;
    /** Tells if this {@code Graphic} is animated. */
    private final boolean animated;
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

        ArrayList<BufferedImage> framesRaw;
        if (imgFile.isDirectory()) {
            int x = imgFile.list().length;
            framesRaw = new ArrayList<>(x);
            for (int i = 0; i < x - 1; i++) {
                framesRaw.add(ImageIO.read(new File(imgFile, i + ".png")));
            }

            animated = true;
        } else {
            framesRaw = new ArrayList<>(1);
            imgFile = new File(path + ".png");
            framesRaw.add(ImageIO.read(imgFile));
            animated = false;
        }

        frames = new HashMap<>();
        frames.put("raw", framesRaw);
        framesTick = animated ? getFrameTick(path) : 0;
    }

    /**
     * Returns the {@code BufferedImage} that represents this {@code Graphic}.
     * 
     * @return The {@code BufferedImage} that represents this {@code Graphic}.
     */
    public BufferedImage getBuffer() {
        return getBuffer("raw");
    }

    /**
     * Returns the {@code BufferedImage} that represents this {@code Graphic}.
     * 
     * @return The {@code BufferedImage} that represents this {@code Graphic}.
     */
    public BufferedImage getBuffer(String edition) {
        return frames.get(edition).get(0);
    }

    /**
     * @return The height of the first frames.
     */
    public int getHeight() {
        return getHeight("raw");
    }

    /**
     * @return The height of the first frames.
     */
    public int getHeight(String edition) {
        return frames.get(edition).get(0).getHeight();
    }

    /**
     * @return The width of the first frame.
     */
    public int getWidth() {
        return getWidth("raw");
    }

    /**
     * @return The width of the first frame of specific edition
     */
    public int getWidth(String edition) {
        return frames.get(edition).get(0).getWidth();
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

            result = jo.getInt("framesTick");
        } catch (IOException ex) {
            Logger.getLogger(Graphic.class.getName()).log(
                Level.SEVERE, null, ex
            );
        }

        return result;
    }

    public void rescale() {

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
     * Clears all the editions stored to save memory
     */
    public void clean() {
        ArrayList<BufferedImage> raw = frames.get("raw");
        frames.clear();
        frames.put("raw", raw);
    }

    /**
     * Creates a tinted edition derived from the raw edition
     * 
     * @param color
     * @return returns self for function chaining
     */
    public Graphic tint(String color, String edition_id) {
        return tint("raw", color, edition_id);
    }

    /**
     * Crates a tinted edition derived from a base edition
     * 
     * @param base_edition
     * @param color
     * @return returns self for function chaining
     */
    public Graphic tint(String base_edition, String color, String edition_id) {
        // clone the base edition
        ArrayList<BufferedImage> tintFrames = new ArrayList<>();

        // tint
        Color clr = Color.decode(color);
        for (int i = 0; i < frames.get(base_edition).size(); i++)
            tintFrames.add(
                GraphKit.tintGrayMap(frames.get(base_edition).get(i), clr)
            );

        frames.put(edition_id, tintFrames);
        return this;
    }

    public Graphic rescale(int w, int h, String edition_id) {
        return rescale("raw", w, h, edition_id);
    }

    public Graphic rescale(
        String base_edition, int w, int h, String edition_id
    ) {
        // clone the base edition
        ArrayList<BufferedImage> scaledFrames = new ArrayList<>();

        // rescale
        for (int i = 0; i < frames.get(base_edition).size(); i++)
            scaledFrames.add(
                GraphKit.qualityScale(frames.get(base_edition).get(i), w, h)
            );

        frames.put(edition_id, scaledFrames);
        return this;
    }
}

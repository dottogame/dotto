package com.dotto.cli.ui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * 
 * @author lite20 (Ephraim Bilson)
 * @author SoraKatadzuma
 */
public class Graphic {
    /** The image that represents this {@code Graphic}. */
    private final ArrayList<BufferedImage> frame;

    public final String name;

    /**
     * Constructs a new {@code Graphic} class object with a {@code BufferedImage} as it's
     * representation.
     * 
     * @param Image The image that represents this {@code Graphic}.
     * @throws java.io.IOException If the {@code Image} could not be read.
     */
    public Graphic(String path) throws IOException {
        String[] parts = path.split("/");
        this.name = parts[parts.length - 1];
        File imgFile = new File(path + '/');
        if (imgFile.isDirectory()) {
            int x = imgFile.list().length;
            frame = new ArrayList<>(x);
            for (int i = 0; i < x; i++) {
                frame.add(ImageIO.read(new File(path + '/' + i + ".png")));
            }
        } else {
            frame = new ArrayList<>(1);
            imgFile = new File(path + ".png");
            frame.add(ImageIO.read(imgFile));
        }
    }

    /**
     * Returns the {@code BufferedImage} that represents this {@code Graphic}.
     * 
     * @return The {@code BufferedImage} that represents this {@code Graphic}.
     */
    public BufferedImage getBuffer() {
        // TODO support animation
        return frame.get(0);
    }

    public int getHeight() {
        return frame.get(0).getHeight();
    }

    public int getWidth() {
        return frame.get(0).getWidth();
    }
}

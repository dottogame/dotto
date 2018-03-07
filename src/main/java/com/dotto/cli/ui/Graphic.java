package com.dotto.cli.ui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 
 * @author lite20 (Ephraim Bilson)
 * @author SoraKatadzuma
 */
public class Graphic {
    /** The image that represents this {@code Graphic}. */
    private final BufferedImage[] Image;
    /** The name of this {@code Graphic}. */
    private final String GraphicName;

    /**
     * Constructs a new {@code Graphic} class object with a {@code BufferedImage} as it's
     * representation.
     * 
     * @param Image The image that represents this {@code Graphic}.
     * @throws java.io.IOException If the {@code Image} could not be read.
     */
    public Graphic(File Image) throws IOException {
        BufferedImage buff = ImageIO.read(Image);
        GraphicName = Image.getName();
    }

    /**
     * Returns the {@code BufferedImage} that represents this {@code Graphic}.
     * 
     * @return The {@code BufferedImage} that represents this {@code Graphic}.
     */
    public BufferedImage getBuffer() {
        return Image;
    }

    /**
     * Inherited method.
     * 
     * @return The name of this {@code Graphic}.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return GraphicName;
    }

    public int getHeight() {
        return Image.getHeight();
    }
}

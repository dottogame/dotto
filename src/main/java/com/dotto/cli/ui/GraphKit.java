package com.dotto.cli.ui;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * /// Class Description ///
 * 
 * @author lite20 (Ephraim Bilson)
 */
public class GraphKit {

    public static HashMap<String, BufferedImage> tintMap = new HashMap<>();

    public static Element buildButton(
        String text, Graphic parts[], int x, int y, int w
    ) {
        // TODO create functions to stretch and combine graphics
        Graphic graph = null;

        return new Element(graph, x, y, w, graph.getHeight());
    }

    /**
     * Takes a grayscale image and blends it with a color while maintaining original alpha TODO:
     * Optimize
     * 
     * @param buff
     * @param clr
     * @return
     */
    public static BufferedImage tintGrayMap(BufferedImage buff, Color clr) {
        BufferedImage target = new BufferedImage(
            buff.getWidth(), buff.getHeight(), BufferedImage.TYPE_INT_ARGB
        );

        int width = target.getWidth();
        int height = target.getHeight();

        // pre-compute multipliers
        float red = clr.getRed() / 255;
        float green = clr.getGreen() / 255;
        float blue = clr.getBlue() / 255;

        // iterate over pixels
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = buff.getRGB(x, y);

                // extract argb values
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;

                // multiply values
                r = (int) (red * clr.getRed());
                g = (int) (green * clr.getGreen());
                b = (int) (blue * clr.getBlue());

                // place argb values
                p = (a << 24) | (r << 16) | (g << 8) | b;

                target.setRGB(x, y, p);
            }
        }

        return target;
    }
}

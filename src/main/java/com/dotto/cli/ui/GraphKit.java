package com.dotto.cli.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Utility class used to perform many operations for UI
 * 
 * @author lite20 (Ephraim Bilson)
 */
public class GraphKit {

    public static HashMap<String, BufferedImage> tintMap = new HashMap<>();

    public static RenderingHints qualityRescaleHints;

    public static void init() {
        qualityRescaleHints = new RenderingHints(
            RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON
        );

        qualityRescaleHints.put(
            RenderingHints.KEY_ALPHA_INTERPOLATION,
            RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY
        );

        qualityRescaleHints.put(
            RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BICUBIC
        );
    }

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

    public static BufferedImage qualityScale(BufferedImage src, int w, int h) {
        BufferedImage target = new BufferedImage(
            w, h, BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g = (Graphics2D) target.getGraphics();
        g.setRenderingHints(qualityRescaleHints);
        g.drawImage(src, 0, 0, w, h, null);
        g.dispose();
        return target;
    }
}

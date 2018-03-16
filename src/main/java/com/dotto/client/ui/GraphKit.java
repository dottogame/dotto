package com.dotto.client.ui;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;

/**
 * Utility class used to perform many operations for UI
 * 
 * @author lite20 (Ephraim Bilson)
 */
public class GraphKit {

    public static HashMap<String, BufferedImage> tintMap = new HashMap<>();

    public static RenderingHints qualityRescaleHints;

    public static void drawQuad(float x2, float y2, float x1, float y1) {
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2f(x1, y1);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2f(x2, y1);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2f(x2, y2);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2f(x1, y2);
        GL11.glEnd();
    }

    public static void drawQuad(Rectangle rect) {
        drawQuad(
            (float) rect.getX(), (float) rect.getY(),
            (float) (rect.getX() + rect.getWidth()),
            (float) (rect.getY() + rect.getHeight())
        );
    }
}

package com.dotto.client.ui;

import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * /// Class Description ///
 * 
 * @author lite20 (Ephraim Bilson)
 */
public class Element {
    private final Rectangle rect;
    private final Graphic graph;
    private final int x;
    private final int y;

    public Element(Graphic graph, int x, int y, int width, int height) {
        this.graph = graph;
        this.x = x;
        this.y = y;
        rect = new Rectangle();
        rect.setBounds(x, y, width, height);
    }

    public boolean inBounds(int x, int y) {
        return rect.contains(x, y);
    }

    public void draw(Graphics2D g) {
        g.drawImage(graph.getBuffer(), x, y, null);
    }

}

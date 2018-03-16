package com.dotto.client.ui;

import java.awt.Rectangle;

/**
 * /// Class Description ///
 * 
 * @author lite20 (Ephraim Bilson)
 */
public class Element {
    private final Rectangle rect;
    private final Graphic graph;

    public Element(Graphic graph, int x, int y, int width, int height) {
        this.graph = graph;
        rect = new Rectangle();
        rect.setBounds(x, y, width, height);
    }

    public boolean inBounds(int x, int y) {
        return rect.contains(x, y);
    }

    public void draw() {
        graph.bind();
        GraphKit.drawQuad(rect);
    }
}

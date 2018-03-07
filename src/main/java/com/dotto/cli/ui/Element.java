package com.dotto.cli.ui;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Element {

    private final Rectangle rect;

    private Graphic graph;

    private int x;
    private int y;

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

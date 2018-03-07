package com.dotto.cli.ui;

public class Generator {

    public Element buildButton(
        String text, Graphic parts[], int x, int y, int w
    ) {
        // TODO create functions to stretch and combine graphics
        Graphic graph = null;

        return new Element(graph, x, y, w, graph.getHeight());
    }

}

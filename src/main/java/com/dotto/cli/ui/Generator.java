package com.dotto.cli.ui;

/**
 * /// Class Description ///
 * 
 * @author lite20 (Ephraim Bilson)
 */
public class Generator {

    public Element buildButton(
        String text, Graphic parts[], int x, int y, int w
    ) {
        // TODO create functions to stretch and combine graphics
        Graphic graph = null;

        return new Element(graph, x, y, w, graph.getHeight());
    }

}

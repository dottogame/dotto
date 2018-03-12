package com.dotto.client.view;

/**
 * TODO: write class description.
 * 
 * @author lite20 (Ephraim Bilson)
 * @author SoraKatadzuma
 */
public interface View {
    // returns unique identifier for view
    int getId();

    /**
     * Draws the {@code Graphic g}.
     * 
     * @param g The graphic to draw to the screen.
     */
    void draw();
}

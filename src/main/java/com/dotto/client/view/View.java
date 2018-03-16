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
     * Draws the view
     */
    void draw();

    /**
     * Updates the view
     */
    void update(float delta);
}

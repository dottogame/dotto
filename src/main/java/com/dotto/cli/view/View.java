package com.dotto.cli.view;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

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
    void draw(Graphics2D g);

    /**
     * Mouse up event
     * 
     * @param e
     */
    void mouseUp(MouseEvent e);

    /**
     * Mouse down event
     * 
     * @param e
     */
    void mouseDown(MouseEvent e);

    /**
     * Key down event
     * 
     * @param e
     */
    void keyDown(KeyEvent e);

    /**
     * Key up event
     * 
     * @param e
     */
    void keyUp(KeyEvent e);

    /**
     * Updates the game every frame.
     * 
     * @param delta
     */
    void update(double delta);
}

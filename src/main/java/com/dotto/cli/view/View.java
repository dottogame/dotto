package com.dotto.cli.view;

import java.awt.Graphics;
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
    void draw(Graphics g);

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
}

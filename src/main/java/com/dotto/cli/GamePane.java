package com.dotto.cli;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import com.dotto.cli.util.GameCall;
import com.dotto.cli.util.GameLoop;
import com.dotto.cli.view.Boot;
import com.dotto.cli.view.View;

/**
 * Drawing surface for views and view manager
 * 
 * @author lite20 (Ephraim Bilson)
 * @author SoraKatadzuma
 */
@SuppressWarnings("serial")
public class GamePane extends JPanel implements MouseListener, KeyListener {

    public boolean isRunning = true;

    public GameLoop renderLoop;
    public GameLoop updateLoop;

    /** Current active view in the game pane. */
    public View view;

    /**
     * Constructs a new {@code GamePane} object.
     */
    public GamePane() {
        view = new Boot();
        renderLoop = new GameLoop(
            new GameCall() {

                @Override
                public void call(double delta) {

            }
            }
        );

        updateLoop = new GameLoop(
            new GameCall() {

                @Override
                public void call(double delta) {
                    view.update(delta);
                }
            }
        );

        new Thread(renderLoop).start();
        new Thread(updateLoop).start();

    }

    /**
     * Inherited method.
     * 
     * @param g The {@code Graphics} being painted to the view.
     * @see javax.swing.JPanel#paintComponent(java.awt.Graphics)
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        view.draw(g);
    }

    /**
     * Inherited method.
     * 
     * @param e The event being performed.
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(MouseEvent e) {
        view.mouseDown(e);
    }

    /**
     * Inherited method.
     * 
     * @param e The event being performed.
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        view.mouseUp(e);
    }

    /**
     * Inherited method.
     * 
     * @param e The event being performed.
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    @Override
    public void keyPressed(KeyEvent e) {
        view.keyDown(e);
    }

    /**
     * Inherited method.
     * 
     * @param e The event being performed.
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
    @Override
    public void keyReleased(KeyEvent e) {
        view.keyUp(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}

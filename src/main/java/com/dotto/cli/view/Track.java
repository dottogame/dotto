package com.dotto.cli.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;

import com.dotto.cli.Core;
import com.dotto.cli.util.Config;
import com.dotto.cli.util.Util;

/**
 * TODO: write class description.
 * 
 * @author lite20 (Ephraim Bilson)
 * @author SoraKatadzuma
 */
public class Track implements View {
    /** A manual serial id, instead of the normal Java serailID. */
    public static int ID = 3;

    public static enum Direction {
        UP, DOWN, LEFT, RIGHT, STOPPED
    };

    public float xOffset = 0.0f;
    public float yOffset = 0.0f;

    public Direction dir = Direction.STOPPED;

    public Track() {

    }

    public void start() throws IOException {
        String path = Util.getAssetPath("./assets/dotto.wav");
        Core.audioManager.load(path);
        Core.audioManager.play(path);
    }

    /**
     * Gets the serial id of this object.
     * 
     * @return The serial id of this object.
     */
    @Override
    public int getId() {
        return ID;
    }

    /**
     * Draws the current {@code Graphics g} to the screen.
     * 
     * @param g The {@code Graphics} to draw.
     */
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Config.WIDTH, Config.HEIGHT);

        // draw grid
        g.setColor(Color.WHITE);
        for (int y = 0; y < Config.HEIGHT; y += 10) {
            for (int x = 0; x < Config.WIDTH; x += 10) {
                g.fillOval(x - 2, y - 2, 4, 4);
            }
        }
    }

    /**
     * Mouse up event handle
     * 
     * @param e
     */
    @Override
    public void mouseUp(MouseEvent e) {}

    /**
     * Mouse down event handle
     * 
     * @param e
     */
    @Override
    public void mouseDown(MouseEvent e) {}

    /**
     * Key down event handle
     * 
     * @param e
     */
    @Override
    public void keyDown(KeyEvent e) {
        if (e.getKeyCode() == Config.UP_KEY) dir = Direction.UP;
        else if (e.getKeyCode() == Config.DOWN_KEY) dir = Direction.DOWN;
        else if (e.getKeyCode() == Config.LEFT_KEY) dir = Direction.LEFT;
        else if (e.getKeyCode() == Config.RIGHT_KEY) dir = Direction.RIGHT;
        else if (Config.TAP_KEYS.contains(e.getKeyCode())) {

        }
    }

    /**
     * Key up event handle
     * 
     * @param e
     */
    @Override
    public void keyUp(KeyEvent e) {}
}

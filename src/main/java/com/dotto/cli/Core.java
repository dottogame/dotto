package com.dotto.cli;

import java.awt.Cursor;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JFrame;

import com.dotto.cli.util.Config;
import com.dotto.cli.util.Flagger;
import com.dotto.cli.util.Util;
import com.dotto.cli.util.manager.Graphics;
import com.dotto.cli.view.Track;

/**
 * Entry point of program
 *
 * @author lite20 (Ephraim Bilson)
 * @author SoraKatadzuma
 */
public class Core {
    /** The current window. */
    public static JFrame w;
    /** The current game pane in the window. */
    public static GamePane pane;
    /** The root directory of the application. */
    public static File rootDirectory;
    /** The graphics manager. */
    public static Graphics graphicManager;

    public static DisplayMode originalMode;

    public static GraphicsDevice vc;

    /**
     * Entry point of application.
     * 
     * @param args The command line arguments.
     */
    public static void main(String... args) {
        // needed to allow the game to draw in fullscreen mode
        System.setProperty("sun.java2d.noddraw", "true");
        Flagger.setFlags(args);

        try {
            rootDirectory = Util.getLocalDirectory();
            Config.load();
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            System.exit(0);
        }

        // instantiate graphic manager
        graphicManager = new Graphics();

        // build game pane and window
        pane = new GamePane();
        w = new JFrame("Dotto");
        w.add(pane);
        w.addKeyListener(pane);
        w.addMouseListener(pane);

        // Create a new blank cursor.
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
            new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB),
            new Point(0, 0), "blank cursor"
        );

        // Set the blank cursor to the JFrame.
        Core.w.getContentPane().setCursor(blankCursor);

        // launch in either fullscreen mode or normal mode
        if (Config.FULLSCREEN) {
            w.setUndecorated(true);
            vc = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice();
            if (
                !vc.isFullScreenSupported()
            ) { throw new UnsupportedOperationException(
                "Fullscreen mode is unsupported."
            ); }

            originalMode = vc.getDisplayMode();
            DisplayMode[] dm = vc.getDisplayModes();
            DisplayMode finalDM = dm[0];
            for (int i = 0; i < dm.length; i++) {
                if (dm[i].getRefreshRate() >= finalDM.getRefreshRate())
                    if (dm[i].getWidth() <= Config.WIDTH) finalDM = dm[i];
            }

            System.out.println(
                finalDM.getWidth() + ":" + finalDM.getHeight() + ":"
                    + finalDM.getRefreshRate()
            );

            vc.setFullScreenWindow(w);
            vc.setDisplayMode(finalDM);
            if (Config.FULLSCREEN) {
                Config.WIDTH = w.getWidth();
                Config.HEIGHT = w.getHeight();
            }
        }

        else {
            w.setVisible(true);
        }

        try {
            Track t = new Track(
                rootDirectory.getPath() + "/maps/still_snow", "tom_easy"
            );

            pane.view = t;
            t.start();
        } catch (IOException e) { // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void shutdown() {
        vc.setDisplayMode(originalMode);
        System.exit(0);
    }
}

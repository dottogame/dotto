package com.dotto.cli;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;

import com.dotto.cli.util.Config;
import com.dotto.cli.util.Flagger;
import com.dotto.cli.util.Util;
import com.dotto.cli.util.asset.Visualization;
import com.dotto.cli.util.manager.Graphics;
import com.dotto.cli.view.Track;

/**
 * Entry point of program.
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
    /** A thread factory for the game to use to schedule events. */
    public static final ScheduledThreadPoolExecutor THREAD_FACTORY = new ScheduledThreadPoolExecutor(
        2, Executors.defaultThreadFactory()
    );

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

        // Sets flags that the game will use to adjust how it runs.
        Flagger.setFlags(args);

        try {
            rootDirectory = Util.getLocalDirectory();
            Config.load();
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(Core.class.getName()).log(
                Level.WARNING, ex.getMessage(), ex
            );

            shutdown();
        }

        // instantiate graphic manager
        graphicManager = new Graphics();

        // Building the game window.
        pane = new GamePane();

        THREAD_FACTORY.schedule(
            pane.renderLoop, 1000 / pane.renderLoop.targetFps,
            TimeUnit.MILLISECONDS
        );
        THREAD_FACTORY.schedule(
            pane.updateLoop, 1000 / pane.updateLoop.targetFps,
            TimeUnit.MILLISECONDS
        );

        // Building the game frame.
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

            for (DisplayMode dm1 : dm) {
                if (dm1.getRefreshRate() >= finalDM.getRefreshRate()) {
                    if (dm1.getWidth() <= Config.WIDTH) {
                        finalDM = dm1;
                    }
                }
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
        } else {
            w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            w.setPreferredSize(new Dimension(Config.WIDTH, Config.HEIGHT));
            w.pack();
            w.setLocationRelativeTo(null);
            w.setResizable(false);
            w.setVisible(true);
        }

        try {
            Track t = new Track(
                rootDirectory.getPath() + "/maps/still_snow", "tom_easy"
            );

            new Visualization(
                rootDirectory.getPath() + "/maps/still_snow/track.ogg"
            );

            pane.view = t;
            t.start();
        } catch (IOException ex) {
            Logger.getLogger(Core.class.getName()).log(
                Level.WARNING, ex.getMessage(), ex
            );

            shutdown();
        }
    }

    /**
     * Closes the game upon request.
     */
    public static void shutdown() {
        THREAD_FACTORY.shutdown();
        vc.setDisplayMode(originalMode);
        System.exit(0);
    }
}

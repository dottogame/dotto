package com.dotto.cli;

import com.dotto.cli.util.Config;
import com.dotto.cli.util.Flagger;
import com.dotto.cli.util.Util;
import com.dotto.cli.util.manager.Graphics;
import com.dotto.cli.view.Track;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 * Entry point of program.
 *
 * @author lite20 (Ephraim Bilson)
 * @author SoraKatadzuma
 */
public class Core {
    /** The current window. */
    private static JFrame w;
    /** The current game pane in the window. */
    public static GamePane pane;
    /** The root directory of the application. */
    public static File rootDirectory;
    /** The graphics manager. */
    public static Graphics graphicManager;
    /** A thread factory for the game to use to schedule events. */
    public static final ScheduledThreadPoolExecutor THREAD_FACTORY =
            new ScheduledThreadPoolExecutor(2, Executors.defaultThreadFactory());

    /**
     * Entry point of application.
     * 
     * @param args The command line arguments.
     */
    public static void main(String... args) {
        // Sets flags that the game will use to adjust how it runs.
        Flagger.setFlags(args);

        try {
            rootDirectory = Util.getLocalDirectory();
            Config.load();
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(Core.class.getName())
                    .log(Level.WARNING, ex.getMessage(), ex);
            
            shutdown();
        }

        // instantiate managers
        graphicManager = new Graphics();

        // Building the game window.
        pane = new GamePane();
        
        THREAD_FACTORY.schedule(
            pane.renderLoop, 1000 / pane.renderLoop.targetFps, TimeUnit.MILLISECONDS
        );
        THREAD_FACTORY.schedule(
            pane.updateLoop, 1000 / pane.updateLoop.targetFps, TimeUnit.MILLISECONDS
        );
        
        // Building the game frame.
        w = new JFrame("Dotto");
        w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        w.setResizable(false);

        // Checks for Fullscreen and adjusts the game screen.
        if (Config.FULLSCREEN) {
            w.setExtendedState(JFrame.MAXIMIZED_BOTH);
            w.setUndecorated(true);
        } else {
            w.setPreferredSize(new Dimension(Config.WIDTH, Config.HEIGHT));
            w.setLocationRelativeTo(null);
        }

        // Adding the actual game window to the screen.
        w.add(pane);
        w.addKeyListener(pane);
        w.addMouseListener(pane);
        w.pack();
        w.setVisible(true);
        
        // Setting the game window to fit the screen if Fullscreen is enabled.
        if (Config.FULLSCREEN) {
            Config.WIDTH = w.getWidth();
            Config.HEIGHT = w.getHeight();
        }

        try {
            Track t = new Track(
                rootDirectory.getPath() + "/maps/heiakim_counting", "1520351143651"
            );

            pane.view = t;
            t.start();
        } catch (IOException ex) { // TODO Auto-generated catch block
            Logger.getLogger(Core.class.getName())
                    .log(Level.WARNING, ex.getMessage(), ex);
            
            shutdown();
        }
    }

    /**
     * Closes the game upon request.
     */
    public static void shutdown() {
        THREAD_FACTORY.shutdown();
        System.exit(0);
    }
}

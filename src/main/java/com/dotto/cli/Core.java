package com.dotto.cli;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
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

    /**
     * Entry point of application.
     * 
     * @param args The command line arguments.
     */
    public static void main(String... args) {
        Flagger.setFlags(args);

        try {
            rootDirectory = Util.getLocalDirectory();
            Config.load();
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            System.exit(0);
        }

        // instantiate managers
        graphicManager = new Graphics();

        pane = new GamePane();
        w = new JFrame("Dotto");
        w.add(pane);
        w.addKeyListener(pane);
        w.addMouseListener(pane);
        w.setUndecorated(true);
        GraphicsDevice vc = GraphicsEnvironment.getLocalGraphicsEnvironment()
            .getDefaultScreenDevice();
        if (
            !vc.isFullScreenSupported()
        ) { throw new UnsupportedOperationException(
            "Fullscreen mode is unsupported."
        ); }

        vc.setFullScreenWindow(w);
        if (Config.FULLSCREEN) {
            Config.WIDTH = w.getWidth();
            Config.HEIGHT = w.getHeight();
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
        System.exit(0);
    }
}

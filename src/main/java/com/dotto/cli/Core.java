package com.dotto.cli;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JFrame;

import com.dotto.cli.util.Config;
import com.dotto.cli.util.Flagger;
import com.dotto.cli.util.Util;
import com.dotto.cli.util.manager.Audio;
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
    private static JFrame w;
    /** The current game pane in the window. */
    public static GamePane pane;
    /** The root directory of the application. */
    public static File rootDirectory;
    /** The graphics manager. */
    public static Graphics graphicManager;
    /** The audio manager. */
    public static Audio audioManager;

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
        audioManager = new Audio();

        pane = new GamePane();
        w = new JFrame("Dotto");
        w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        w.setPreferredSize(new Dimension(720, 480));
        w.setLocationRelativeTo(null);
        w.add(pane);

        if (Config.FULLSCREEN) {
            w.setExtendedState(JFrame.MAXIMIZED_BOTH);
            w.setUndecorated(true);
        }

        w.pack();
        w.setVisible(true);

        Track t = new Track();
        pane.view = t;
        try {
            t.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}

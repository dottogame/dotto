package com.dotto.client;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.FontFormatException;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.dotto.client.ui.GraphKit;
import com.dotto.client.ui.Skin;
import com.dotto.client.util.Config;
import com.dotto.client.util.Flagger;
import com.dotto.client.util.Util;
import com.dotto.client.util.manager.Discord;
import com.dotto.client.util.manager.Graphics;
import com.dotto.client.view.Track;
import java.util.concurrent.ExecutorService;

import net.arikia.dev.drpc.DiscordRPC;

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
    public static final ExecutorService THREAD_FACTORY = Executors.newCachedThreadPool();
    /** The user's original display mode before optimization. */
    public static DisplayMode originalMode;
    /** The graphic device of the user's original display. */
    public static GraphicsDevice vc;

    /**
     * Entry point of application.
     * 
     * @param args The command line arguments.
     */
    public static void main(String... args) {
        // needed to allow the game to draw in fullscreen mode
        System.setProperty("sun.java2d.noddraw", "true");

        try {
            // Sets flags that the game will use to adjust how it runs.
            Flagger.setFlags(args);

            if (!Flagger.DebugMode())
                // Protects the game from creating multiple instances of itself.
                GameLock.lockGame();

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

        // initialize utilities
        Discord.init();
        GraphKit.init();
        
        try {
            Skin.init();
        } catch (FontFormatException | IOException e) {
            Logger.getLogger(Core.class.getName())
                    .log(Level.SEVERE, "", e);
        }

        // Building the game window.
        pane = new GamePane();
        
        THREAD_FACTORY.execute(pane.updateLoop);
        THREAD_FACTORY.execute(pane.renderLoop);

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

        // load and set icon
        ImageIcon img = new ImageIcon(
            rootDirectory.getAbsolutePath() + "/data/dotto_64x64.png"
        );

        w.setIconImage(img.getImage());

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

        DiscordRPC.discordShutdown();
        if (Config.FULLSCREEN) vc.setDisplayMode(originalMode);

        if (!Flagger.DebugMode()) GameLock.unlockFile();

        System.exit(0);
    }
}

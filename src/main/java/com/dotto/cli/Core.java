package com.dotto.cli;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JFrame;

import com.dotto.cli.util.Config;
import com.dotto.cli.util.Util;
import com.dotto.cli.util.manager.Audio;
import com.dotto.cli.util.manager.Graphics;

/**
 * Entry point of program
 *
 */
public class Core {
    private static JFrame w;

    private static GamePane pane;

    public static File rootDirectory;

    public static Graphics graphicManager;
    public static Audio audioManager;

    public static void main(String[] args) {
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

        try {
            String path = Util.getAssetPath("./assets/dotto.wav");
            audioManager.load(path);
            audioManager.play(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.dotto.client;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.awt.FontFormatException;
import java.io.IOException;
import java.net.URISyntaxException;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.Configuration;

import com.dotto.client.ui.Skin;
import com.dotto.client.util.Config;
import com.dotto.client.util.Flagger;
import com.dotto.client.util.asset.BeatMap;
import com.dotto.client.util.manager.Discord;
import com.dotto.client.util.manager.Graphics;
import com.dotto.client.util.manager.MapConfigure;
import com.dotto.client.view.Track;
import com.dotto.client.view.View;

import net.arikia.dev.drpc.DiscordRPC;

/**
 * Entry point of program.
 *
 * @author lite20 (Ephraim Bilson)
 * @author SoraKatadzuma
 */
public class Core {
    // The window handle
    public static long window;

    // current view
    public static View view;

    /**
     * Entry point of application.
     * 
     * @param args The command line arguments.
     */
    public static void main(String... args) {
        // Sets flags that the game will use to adjust how it runs.
        Flagger.setFlags(args);

        // initialize the engine and utilities
        try {
            init();

            // Initialize & configure GLFW
            if (!glfwInit()) throw new IllegalStateException("Init GLFW fail");
            glfwDefaultWindowHints(); // optional, the current window hints are already the default
            glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
            glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will be resizable

            // Create the window
            window = glfwCreateWindow(
                Config.WIDTH, Config.HEIGHT, "dotto", NULL, NULL
            );

            if (window == NULL) throw new RuntimeException("GLFW window fail");

            // Setup key callback
            glfwSetKeyCallback(
                window, (window, key, scancode, action, mods) -> {
                    if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                        shutdown();
                        glfwSetWindowShouldClose(window, true);
                    }
                }
            );

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            int window_x = (vidmode.width() - Config.WIDTH) / 2;
            int window_y = (vidmode.height() - Config.HEIGHT) / 2;
            glfwSetWindowPos(window, window_x, window_y);

            // Make the OpenGL context current
            glfwMakeContextCurrent(window);

            // Enable v-sync
            glfwSwapInterval(1);

            // Make the window visible
            glfwShowWindow(window);

            // Detect & configure to what the graphics card is capable of
            GL.createCapabilities();

            // configure our opengl context
            initGL();

            // load skin
            Skin.init();

            // load test map
            BeatMap bm = MapConfigure.MapFromFolder("still_snow");
            view = new Track(bm.Maps.get(0), "still_snow");
            // TODO replace with proper game loop
            while (!glfwWindowShouldClose(window)) {
                // Poll for window events
                glfwPollEvents();

                // clear the framebuffer
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

                view.draw();

                // swap the color buffers
                glfwSwapBuffers(window);
            }
        } catch (URISyntaxException | IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the game upon request.
     */
    public static void shutdown() {
        DiscordRPC.discordShutdown();

        if (!Flagger.DEBUG) GameLock.unlockFile();
    }

    public static void init()
        throws URISyntaxException, IOException, FontFormatException {

        // Protects the game from creating multiple instances of itself.
        if (!Flagger.DEBUG) GameLock.lockGame();

        // initialize utilities
        Config.load();
        Discord.init();
        Graphics.init();

        Configuration.DEBUG.set(Flagger.DEBUG);

        // create error callback
        glfwSetErrorCallback((error, description) -> {
            System.err.println(
                "GLFW error [" + Integer.toHexString(error) + "]: " + GLFWErrorCallback.getDescription(description)
            );
        });

    }

    public static void initGL() {
        // set matrix mode
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();

        // set orthographic mode (and resolution)
        GL11.glOrtho(0, Config.WIDTH, 0, Config.HEIGHT, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        // Enable 2d textures
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        // Enable transparency
        GL11.glEnable(GL11.GL_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }
}

package com.dotto.client;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
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
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.awt.FontFormatException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import com.dotto.client.ui.GraphKit;
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
            glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

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

            // Get the thread stack and push a new frame
            MemoryStack stack = stackPush();
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            int window_x = (vidmode.width() - pWidth.get(0)) / 2;
            int window_y = (vidmode.height() - pHeight.get(0)) / 2;
            glfwSetWindowPos(window, window_x, window_y);

            // Make the OpenGL context current
            glfwMakeContextCurrent(window);

            // Enable v-sync
            glfwSwapInterval(1);

            // Make the window visible
            glfwShowWindow(window);

            // Detect & configure to what the graphics card is capable of
            GL.createCapabilities();

            // Set the clear color
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

            BeatMap bm = MapConfigure.MapFromFolder("still_snow");
            view = new Track(bm.Maps.get(0), "still_snow");
            // TODO replace with proper game loop
            while (!glfwWindowShouldClose(window)) {
                // clear the framebuffer
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

                // swap the color buffers
                glfwSwapBuffers(window);

                // Poll for window events
                glfwPollEvents();

                view.draw();
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
        GraphKit.init();
        Skin.init();
        Graphics.init();

        // create error callback
        GLFWErrorCallback.createPrint(System.err).set();
    }
}

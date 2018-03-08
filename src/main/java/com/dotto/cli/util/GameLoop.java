package com.dotto.cli.util;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author lite20 (Ephraim Bilson)
 */
public class GameLoop implements Runnable {
    /** Whether or not this {@code GameLoop} is running. */
    public boolean isRunning = true;
    /** The {@code GameCall} this {@code GameLoop} should run. */
    private final GameCall call;
    /** The current frames per second. */
    public int fps = 0;
    /** The static frames per second. */
    public int staticFps = 0;
    /** The target frames per second. */
    private final int targetFps;

    /**
     * Constructs a new instance of {@code GameLoop}.
     * 
     * @param call The call to make in this {@code GameLoop}.
     * @param fps The frames per second we want to achieve.
     */
    public GameLoop(GameCall call, int fps) {
        this.call = call;
        this.targetFps = fps;
    }
    
    /**
     * Inherited method.
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        // game render loop
        long lastLoopTime = System.nanoTime();
        long lastFpsTime = 0;

        final long OPTIMAL_TIME = 1000000000 / targetFps;

        while (isRunning) {
            // work out how long its been since the last update, this
            // will be used to calculate how far the entities should
            // move this loop
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            double delta = updateLength / ((double) OPTIMAL_TIME);

            call.call(delta);

            // update the frame counter
            lastFpsTime += updateLength;
            fps++;

            if (lastFpsTime >= 1000000000) {
                staticFps = fps;
                lastFpsTime = 0;
                fps = 0;
            }

            // sleep t'ill next frame
            try {
                if((lastLoopTime - System.nanoTime() + OPTIMAL_TIME) > 0)
                    Thread.sleep(
                        (lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000
                    );
            } catch (InterruptedException ex) {
                // TODO Auto-generated catch block
                Logger.getLogger(GameLoop.class.getName())
                        .log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }
}

package com.dotto.client.util;

/**
 * 
 * @author lite20 (Ephraim Bilson)
 */
public class GameLoop implements Runnable {
    /** The call this loop will make. */
    private final GameCall call;
    /** The number of frames we are currently getting. */
    public int frames;
    /** A static amount of frames per second to report back. */
    public int staticFps;
    /** The number of frames per second we want to achieve. */
    private final int targetFps;

    /**
     * Constructs a new {@code Engine} instance.
     * 
     * @param call The call this {@code GameLoop} will make.
     * @param targetFps The target amount of frames per second we want to achieve.
     */
    public GameLoop(GameCall call, int targetFps) {
        this.call = call;
        this.targetFps = targetFps;
    }

    /**
     * Inherited method.
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        long initialTime = System.nanoTime();
        final double timeF = 1000000000 / targetFps;
        double deltaF = 0;
        long timer = System.currentTimeMillis();

        while (true) {
            long currentTime = System.nanoTime();
            deltaF += (currentTime - initialTime) / timeF;
            initialTime = currentTime;

            if (deltaF >= 1) {
                call.call(deltaF);
                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis() - timer > 500) {
                staticFps = frames;
                frames = 0;
                timer += 1000;
            }
        }
    }
}

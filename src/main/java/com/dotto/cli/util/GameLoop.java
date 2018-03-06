package com.dotto.cli.util;

public class GameLoop implements Runnable {
    public boolean isRunning = true;

    private GameCall call;

    public int fps = 0;
    public int staticFps = 0;

    public GameLoop(GameCall call) {
        this.call = call;
    }

    @Override
    public void run() {
        // game render loop
        long lastLoopTime = System.nanoTime();
        long lastFpsTime = 0;

        final int TARGET_FPS = 60;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;

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
                Thread.sleep(
                    (lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000
                );
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}

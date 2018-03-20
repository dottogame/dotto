/* Copyright (c) 2018 SoraKatadzuma */
package com.dotto.client.framework;

import com.dotto.client.Core;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SoraKatadzuma
 */
public class Engine implements Runnable {
    /** The list of objects with {@code Behaviour} on them. */
    public final List<GameObject> OBJECTS = new ArrayList<>();
    /**
     * Frames are essentially when renders happen. These frame updates allow textures and
     * images to be written to the screen. It should try to mimic the refresh rate of the
     * monitor it is displaying to, if it can. If not then the fps should be variable
     * instead of static. By default we try to keep this at 60.
     */
    public final int updatesPerSecond;
    /***/
    private final long period;
    /** The current static amount of updates that we are experiencing. */
    private int staticUpdates;
    /** The current delta of the game. */
    private long delta = 0L;
    /** The time of our last update. */
    private long lastUpdateTime;
    /** The number of updates that have elapsed since the last second. */
    private int updateCount = 0;
    /** The number of notifications this {@code Engine} has received. */
    private int notificationCount = 0;
    
    /**
     * @param updatesPerSecond The number of updates per second this {@code Engine} should
     *      make.
     */
    public Engine(int updatesPerSecond) {
        this.updatesPerSecond = updatesPerSecond;
        staticUpdates = updatesPerSecond;
        period = 1000 / updatesPerSecond;
    }
    
    /**
     * Inherited method.
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public synchronized void run() {
        long currentTime = System.currentTimeMillis();
        long difference = currentTime - lastUpdateTime;
        long displacement;

        delta = difference / period;
        lastUpdateTime = currentTime;

        new Thread(() -> {
            OBJECTS.forEach((object) -> {
                object.update();
                addNotification();
            });
        }).start();

        try {
            this.wait();
        } catch (InterruptedException ex) {
            Logger.getLogger(Engine.class.getName())
                .log(Level.SEVERE, "Thread already interrupted.", ex);
        }

        // increment our number of updates
        updateCount = (++updateCount) < updatesPerSecond ? updateCount : 0;

        currentTime = System.currentTimeMillis();
        difference = currentTime - lastUpdateTime;
        displacement = period - difference;

        long nextUpdateTime = (displacement < 0) ? difference : displacement;
        
        // Liaison thread
        Core.THREAD_FACTORY.execute(() ->
            Core.pane.executeEngineAt(nextUpdateTime, this)
        );
    }
    
    /**
     * @return Current delta for this engine.
     */
    public long deltaTime() {
        return delta;
    }
    
    /**
     * @return The current smoothed amount of updates per second.
     */
    public int currentUpdatesPerSecond() {
        return staticUpdates;
    }
    
    /**
     * When enough notifications have came in, it will fully notify this {@code Engine}
     * and wake it up from it's sleep.
     */
    public synchronized void addNotification() {
        if (++notificationCount == OBJECTS.size()) { 
            this.notify();
            notificationCount = 0;
        }
    }
}

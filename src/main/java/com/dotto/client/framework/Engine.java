/* Copyright (c) 2018 SoraKatadzuma */
package com.dotto.client.framework;

import com.dotto.client.Core;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
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
    private static int updatesPerSecond;
    /** Helps time the {@code Engine} loop. */
    public static final Timer TIMER = new Timer();
    /** The amount of time between updates. */
    private final long period;
    /** The current static amount of updates that we are experiencing. */
    private static volatile int staticUpdates;
    /** The current average amount of updates that we are experiencing. */
    private static volatile int averageUpdatesPerSecond;
    /** The current delta of the game. */
    private long delta = 0L;
    /** The time of our last update. */
    private long lastUpdateTime;
    /** The number of updates that have elapsed since the last second. */
    private static int updateCount = 0;
    /** The update count last second. */
    private static int lastUpdateCount = 0;
    /** The number of seconds that have passed. */
    private static int secondCount = 0;
    /** The number of notifications this {@code Engine} has received. */
    private int notificationCount = 0;
    
    /**
     * @param targetUpdatesPerSecond The number of updates per second this {@code Engine} should
     *      make.
     */
    public Engine(int targetUpdatesPerSecond) {
        updatesPerSecond = targetUpdatesPerSecond;
        staticUpdates = updatesPerSecond;
        averageUpdatesPerSecond = updatesPerSecond;
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

        Core.THREAD_FACTORY.execute(() -> {
            OBJECTS.forEach((object) -> {
                object.update();
                addNotification();
            });
        });

        try {
            this.wait();
        } catch (InterruptedException ex) {
            Logger.getLogger(Engine.class.getName())
                .log(Level.SEVERE, "Thread already interrupted.", ex);
        }

        updateCount = (++updateCount <= updatesPerSecond) ? updateCount : 1;
        currentTime = System.currentTimeMillis();
        difference = currentTime - lastUpdateTime;
        displacement = period - (2 * difference);

        long nextUpdateTime = (displacement != 0) ? (displacement < 0) ? difference : displacement : 1;

        // Liaison thread
        Core.THREAD_FACTORY.execute(() ->
            Core.pane.executeEngineAt(nextUpdateTime, this)
        );
    }
    
    /**
     * Does particular work that happens only every second.
     */
    public static void setSecond() {
        long difference = updateCount - lastUpdateCount;
        lastUpdateCount = updateCount;
        
        difference = difference == updatesPerSecond ?
                     difference < 0 ?
                     updateCount + difference :
                     updateCount - difference :
                     0;
        
//        System.out.println(
//            String.format(
//                "UC: %d, LSUC: %d, UD: %d",
//                updateCount,
//                lastUpdateCount,
//                difference
//            )
//        );
        
        staticUpdates = (int)(difference == 0 ? updatesPerSecond : difference);
        averageUpdatesPerSecond += ((staticUpdates - averageUpdatesPerSecond) / ++secondCount);
        
//        System.out.println(String.format("AverageUPS: %d, StaticUPS: %d", averageUpdatesPerSecond, staticUpdates));
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

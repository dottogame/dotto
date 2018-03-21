package com.dotto.client;

import com.dotto.client.framework.Engine;
import com.dotto.client.framework.GameObject;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import com.dotto.client.util.Config;
import com.dotto.client.view.Menu;
import com.dotto.client.view.View;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Drawing surface for views and view manager.
 * 
 * @author lite20 (Ephraim Bilson)
 * @author SoraKatadzuma
 */
@SuppressWarnings("serial")
public class GamePane extends JPanel implements MouseListener, KeyListener {
    /** Current active view in the game pane. */
    public View view;
    /** Our update engine. */
    public final Engine physicsEngine;
    /** Our render engine. */
    public final Engine renderingEngine;
    /** The list of our {@code Engine}s, ease of use. */
    public final List<Engine> engines = new ArrayList<>();

    /**
     * Constructs a new {@code GamePane} object.
     */
    public GamePane() {
        setOpaque(true);

        // The starting view of the game.
        view = new Menu();

        engines.add(physicsEngine = new Engine(30));
        engines.add(renderingEngine = new Engine(60));

        GameObject object = new GameObject() {
            @Override
            public void update() {
                view.update(physicsEngine.deltaTime());
            } 
        };

        GameObject object2 = new GameObject() {
            @Override
            public void update() {
                repaint();
            } 
        };

        physicsEngine.OBJECTS.add(object);
        renderingEngine.OBJECTS.add(object2);

        engines.forEach((engine) -> {
            Engine.TIMER.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    engine.setSecond();
                }
            }, 0, 1000L);
            
            executeEngine(engine);
        });
    }

    /**
     * Alternative to {@code executeEngineAt(long, Engine)}
     * 
     * @param engine The engine to execute.
     */
    public final void executeEngine(Engine engine) {
        Core.THREAD_FACTORY.execute(() -> {
            if (Core.THREAD_FACTORY.isShutdown()) return;
            
            Core.THREAD_FACTORY.execute(engine);
        });
    }
    
    /**
     * Restarts the engines for another loop.
     * 
     * @param nextUpdateTime The time of the next update.
     * @param engine The engine to execute again.
     */
    public final void executeEngineAt(long nextUpdateTime, Engine engine) {
        Core.THREAD_FACTORY.execute(new Runnable() {
            @Override
            public synchronized void run() {
                try {
                    this.wait(nextUpdateTime);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GamePane.class.getName())
                        .log(Level.SEVERE, "Thread already interrupted.", ex);
                }

                if (Core.THREAD_FACTORY.isShutdown()) return;
                
                Core.THREAD_FACTORY.execute(engine);
            }
        });
    }
    
    /**
     * Inherited method.
     * 
     * @param g The {@code Graphics} being painted to the view.
     * @see javax.swing.JPanel#paintComponent(java.awt.Graphics)
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        if (Config.ANTIALIAS) {
            g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON
            );

            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );
        } else {
            g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_OFF
            );

            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF
            );
        }

        g2.setRenderingHint(
            RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR
        );

        g2.setRenderingHint(
            RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED
        );

        g2.setRenderingHint(
            RenderingHints.KEY_ALPHA_INTERPOLATION,
            RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED
        );

        view.draw(g2);
    }

    /**
     * Inherited method.
     * 
     * @param e The event being performed.
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(MouseEvent e) {
        view.mouseDown(e);
    }

    /**
     * Inherited method.
     * 
     * @param e The event being performed.
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        view.mouseUp(e);
    }

    /**
     * Inherited method.
     * 
     * @param e The event being performed.
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    @Override
    public void keyPressed(KeyEvent e) {
        view.keyDown(e);
    }

    /**
     * Inherited method.
     * 
     * @param e The event being performed.
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
    @Override
    public void keyReleased(KeyEvent e) {
        view.keyUp(e);
    }

    /**
     * Inherited method.
     * 
     * @param e The event being performed.
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * Inherited method.
     * 
     * @param e The event being performed.
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(MouseEvent e) {}

    /**
     * Inherited method.
     * 
     * @param e The event being performed.
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseEntered(MouseEvent e) {}

    /**
     * Inherited method.
     * 
     * @param e The event being performed.
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseExited(MouseEvent e) {}
}

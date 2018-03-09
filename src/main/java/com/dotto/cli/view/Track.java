package com.dotto.cli.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dotto.cli.Core;
import com.dotto.cli.ui.Graphic;
import com.dotto.cli.util.BeatStreamReader;
import com.dotto.cli.util.Config;
import com.dotto.cli.util.Util;
import com.dotto.cli.util.asset.Audio;
import com.dotto.cli.util.asset.Beat;
import com.dotto.cli.util.asset.BeatMap;
import com.dotto.cli.util.asset.MapData;
import com.dotto.cli.util.manager.MapConfigure;
import com.dotto.cli.util.manager.Score;

/**
 * TODO: write class description.
 * 
 * @author lite20 (Ephraim Bilson)
 * @author SoraKatadzuma
 */
public class Track implements View {
    /** A manual serial id, instead of the normal Java serailID. */
    public static int ID = 3;
    /** The x offset of a beat. */
    public double xOffset = 0;
    /** The y offset of a beat. */
    public double yOffset = 0;
    /** The graphical x offset of a beat. */
    public double gxOffset = 0;
    /** The graphical y offset of a beat. */
    public double gyOffset = 0;
    /** The x acceleration of the map. */
    public double xAccel = 0;
    /** The y acceleration of the map. */
    public double yAccel = 0;
    /** The speed of the acceleration. */
    public double speed = 4;
    /** How much the cursor slides when moving. */
    public double glideFactor = 0.5;
    /***/
    public boolean UP = false;
    public boolean DOWN = false;
    public boolean LEFT = false;
    public boolean RIGHT = false;

    /** The {@code BeatMap} that will be played. */
    public BeatMap beatMap;
    /** The beatMap's data. */
    public MapData map;
    /** A reader for the map. */
    private BeatStreamReader bsr;
    /** The beats that will be loaded and placed on the screen. */
    private final List<Beat> beats;
    /** The music to play during this game round. */
    private final Audio music;
    /** Current game round score. */
    private final Score score;
    /** The current background image. */
    private final Graphic back;
    /** The background dim color. */
    private final Color tint;
    /** The ratio of the background. */
    private final float backRatio;
    /** The width of the background. */
    private final int backWidth;
    /** A check to see if the track should reset. */
    private boolean shouldReset;

    private Graphic cursor;
    private Graphic gridPoint;
    private Graphic hitCircle;
    private Graphic approachCircle;

    /**
     * Constructs a new {@code Track View}.
     * 
     * @param path The path to the song for this map.
     * @param mapId The BeatMap to play.
     * @throws java.io.IOException If a file cannot be read.
     */
    public Track(String path, String mapId) throws IOException {
        music = new Audio(path + "/track.ogg");
        beatMap = MapConfigure.MapFromFolder(path);
        bsr = new BeatStreamReader(new File(path + "/" + mapId + ".to"));
        map = beatMap.Maps.get(mapId);
        speed = map.acceleration;
        beats = Collections
            .synchronizedList(new ArrayList<>(map.ClickCount + map.SlideCount));
        beats.add(bsr.GetNextBeat());
        score = new Score();
        // load graphics
        back = new Graphic(path + "/back");
        cursor = new Graphic(Util.getSkinPath("cursor"));
        gridPoint = new Graphic(Util.getSkinPath("grid_plus"));
        hitCircle = new Graphic(Util.getSkinPath("hit_circle"));
        approachCircle = new Graphic(Util.getSkinPath("approach_circle"));
        backWidth = (int) (map.bound.x * 0.25f) + Config.WIDTH;
        backRatio = (float) back.getHeight() / (float) back.getWidth();
        tint = new Color(0f, 0f, 0f, Config.BACK_DIM);
    }

    /**
     * Starts a game round.
     * 
     * @throws java.io.IOException
     */
    public void start() throws IOException {
        // align at center of note 0
        xOffset = beats.get(0).x;
        yOffset = beats.get(0).y;

        music.play();
    }

    /**
     * Rests the game round.
     * 
     * @throws java.io.IOException
     */
    public void reset() throws IOException {
        xAccel = yAccel = 0;
        score.reset();
        beats.clear();
        bsr.reset();
        beats.add(bsr.GetNextBeat());
        music.stop();
        music.clip.setFramePosition(0);
        start();
    }

    /**
     * Gets the serial id of this object.
     * 
     * @return The serial id of this object.
     */
    @Override
    public int getId() {
        return ID;
    }

    /**
     * Draws the current {@code Graphics g} to the screen.
     * 
     * @param g The {@code Graphics} to draw.
     */
    @Override
    public void draw(Graphics2D g) {

        // draw back
        g.drawImage(
            back.getBuffer(), (int) (gxOffset * 0.25), (int) (gyOffset * 0.25),
            backWidth, (int) (backWidth * backRatio), null
        );

        // draw tint
        g.setColor(tint);
        g.fillRect(0, 0, Config.WIDTH, Config.HEIGHT);

        // draw grid
        g.setColor(Color.WHITE);
        for (int y = -100; y < Config.HEIGHT + 100; y += 100) {
            for (int x = -100; x < Config.WIDTH + 100; x += 100) {
                g.drawImage(
                    gridPoint.getBuffer(),
                    x + (int) ((xOffset % 200) * 0.5)
                        - (int) (gridPoint.getWidth() / 2),
                    y + (int) ((yOffset % 200) * 0.5)
                        - (int) (gridPoint.getHeight() / 2),
                    null
                );
            }
        }

        // draw notes
        float pad;
        Beat beat;
        double noteOffX = Config.WIDTH / 2 - 50;
        double noteOffY = Config.HEIGHT / 2 - 50;

        for (int i = 0; i < beats.size(); i++) {
            beat = beats.get(i);

            if (beat == null) continue;

            pad = ((beat.ClickTimestamp
                - (music.clip.getMicrosecondPosition() / 1000)) * 0.1f);

            if (pad < 0) pad = 0;

            g.drawImage(
                approachCircle.getBuffer(),
                (int) (beat.x - pad / 2 + xOffset + noteOffX),
                (int) (beat.y - pad / 2 + yOffset + noteOffY),
                (int) (100.0f + pad), (int) (100.0f + pad), null
            );

            if (beat.GetType() == Beat.CLICK) {
                g.drawImage(
                    hitCircle.getBuffer(), (int) (beat.x + xOffset + noteOffX),
                    (int) (beat.y + yOffset + noteOffY), 100, 100, null
                );
            } else {
                // draw hit points at first and last point of slider
                float[] beatE = beat.sliderPoints
                    .get(beat.sliderPoints.size() - 1);
                g.drawImage(
                    hitCircle.getBuffer(), (int) (beat.x + xOffset + noteOffX),
                    (int) (beat.y + yOffset + noteOffY), 100, 100, null
                );

                g.drawImage(
                    hitCircle.getBuffer(),
                    (int) (beatE[0] + xOffset + noteOffX),
                    (int) (beatE[1] + yOffset + noteOffY), 100, 100, null
                );

                // draw midpoints
                for (int z = 0; z < beat.sliderPoints.size(); z++) {
                    g.draw(
                        new Ellipse2D.Double(
                            beat.sliderPoints.get(z)[0] + xOffset + 10,
                            beat.sliderPoints.get(z)[1] + yOffset + 10, 20, 20
                        )
                    );
                }
            }
        }

        g.setColor(Color.WHITE);

        // draw FPS
        g.drawString(Core.pane.renderLoop.staticFps + " fps", 10, 20);

        // draw Accuracy
        g.drawString(
            score.currentAccuracy + "%", (int) (Config.WIDTH * 0.9f), 20
        );

        // draw cursor
        g.drawImage(
            cursor.getBuffer(), (Config.WIDTH / 2) - 15,
            (Config.HEIGHT / 2) - 15, 30, 30, null
        );

    }

    /**
     * Mouse up event handle
     * 
     * @param e
     */
    @Override
    public void mouseUp(MouseEvent e) {}

    /**
     * Mouse down event handle
     * 
     * @param e
     */
    @Override
    public void mouseDown(MouseEvent e) {}

    /**
     * Key down event handle
     * 
     * @param e
     */
    @Override
    public void keyDown(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_R && e.isControlDown())
            shouldReset = true;
        else if (e.getKeyCode() == Config.UP_KEY) UP = true;
        else if (e.getKeyCode() == Config.DOWN_KEY) DOWN = true;
        else if (e.getKeyCode() == Config.LEFT_KEY) LEFT = true;
        else if (e.getKeyCode() == Config.RIGHT_KEY) RIGHT = true;
        else if (Config.TAP_KEYS.contains(e.getKeyCode())) {
            Beat tapped = beats.get(0);

            if (tapped == null) return;

            long tapOff = tapped.ClickTimestamp
                - (music.clip.getMicrosecondPosition() / 1000);

            // ignore if too early. Eventually give some visual feedback
            if (tapOff < 500) {
                beats.remove(0);
                score.adjustAccuracy(score.calculateAccuracy(tapOff));

                if (Math.abs(tapOff) < 100) System.out.println("nice!");
            }
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            Core.shutdown();
        }
    }

    /**
     * Key up event handle.
     * 
     * @param e
     */
    @Override
    public void keyUp(KeyEvent e) {
        if (e.getKeyCode() == Config.UP_KEY) UP = false;
        else if (e.getKeyCode() == Config.DOWN_KEY) DOWN = false;
        else if (e.getKeyCode() == Config.LEFT_KEY) LEFT = false;
        else if (e.getKeyCode() == Config.RIGHT_KEY) RIGHT = false;
    }

    /**
     * Inherited method.
     * 
     * @param delta The gradient at which things should move.
     * @see com.dotto.cli.view.View#update(double)
     */
    @Override
    public void update(double delta) {
        if (shouldReset) {
            try {
                reset();
            } catch (IOException ex) {
                Logger.getLogger(Track.class.getName()).log(
                    Level.SEVERE, null, ex
                );
            }

            shouldReset = false;
        }

        yAccel *= glideFactor;
        xAccel *= glideFactor;

        if (UP) yAccel += speed;
        if (DOWN) yAccel -= speed;
        if (LEFT) xAccel += speed;
        if (RIGHT) xAccel -= speed;

        yOffset += yAccel * delta;
        xOffset += xAccel * delta;

        gxOffset = xOffset;
        gyOffset = yOffset;

        if (gxOffset > 0) gxOffset = 0;
        if (gxOffset < -map.bound.x) gxOffset = -map.bound.x;
        if (gyOffset > 0) gyOffset = 0;
        if (gyOffset < -map.bound.y) gyOffset = -map.bound.y;

        // load more beats
        if (beats.isEmpty()) beats.add(bsr.GetNextBeat());

        long offset = music.clip.getMicrosecondPosition() / 1000;

        // notes can be deleted in this window (if game is reset)
        if (beats.size() > 0) {
            Beat new_beat = beats.get(beats.size() - 1);
            while (new_beat != null && new_beat.InitTimestamp < offset) {
                new_beat = bsr.GetNextBeat();
                beats.add(new_beat);
            }
        }

        // expire passed notes
        float pad;
        int i = 0;
        Beat beat;

        for (int z = 0; z < beats.size(); z++) {
            beat = beats.get(z);

            if (beat == null) continue;

            pad = (beat.ClickTimestamp
                - (music.clip.getMicrosecondPosition() / 1000));

            if (pad < -250) {
                // it's a miss! D:
                beats.remove(i);
                score.adjustAccuracy(0);
            }

            i++;
        }
    }
}

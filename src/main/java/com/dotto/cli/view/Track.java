package com.dotto.cli.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import com.dotto.cli.Core;
import com.dotto.cli.ui.Graphic;
import com.dotto.cli.util.BeatStreamReader;
import com.dotto.cli.util.Config;
import com.dotto.cli.util.asset.Audio;
import com.dotto.cli.util.asset.Beat;
import com.dotto.cli.util.asset.BeatMap;
import com.dotto.cli.util.asset.Click;
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

    public double xOffset = 0;
    public double yOffset = 0;
    public double gxOffset = 0;
    public double gyOffset = 0;
    public double xAccel = 0;
    public double yAccel = 0;
    public double speed = 4;
    public double glideFactor = 0.5;

    public boolean UP = false;
    public boolean DOWN = false;
    public boolean LEFT = false;
    public boolean RIGHT = false;

    public BeatMap beatMap;

    public MapData map;

    private BeatStreamReader bsr;

    private final Vector<Beat> beats;

    private final Audio music;

    private String path;

    private String mapId;

    final static float dash1[] = { 10.0f };
    final static float dash2[] = { 1.0f };

    final static BasicStroke solid = new BasicStroke(
        5.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash2, 0.0f
    );

    final static BasicStroke dashed = new BasicStroke(
        2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f
    );

    private Score score;
    private Graphic back;

    private Color tint;

    private float backRatio;

    private int backWidth;

    public Track(String path, String mapId) throws IOException {
        this.path = path;
        this.mapId = mapId;
        music = new Audio(path + "/track.ogg");
        beatMap = MapConfigure.MapFromFolder(path);
        bsr = new BeatStreamReader(new File(path + "/" + mapId + ".to"));
        map = beatMap.Maps.get(mapId);
        speed = map.acceleration;
        beats = new Vector<>(map.ClickCount + map.SlideCount);
        beats.add(bsr.GetNextBeat());
        score = new Score();
        back = new Graphic(path + "/back");
        backWidth = (int) (map.bound.x * 0.25f) + Config.WIDTH;
        backRatio = (float) back.getHeight() / (float) back.getWidth();

        tint = new Color(0f, 0f, 0f, Config.BACK_DIM);
    }

    public void start() throws IOException {
        // align at center of note 0
        Click click = (Click) beats.get(0);
        xOffset = click.x;
        yOffset = click.y;

        music.play();
    }

    public void reset() throws IOException {
        score.reset();
        beats.clear();
        bsr = new BeatStreamReader(new File(path + "/" + mapId + ".to"));

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
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Config.WIDTH, Config.HEIGHT);

        // draw back
        /*
         * back.getBuffer(), 0, 0, Config.WIDTH, (int) (Config.HEIGHT / backScaleFactor), null );
         */
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
                g.fillOval(
                    x + (int) ((xOffset % 200) * 0.5),
                    y + (int) ((yOffset % 200) * 0.5), 3, 3
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
            if (beat.GetType() == Beat.CLICK) {
                Click click = (Click) beat;
                if (pad * 100 < 250) g.setColor(Color.RED);
                if (pad == 0) g.setColor(Color.GRAY);

                g.setStroke(solid);
                g.draw(
                    new Ellipse2D.Double(
                        (int) (click.x - pad / 2 + xOffset + noteOffX),
                        (int) (click.y - pad / 2 + yOffset + noteOffY),
                        (int) (100 + pad), (int) (100 + pad)
                    )
                );

                g.setColor(Color.WHITE);
                g.setStroke(dashed);
                g.draw(
                    new Ellipse2D.Double(
                        (int) (click.x + xOffset + noteOffX),
                        (int) (click.y + yOffset + noteOffY), 100, 100
                    )
                );
            } else {

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
        g.fillOval((Config.WIDTH / 2) - 15, (Config.HEIGHT / 2) - 15, 30, 30);
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
        if (e.getKeyCode() == KeyEvent.VK_R && e.isControlDown()) {
            try {
                reset();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } else if (e.getKeyCode() == Config.UP_KEY) UP = true;
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
     * Key up event handle
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

    @Override
    public void update(double delta) {
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
        if (beats.size() == 0) beats.add(bsr.GetNextBeat());
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

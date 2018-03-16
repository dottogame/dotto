package com.dotto.client.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.dotto.client.Core;
import com.dotto.client.ui.GraphKit;
import com.dotto.client.ui.Graphic;
import com.dotto.client.ui.Skin;
import com.dotto.client.util.BeatStreamReader;
import com.dotto.client.util.Config;
import com.dotto.client.util.Util;
import com.dotto.client.util.asset.Audio;
import com.dotto.client.util.asset.Beat;
import com.dotto.client.util.asset.MapData;
import com.dotto.client.util.manager.Discord;
import com.dotto.client.util.manager.Score;

/**
 * TODO: write class description.
 * 
 * @author lite20 (Ephraim Bilson)
 * @author SoraKatadzuma
 */
public class Track implements View {

    private Audio music;

    private double speed;

    private List<Beat> beats;

    private BeatStreamReader bsr;

    private Score score;

    private Graphic back;

    private float xOffset;
    private float yOffset;
    private float yAccel;
    private float xAccel;
    private float glideFactor;

    private MapData map;

    public Track(MapData map, String trackName) throws IOException {
        try {
            String path = Util.getLocal() + "/maps/" + trackName;
            music = new Audio(path + "/track.ogg");
            bsr = new BeatStreamReader(new File(path + "/" + map.id + ".to"));
            back = new Graphic(path + "/back");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        this.map = map;
        score = new Score();
        speed = map.acceleration;
        beats = Collections
            .synchronizedList(new ArrayList<>(map.ClickCount + map.SlideCount));

        // backWidth = (int) (map.bound.x * 0.25f) + Config.WIDTH;
        // backRatio = (float) back.getHeight() / (float) back.getWidth();
        setDiscordName(trackName);
    }

    private void setDiscordName(String name) {
        Discord.discord.largeImageKey = "large_default";
        Discord.discord.details = "Playing \"" + name + "\"";
        Discord.update();
    }

    private void setDiscordTime() {
        Discord.discord.endTimestamp = (System.currentTimeMillis()
            + (music.clip.getMicrosecondLength() / 1000)) / 1000;
        Discord.update();
    }

    @Override
    public int getId() {
        return 3;
    }

    @Override
    public void draw() {
        // draw back
        back.bind();
        GraphKit.drawQuad(
            xOffset * 0.25f, yOffset * 0.25f,
            (xOffset * 0.25f) + back.getWidth(),
            (yOffset * 0.25f) + back.getHeight());

        // TODO draw tint

        // draw grid
        Skin.gridPoint.bind();
        float xpos, ypos;
        for (int y = -100; y < Config.HEIGHT + 100; y += 100) {
            for (int x = -100; x < Config.WIDTH + 100; x += 100) {
                xpos = x + (xOffset % 200.0f) * 0.5f
                    - Skin.gridPoint.getWidth() / 2;
                ypos = y + (yOffset % 200.0f) * 0.5f
                    - Skin.gridPoint.getHeight() / 2;
                GraphKit.drawQuad(
                    xpos, ypos, xpos + Skin.gridPoint.getWidth(),
                    ypos + Skin.gridPoint.getHeight());
            }
        }

        // draw notes
        float pad;
        float noteOffX = Config.HALF_WIDTH - 50;
        float noteOffY = Config.HALF_HEIGHT - 50;

        Beat beat;
        for (int i = 0; i < beats.size(); i++) {
            beat = beats.get(i);

            if (beat == null) continue;

            pad = ((beat.ClickTimestamp
                - (music.clip.getMicrosecondPosition() / 1000)) * 0.1f);

            if (pad < 0) pad = 0;

            // draw approach circle
            Skin.approachCircle.bind();

            GraphKit.drawQuad(
                beat.x - pad / 2.0f + xOffset + noteOffX,
                beat.y - pad / 2.0f + yOffset + noteOffY, 100.0f + pad,
                100.0f + pad);

            // draw sub circle
            Skin.hitCircle.bind();
            GraphKit.drawQuad(
                beat.x + xOffset + noteOffX, beat.y + yOffset + noteOffY,
                beat.x + xOffset + noteOffX + Skin.hitCircle.getWidth(),
                beat.y + yOffset + noteOffY + Skin.hitCircle.getHeight());

            // draw top circle
            Skin.hitCircleOverlay.bind();
            GraphKit.drawQuad(
                beat.x + xOffset + noteOffX, beat.y + yOffset + noteOffY,
                beat.x + xOffset + noteOffX + Skin.hitCircleOverlay.getWidth(),
                beat.y + yOffset + noteOffY + Skin.hitCircleOverlay.getHeight()
                );

            if (beat.GetType() == Beat.SLIDE) {
                // draw hit point last point of slider
                float[] beatE = beat.sliderPoints
                    .get(beat.sliderPoints.size() - 1);

                // draw sub circle
                Skin.hitCircle.bind();
                GraphKit.drawQuad(
                    beatE[0] + xOffset + noteOffX,
                    beatE[1] + yOffset + noteOffY,
                    beatE[0] + xOffset + noteOffX + Skin.hitCircle.getWidth(),
                    beatE[1] + yOffset + noteOffY + Skin.hitCircle.getHeight());

                // draw top circle
                Skin.hitCircleOverlay.bind();
                GraphKit.drawQuad(
                    beatE[0] + xOffset + noteOffX,
                    beatE[1] + yOffset + noteOffY,
                    beatE[0] + xOffset + noteOffX
                        + Skin.hitCircleOverlay.getWidth(),
                    beatE[1] + yOffset + noteOffY
                        + Skin.hitCircleOverlay.getHeight()
                    );
            }
        }

        // draw cursor
        Skin.cursor.bind();
        GraphKit.drawQuad(
            Config.HALF_WIDTH - 15, Config.HALF_HEIGHT - 15,
            Config.HALF_WIDTH - 15 + Skin.cursor.getWidth(),
            Config.HALF_HEIGHT - 15 + Skin.cursor.getHeight());
    }

    @Override
    public void update(float delta) {
        yAccel *= glideFactor;
        xAccel *= glideFactor;

        if (GLFW.glfwGetKey(Core.window, GLFW.GLFW_KEY_I) == 1) yAccel += speed;
        if (GLFW.glfwGetKey(Core.window, GLFW.GLFW_KEY_K) == 1) yAccel -= speed;
        if (GLFW.glfwGetKey(Core.window, GLFW.GLFW_KEY_J) == 1) xAccel += speed;
        if (GLFW.glfwGetKey(Core.window, GLFW.GLFW_KEY_L) == 1) xAccel -= speed;

        yOffset += yAccel * delta;
        xOffset += xAccel * delta;

        if (xOffset > 0) xOffset = 0;
        if (xOffset < -map.bound.x) xOffset = -map.bound.x;
        if (yOffset > 0) yOffset = 0;
        if (yOffset < -map.bound.y) yOffset = -map.bound.y;

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

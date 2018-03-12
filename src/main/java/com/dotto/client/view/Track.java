package com.dotto.client.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.dotto.client.util.BeatStreamReader;
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

    public Track(MapData map, String trackName) {
        try {
            String path = Util.getLocal() + "/maps/" + trackName;
            music = new Audio(path + "/track.ogg");
            bsr = new BeatStreamReader(new File(path + "/" + map.id + ".to"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

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

    }

    public Track start() {
        beats.add(bsr.GetNextBeat());
        setDiscordTime();
        music.clip.start();
        return this;
    }
}

package com.dotto.client.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.PNGDecoder;

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

    private int w;

    private int h;

    private int texId;

    public Track(MapData map, String trackName) throws IOException {
        try {
            String path = Util.getLocal() + "/maps/" + trackName;
            music = new Audio(path + "/track.ogg");
            bsr = new BeatStreamReader(new File(path + "/" + map.id + ".to"));
            InputStream in = new FileInputStream(new File(path + "/back.png"));
            PNGDecoder decoder = new PNGDecoder(in);
            w = decoder.getWidth();
            h = decoder.getHeight();
            ByteBuffer buffer = BufferUtils.createByteBuffer(w * h * 4);
            decoder.decode(buffer, w * 4, PNGDecoder.RGBA);
            buffer.flip();
            in.close();

            texId = GL11.glGenTextures();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);

            // All RGB bytes are aligned to each other and each component is 1 byte
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            // Upload the texture data and generate mip maps (for scaling)
            GL11.glTexImage2D(
                GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, w, h, 0, GL11.GL_RGBA,
                GL11.GL_UNSIGNED_BYTE, buffer
            );

            // Setup what to do when the texture has to be scaled
            GL11.glTexParameteri(
                GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST
            );
            GL11.glTexParameteri(
                GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
                GL11.GL_LINEAR_MIPMAP_LINEAR
            );
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
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2f(100, 100);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2f(100 + w, 100);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2f(100 + w, 100 + h);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2f(100, 100 + h);
        GL11.glEnd();
    }

    public Track start() {
        beats.add(bsr.GetNextBeat());
        setDiscordTime();
        music.clip.start();
        return this;
    }
}

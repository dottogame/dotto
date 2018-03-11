package com.dotto.cli.util.manager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.arikia.dev.drpc.callbacks.ReadyCallback;
import net.arikia.dev.drpc.callbacks.SpectateGameCallback;

public class Discord implements ActionListener {
    private static boolean initialized = false;
    private static boolean new_presence = true;
    private static boolean connected = false;

    private static DiscordEventHandlers handler;

    public static DiscordRichPresence discord;

    // here in case we need to kill timers eventually
    public static Discord dcord;

    public Discord() {
        Timer timer = new Timer(5000, this);
        timer.setInitialDelay(0);
        timer.start();
    }

    public static void init() {
        discord = new DiscordRichPresence();
        handler = new DiscordEventHandlers();
        handler.ready = new ReadyEvent();
        dcord = new Discord();
    }

    public static class ReadyEvent implements ReadyCallback {
        @Override
        public void apply() {
            connected = true;
            System.out.println("Discord Connected.");
        }
    }

    public static class SpectateEvent implements SpectateGameCallback {
        @Override
        public void apply(String id) {
            System.out.println("Spectate requested " + id);
        }
    }

    /**
     * We perform initialization here as DiscordRPC doesn't play nicely with multi-threading so we
     * keep everything happening on one thread.
     */
    @Override
    public void actionPerformed(ActionEvent act) {
        // init, and if initialized we run callbacks
        if (connected && new_presence) {
            new_presence = false;
            System.out.println("updating presence");
            DiscordRPC.discordUpdatePresence(discord);
        } else if (initialized) DiscordRPC.discordRunCallbacks();
        else {
            initialized = true;
            System.out.println("Kickstarting Discord RPC");
            DiscordRPC.discordInitialize("421924584904851456", handler, true);
        }
    }

    public static void update() {
        new_presence = true;
    }
}

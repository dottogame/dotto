package com.dotto.cli.util.manager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.callbacks.ReadyCallback;
import net.arikia.dev.drpc.callbacks.SpectateGameCallback;

public class Discord implements ActionListener {
    private boolean initialized = false;

    private DiscordEventHandlers handler;

    public Discord() {
        handler = new DiscordEventHandlers();
        handler.ready = new ReadyEvent();
        Timer timer = new Timer(5000, this);
        timer.setInitialDelay(0);
        timer.start();
    }

    public static class ReadyEvent implements ReadyCallback {
        @Override
        public void apply() {
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
        if (initialized) DiscordRPC.discordRunCallbacks();
        else {
            initialized = true;
            System.out.println("Kickstarting Discord RPC");
            DiscordRPC.discordInitialize("421924584904851456", handler, true);
        }
    }
}

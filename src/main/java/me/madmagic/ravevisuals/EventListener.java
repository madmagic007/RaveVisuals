package me.madmagic.ravevisuals;

import me.madmagic.ravevisuals.raveold.handlers.fixtures.FixtureHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventListener implements Listener {

    @EventHandler
    public void event(PlayerJoinEvent event) {
        new Thread(() -> {
            FixtureHandler.spawnAllForPlayer(event.getPlayer());
        }).start();
    }
}
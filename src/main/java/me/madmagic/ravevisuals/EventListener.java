package me.madmagic.ravevisuals;

import me.madmagic.ravevisuals.handlers.FixtureHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventListener implements Listener {

    @EventHandler
    public void event(PlayerJoinEvent event) {
        new Thread(() -> {
            Player player = event.getPlayer();
            FixtureHandler.activeFixtures.forEach((s, f) -> f.spawn(player).syncAll(player));
        }).start();
    }
}
package me.madmagic.ravevisuals.handlers;

import me.madmagic.ravevisuals.ents.Fixture;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class EditorHandler {

    public static Map<UUID, EditingPlayer> editingPlayers = new HashMap<>();

    public static void startEditMode(Player player) {
        if (editingPlayers.containsKey(player.getUniqueId())) return;
        editingPlayers.put(player.getUniqueId(), new EditingPlayer(null));

        FixtureHandler.activeFixtures.forEach((s, f) -> f.setCustomNameVisible(true, player));

        startTimer();
    }

    public static void stopEditMode(Player player) {
        EditingPlayer ep = editingPlayers.get(player.getUniqueId());
        if (ep == null) return;

        ep.stopEditingFixture();
        editingPlayers.remove(player.getUniqueId());
        if (editingPlayers.isEmpty()) stopTimer();

        FixtureHandler.activeFixtures.forEach((s, f) -> f.setCustomNameVisible(false, player));
    }

    public static void editFixture(Player player, Fixture fixture) {
        EditingPlayer ep = editingPlayers.get(player.getUniqueId());
        if (ep == null) return;

        if (fixture.equals(ep.f)) {
            ep.stopEditingFixture();
            return;
        }

        ep.startEditingFixture(fixture);
    }

    private static Timer editTimer;
    private static void startTimer() {
        stopTimer();
        editTimer = new Timer();
        editTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (editingPlayers.isEmpty()) {
                    stopTimer();
                    return;
                }

                List<UUID> offline = new ArrayList<>();
                editingPlayers.forEach((u, ep) -> {
                    Player p = Bukkit.getPlayer(u);
                    if (p == null || !p.isOnline()) {
                        offline.add(u);
                        return;
                    }

                    if (ep.f == null) return;

                    Location loc = PositioningHelper.inFrontOfLookAt(p, 2).subtract(0, 1.7, 0);
                    ep.f.setLocation(loc).setHeadPose(loc.getYaw(), loc.getPitch()).syncHeadPose();
                });

                offline.forEach(editingPlayers::remove);
            }
        }, 0, 100);
    }

    private static void stopTimer() {
        try {
            editTimer.cancel();
        } catch (Exception ignored) {}
    }

    public static class EditingPlayer {
        public Fixture f;

        public EditingPlayer(Fixture f) {
            this.f = f;
        }

        public void startEditingFixture(Fixture f) {
            if (this.f != null) stopEditingFixture();
            this.f = f;
        }

        public void stopEditingFixture() {
            f = null;
        }
    }
}

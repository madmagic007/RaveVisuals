package me.madmagic.ravevisuals.handlers.fixtures;

import me.madmagic.ravevisuals.fixture.Fixture;
import me.madmagic.ravevisuals.handlers.EditorHandler;
import me.madmagic.ravevisuals.handlers.PositioningHelper;
import me.madmagic.ravevisuals.config.FixtureConfig;
import me.madmagic.ravevisuals.handlers.GroupHandler;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class FixtureHandler {

    public static HashMap<String, Fixture> activeFixtures = new HashMap<>();

    public static void createFromCommand(CommandSender sender, String[] cmdArgs) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only a player may run this command");
            return;
        }
        String name = cmdArgs[1];
        if (activeFixtures.containsKey(name)) {
            sender.sendMessage("A fixture with that name already exists");
            return;
        }

        EditorHandler.startEditMode(player);
        Location loc = PositioningHelper.inFrontOfLookAt(player, 2);
        Fixture f = new Fixture(loc, name);
        activeFixtures.put(name, f);
        f.setInvisible(false).setCustomName(name).update(player);
        player.sendMessage("Fixture created, make sure to use '/rv fixture save' after you are done editing");
    }

    public static void removeFromCommand(CommandSender sender, String[] cmdArgs) {
        String name = cmdArgs[1];
        if (!activeFixtures.containsKey(name)) {
            sender.sendMessage("No fixture with that name found");
            return;
        }

        Fixture f = activeFixtures.get(name);
        f.turnOff();
        f.deSpawn();
        activeFixtures.remove(name);
        if (sender instanceof Player player) EditorHandler.startEditMode(player);
        sender.sendMessage("Fixture removed, make sure to use '/rv fixture save' after you are done editing");
    }

    public static void spawnAllForPlayer(Player player) {
        activeFixtures.forEach((s, f) -> f.lateSpawn(player));
    }

    public static void save(CommandSender sender) {
        if (sender instanceof Player player) EditorHandler.stopEditMode(player);
        FixtureConfig.saveAll();
        reload();
        GroupHandler.reload(); //in case fixture got removed
    }

    public static void despawnAll() {
        activeFixtures.forEach((s, f) -> {
            f.turnOff();
            f.deSpawn();
        });

        activeFixtures.clear();
    }

    public static void createFromConfig(String name, ConfigurationSection conf) {
        Location loc = conf.getLocation("location").add(0, 1.7, 0);
        Fixture f = new Fixture(loc, name, conf);
        activeFixtures.put(name, f);
    }

    public static void reload() {
        FixtureHandler.despawnAll();
        FixtureConfig.init();
    }
}

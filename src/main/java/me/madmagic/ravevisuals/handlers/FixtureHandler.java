package me.madmagic.ravevisuals.handlers;

import me.madmagic.ravevisuals.Util;
import me.madmagic.ravevisuals.config.FixtureConfig;
import me.madmagic.ravevisuals.ents.Fixture;
import me.madmagic.ravevisuals.handlers.anim.MotionHandler;
import me.madmagic.ravevisuals.handlers.anim.SequenceHandler;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class FixtureHandler {

    private static HashMap<String, Fixture> activeFixtures = new HashMap<>();

    public static Fixture getByName(String name) {
        return activeFixtures.get(name);
    }

    public static List<String> getLoadedFixtureNames() {
        return activeFixtures.keySet().stream().toList();
    }

    public static void createFromCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only a player may run this command");
            return;
        }

        String name = args[1];
        if (activeFixtures.containsKey(name)) {
            sender.sendMessage("A fixture with that name already exists");
            return;
        }

        Location loc = PositioningHelper.inFrontOfLookAt(player, 2).subtract(0, 1.7, 0);
        Fixture f = new Fixture(loc, name, true);
        f.spawn().syncAll().setCustomNameVisible(true, player);

        activeFixtures.put(name, f);
        EditorHandler.startEditMode(player);

        player.sendMessage("Fixture created, make sure to use '/rv fixture save' after you are done editing");
    }

    public static void createFromConfig(String name, ConfigurationSection conf) {
        Fixture f = new Fixture(name, conf);
        f.spawn().syncAll();

        activeFixtures.put(name, f);
    }

    public static void removeFromCommand(CommandSender sender, String[] args) {
        String name = args[1];
        if (!activeFixtures.containsKey(name)) {
            sender.sendMessage("No fixture with that name found");
            return;
        }

        activeFixtures.get(name).turnOff().deSpawn();
        activeFixtures.remove(name);

        if (sender instanceof Player player) EditorHandler.startEditMode(player);
        sender.sendMessage("Fixture removed, make sure to use '/rv fixture save' after you are done editing");
    }

    public static void toggleFromCommand(CommandSender sender, String[] args) {
        Util.runIfNotNull(activeFixtures.get(args[1]), f -> toggle(f, args[0].equals("start")), () -> sender.sendMessage("Fixture not found"));
    }

    public static void toggle(Fixture fixture, boolean turnOn) {
        if (turnOn) fixture.turnOn();
        else { //disable sequences and motion
            fixture.turnOff();
            MotionHandler.stopMotion(fixture);
            SequenceHandler.stopSequence(fixture, true);
        }
    }

    public static void forEach(Consumer<Fixture> consumer) {
        activeFixtures.values().forEach(consumer);
    }

    public static void save(CommandSender sender) {
        if (sender instanceof Player player) EditorHandler.stopEditMode(player);
        FixtureConfig.save();
        reload();
        GroupHandler.reload(); //in case fixture got removed
    }

    public static void despawnAll() {
        activeFixtures.forEach((s, f) -> {
            f.turnOff().deSpawn();
        });

        activeFixtures.clear();
    }

    public static void reload() {
        FixtureHandler.despawnAll();
        FixtureConfig.init();
    }

    public static YamlConfiguration createConfig() {
        YamlConfiguration conf = new YamlConfiguration();

        activeFixtures.forEach((name, fixture) -> {
            ConfigurationSection section = conf.createSection(name);
            fixture.saveToConfig(section);
        });

        return conf;
    }
}

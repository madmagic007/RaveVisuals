package me.madmagic.ravevisuals.config.motion;

import me.madmagic.ravevisuals.Main;
import me.madmagic.ravevisuals.ents.Fixture;
import me.madmagic.ravevisuals.handlers.PositioningHelper;
import me.madmagic.ravevisuals.handlers.sequences.MotionPlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class MotionConfig {

    private static final File file = new File(Main.instance.getDataFolder(), "motions.yml");

    public static void init() {
        try {
            file.createNewFile();
        } catch (Exception ignored) {}
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        config.getKeys(false).forEach(name -> MotionPlayer.motions.put(name, new Motion(config.getConfigurationSection(name), name)));
    }
}

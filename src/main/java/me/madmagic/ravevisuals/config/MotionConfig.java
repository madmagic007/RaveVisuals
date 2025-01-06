package me.madmagic.ravevisuals.config;

import me.madmagic.ravevisuals.Main;
import me.madmagic.ravevisuals.handlers.sequences.MotionHandler;
import me.madmagic.ravevisuals.instances.motion.Motion;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MotionConfig {

    private static final File file = new File(Main.instance.getDataFolder(), "motions.yml");

    public static void init() {
        try {
            file.createNewFile();
        } catch (Exception ignored) {}

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.getKeys(false).forEach(name -> MotionHandler.add(name, new Motion(config.getConfigurationSection(name), name)));
    }
}

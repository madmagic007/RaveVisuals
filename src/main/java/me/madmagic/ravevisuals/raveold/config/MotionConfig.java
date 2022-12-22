package me.madmagic.ravevisuals.raveold.config;

import me.madmagic.ravevisuals.Main;
import me.madmagic.ravevisuals.raveold.handlers.fixtures.FixtureAnim;
import me.madmagic.ravevisuals.raveold.handlers.fixtures.Motion;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MotionConfig {

    private static final File file = new File(Main.instance.getDataFolder(), "motions.yml");

    public static void init() {
        try {
            file.createNewFile();
        } catch (Exception ignored) {}
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        config.getKeys(false).forEach(name -> FixtureAnim.motions.put(name, new Motion(config.getConfigurationSection(name), name)));
    }
}

package me.madmagic.ravevisuals.config;

import me.madmagic.ravevisuals.Main;
import me.madmagic.ravevisuals.handlers.FixtureHandler;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FixtureConfig {

    private static final File file = new File(Main.instance.getDataFolder(), "fixtures.yml");

    public static void init() {
        try {
            file.createNewFile();
        } catch (Exception ignored) {}

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.getKeys(false).forEach(name -> FixtureHandler.createFromConfig(name, config.getConfigurationSection(name)));
    }

    public static void save() {
        try {
            FixtureHandler.createConfig().save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

package me.madmagic.ravevisuals.config;

import me.madmagic.ravevisuals.Main;
import me.madmagic.ravevisuals.instances.scenes.Scene;
import me.madmagic.ravevisuals.handlers.sequences.SceneHandler;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class SceneConfig {

    private static final File file = new File(Main.instance.getDataFolder(), "scenes.yml");

    public static void init() {
        try {
            file.createNewFile();
        } catch (Exception ignored) {}

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.getKeys(false).forEach(key -> SceneHandler.addScene(key, new Scene(config.getConfigurationSection(key))));
    }
}

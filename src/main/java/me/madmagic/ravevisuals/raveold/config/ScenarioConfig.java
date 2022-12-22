package me.madmagic.ravevisuals.raveold.config;

import me.madmagic.ravevisuals.Main;
import me.madmagic.ravevisuals.raveold.handlers.scenarios.Scenario;
import me.madmagic.ravevisuals.raveold.handlers.scenarios.ScenarioHandler;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ScenarioConfig {

    private static final File file = new File(Main.instance.getDataFolder(), "scenarios.yml");

    public static void init() {
        try {
            file.createNewFile();
        } catch (Exception ignored) {}
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        config.getKeys(false).forEach(name -> ScenarioHandler.scenarios.put(name, new Scenario(config.getConfigurationSection(name))));
    }
}

package me.madmagic.ravevisuals.config;

import me.madmagic.ravevisuals.Main;
import me.madmagic.ravevisuals.handlers.anim.StateHandler;
import me.madmagic.ravevisuals.instances.State;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class StateConfig {

    private static final File file = new File(Main.instance.getDataFolder(), "states.yml");

    public static void init() {
        try {
            file.createNewFile();
        } catch (Exception ignored) {}

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.getKeys(false).forEach(key -> StateHandler.addState(key, new State(config.getConfigurationSection(key))));
    }
}

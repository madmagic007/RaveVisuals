package me.madmagic.ravevisuals.config;

import me.madmagic.ravevisuals.Main;
import me.madmagic.ravevisuals.handlers.VarHandler;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class VarConfig {

    private static final File file = new File(Main.instance.getDataFolder(), "vars.yml");

    public static void init() {
        try {
            file.createNewFile();
        } catch (Exception ignored) {}

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.getKeys(false).forEach(key -> VarHandler.registerVar(key, config.get(key)));
    }
}

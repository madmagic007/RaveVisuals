package me.madmagic.ravevisuals.config;

import me.madmagic.ravevisuals.Main;
import me.madmagic.ravevisuals.handlers.CommandsHandler;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class CommandsConfig {

    private static final File file = new File(Main.instance.getDataFolder(), "commands.yml");

    public static void init() {
        try {
            file.createNewFile();
        } catch (Exception ignored) {}

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.getKeys(false).forEach(name -> CommandsHandler.createFromConfig(name, config.getStringList(name)));
    }
}

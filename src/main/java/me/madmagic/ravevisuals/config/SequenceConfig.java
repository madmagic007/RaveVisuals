package me.madmagic.ravevisuals.config;

import me.madmagic.ravevisuals.Main;
import me.madmagic.ravevisuals.handlers.anim.SequenceHandler;
import me.madmagic.ravevisuals.instances.sequence.Sequence;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class SequenceConfig {

    private static final File file = new File(Main.instance.getDataFolder(), "sequences.yml");

    public static void init() {
        try {
            file.createNewFile();
        } catch (Exception ignored) {}

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.getKeys(false).forEach(key -> SequenceHandler.addSequence(key, new Sequence(config.getConfigurationSection(key))));
    }
}

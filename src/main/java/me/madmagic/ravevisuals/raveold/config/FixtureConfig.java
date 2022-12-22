package me.madmagic.ravevisuals.raveold.config;

import me.madmagic.ravevisuals.Main;
import me.madmagic.ravevisuals.raveold.fixture.Effect;
import me.madmagic.ravevisuals.raveold.handlers.fixtures.FixtureHandler;
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

    public static void saveAll() {
        YamlConfiguration config = new YamlConfiguration();
        FixtureHandler.activeFixtures.forEach((s, f) -> {
            String p = s + ".";
            config.set(p + "location", f.location);

            config.set(p + "effect", f.effect.effect.name().toLowerCase());
            config.set(p + "head", f.showHead);

            p += "particle.";
            Effect e = f.effect;
            config.set(p + "particle", e.particle.name().toLowerCase());
            config.set(p + "shape", e.shape.toString().toLowerCase());
            config.set(p + "color", String.format("#%06X", (0xFFFFFF & e.col.getRGB())));
            config.set(p + "direction", e.dirToString());
            config.set(p + "speed", e.speed);
            config.set(p + "amount", e.amount);
            config.set(p + "length", e.length);

        });

        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

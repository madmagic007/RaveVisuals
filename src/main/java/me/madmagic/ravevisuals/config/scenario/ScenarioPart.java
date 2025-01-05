package me.madmagic.ravevisuals.config.scenario;

import me.madmagic.ravevisuals.Util;
import me.madmagic.ravevisuals.config.fixture.Effect;
import me.madmagic.ravevisuals.ents.Fixture;
import me.madmagic.ravevisuals.handlers.GroupHandler;
import me.madmagic.ravevisuals.handlers.sequences.MotionPlayer;
import me.madmagic.ravevisuals.handlers.FixtureHandler;
import org.bukkit.configuration.ConfigurationSection;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ScenarioPart {

    private final Map<String, ConfigurationSection> fixtures = new HashMap<>();
    private final Map<String, ConfigurationSection> groups = new HashMap<>();
    public final int afterDelay;

    public ScenarioPart(ConfigurationSection config) {
        afterDelay = config.getInt("delay");

        ConfigurationSection fixtures = config.getConfigurationSection("fixtures");
        Util.runIfNotNull(fixtures, fList -> fList.getKeys(false).forEach(fName -> {
            this.fixtures.put(fName, fList.getConfigurationSection(fName));
        }));

        ConfigurationSection groups = config.getConfigurationSection("groups");
        Util.runIfNotNull(groups, gList -> gList.getKeys(false).forEach(gName -> {
            this.groups.put(gName, gList.getConfigurationSection(gName));
        }));
    }

    public void run() {
        fixtures.forEach(this::runOnFixture);
        groups.forEach((name, config) -> Util.runIfNotNull(GroupHandler.groups.get(name), l -> l.forEach(f -> runOnFixture(f, config))));
    }

    private void runOnFixture(String name, ConfigurationSection config) {
        Util.runIfNotNull(FixtureHandler.activeFixtures.get(name), f -> runOnFixture(f, config));
    }

    private void runOnFixture(Fixture fixture, ConfigurationSection config) {
        if (config.isBoolean("active")) {
            if (config.getBoolean("active")) fixture.turnOn();
            else fixture.turnOff();
        }

        Util.runIfNotNull(config.getString("effect"), effect -> {
            if (effect != null) {
                try {
                    Effect.EffectType type = Effect.EffectType.valueOf(effect.toUpperCase());
                    fixture.changeBeamTypeRunning(type);
                } catch (Exception ignored) {}
            }
        });

        Util.runIfNotNull(config.getString("color"), color -> fixture.effect.col = Color.decode(color));

        Util.runIfNotNull(config.getString("motion"), mot -> MotionPlayer.startMotion(fixture, mot));
    }
}

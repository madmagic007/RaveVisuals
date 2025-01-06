package me.madmagic.ravevisuals.instances.scenes;

import me.madmagic.ravevisuals.Util;
import me.madmagic.ravevisuals.ents.Fixture;
import me.madmagic.ravevisuals.handlers.FixtureHandler;
import me.madmagic.ravevisuals.handlers.GroupHandler;
import me.madmagic.ravevisuals.handlers.anim.StateHandler;
import me.madmagic.ravevisuals.instances.State;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScenePart {

    private final Map<Fixture, State> fixtures = new HashMap<>();
    private final Map<List<Fixture>, State> groups = new HashMap<>();
    public final int afterDelay;

    public ScenePart(ConfigurationSection config) {
        afterDelay = config.getInt("delay");
        config.set("delay", null); //remove key, so iteration below doesn't try to find fixture/group with name "delay"

        config.getKeys(false).forEach(key -> { //key is fixture or group name
            ConfigurationSection stateConfig = config.getConfigurationSection(key);
            State state;

            if (stateConfig.contains("state"))
                state = StateHandler.getByName(stateConfig.getString("state"));
            else
                state = new State(stateConfig);

            Util.runIfNotNull(FixtureHandler.getByName(key), fixture ->
                    fixtures.put(fixture, state)
            );

            Util.runIfNotNull(GroupHandler.getByName(key), list ->
                    groups.put(list, state)
            );
        });
    }

    public void run() {
        fixtures.forEach(this::runOnFixture);
        groups.forEach((list, s) -> list.forEach(f -> runOnFixture(f, s)));
    }

    private void runOnFixture(Fixture fixture, State state) {
        state.applyTo(fixture);
    }
}

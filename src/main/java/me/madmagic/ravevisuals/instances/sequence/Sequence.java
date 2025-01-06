package me.madmagic.ravevisuals.instances.sequence;

import me.madmagic.ravevisuals.Util;
import me.madmagic.ravevisuals.ents.Fixture;
import me.madmagic.ravevisuals.handlers.anim.StateHandler;
import me.madmagic.ravevisuals.instances.State;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class Sequence {

    public List<SequencePart> sequenceParts = new ArrayList<>();
    private State initialState;
    public int repetitions;

    public Sequence(ConfigurationSection config) {
        if (config == null) return;

        repetitions = config.getInt("repetitions");

        ConfigurationSection initialStateConfig = config.getConfigurationSection("initialState");
        Util.runIfNotNull(initialStateConfig, stateConfig -> {
            if (stateConfig.contains("state"))
                initialState = StateHandler.getByName(config.getString("state"));
            else
                initialState = new State(stateConfig);
        });

        ConfigurationSection sequenceConfig = config.getConfigurationSection("sequence");
        Util.runIfNotNull(sequenceConfig, sceneConfig ->
                sceneConfig.getKeys(false).forEach(key ->
                        sequenceParts.add(new SequencePart(sceneConfig.getConfigurationSection(key)))
                )
        );
    }

    public void applyInitialStateToIfDefined(Fixture fixture) {
        if (initialState == null) return;

        initialState.applyTo(fixture);
    }
}

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
    private State finalState;
    public int repetitions;

    public Sequence(ConfigurationSection config) {
        if (config == null) return;

        repetitions = config.getInt("repetitions");

        ConfigurationSection initialStateConfig = config.getConfigurationSection("initial");
        Util.runIfNotNull(initialStateConfig, stateConfig -> {
            if (stateConfig.contains("state"))
                initialState = StateHandler.getByName(stateConfig.getString("state"));
            else
                initialState = new State(stateConfig);
        });

        ConfigurationSection finalStateConfig = config.getConfigurationSection("final");
        Util.runIfNotNull(finalStateConfig, stateConfig -> {
            if (stateConfig.contains("state"))
                finalState = StateHandler.getByName(stateConfig.getString("state"));
            else
                finalState = new State(stateConfig);
        });

        ConfigurationSection sequenceConfig = config.getConfigurationSection("sequence");
        Util.runIfNotNull(sequenceConfig, sceneConfig ->
                sceneConfig.getKeys(false).forEach(key ->
                        sequenceParts.add(new SequencePart(sceneConfig.getConfigurationSection(key)))
                )
        );
    }

    public void applyInitialStateToIfDefined(Fixture fixture) {
        Util.runIfNotNull(initialState, s -> s.applyTo(fixture));
    }

    public void applyFinalStateToIfDefined(Fixture fixture) {
        Util.runIfNotNull(finalState, s -> s.applyTo(fixture));
    }
}

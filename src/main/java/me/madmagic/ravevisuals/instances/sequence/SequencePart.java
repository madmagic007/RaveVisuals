package me.madmagic.ravevisuals.instances.sequence;

import me.madmagic.ravevisuals.ents.Fixture;
import me.madmagic.ravevisuals.handlers.VarHandler;
import me.madmagic.ravevisuals.handlers.anim.StateHandler;
import me.madmagic.ravevisuals.instances.State;
import me.madmagic.ravevisuals.instances.VarInstance;
import org.bukkit.configuration.ConfigurationSection;

public class SequencePart {

    private final State state;
    public final VarInstance afterDelay;

    public SequencePart(ConfigurationSection config) {
        afterDelay = VarHandler.createFromConfig(config, "delay");

        if (config.contains("state"))
            state = StateHandler.getByName(config.getString("state"));
        else
            state = new State(config);
    }

    public void runOn(Fixture fixture) {
        state.applyTo(fixture);
    }
}

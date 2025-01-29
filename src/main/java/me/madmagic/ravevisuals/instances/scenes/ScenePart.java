package me.madmagic.ravevisuals.instances.scenes;

import me.madmagic.ravevisuals.Util;
import me.madmagic.ravevisuals.ents.Fixture;
import me.madmagic.ravevisuals.handlers.CommandsHandler;
import me.madmagic.ravevisuals.handlers.FixtureHandler;
import me.madmagic.ravevisuals.handlers.GroupHandler;
import me.madmagic.ravevisuals.handlers.VarHandler;
import me.madmagic.ravevisuals.handlers.anim.StateHandler;
import me.madmagic.ravevisuals.instances.State;
import me.madmagic.ravevisuals.instances.VarInstance;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class ScenePart {

    private final Map<Fixture, State> fixtures = new HashMap<>();
    private final Map<List<Fixture>, State> groups = new HashMap<>();
    private final Map<VarInstance, Object> vars = new HashMap<>();
    private List<String> commands = new ArrayList<>();
    public VarInstance afterDelay = new VarInstance(0);

    public ScenePart(ConfigurationSection config) {
        if (config.contains("delay")) {
            afterDelay = VarHandler.createFromConfig(config, "delay");
            config.set("delay", null); //remove key, so iteration below doesn't try to find fixture/group with name "delay"
        }

        Util.runIfNotNull(config.getConfigurationSection("vars"), varsSection ->
                varsSection.getKeys(false).forEach(varName -> {
                    Object value = varsSection.get(varName);

                    Util.runIfNotNull(VarHandler.getByName(varName), var ->
                            vars.put(var, value)
                    );
                })
        );
        config.set("vars", null); //remove key, so iteration below doesn't try to find fixture/group with name "vars"

        Util.runIfNotNull(config.get("commands"), commandsValue -> {
            if (commandsValue instanceof String name) {
                Util.runIfNotNull(CommandsHandler.getByName(name), commands::addAll, () -> commands.add(name));
            } else if (commandsValue instanceof List list) {
                list.forEach(o -> {
                    if (!(o instanceof String str)) return;

                    Util.runIfNotNull(CommandsHandler.getByName(str), commands::addAll, () -> commands.add(str));
                });
            }
        });
        config.set("commands", null); //remove key, so iteration below doesn't try to find fixture/group with name "commands"

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

        vars.forEach(VarHandler::setValue);

        CommandsHandler.runCommands(commands);
    }

    private void runOnFixture(Fixture fixture, State state) {
        state.applyTo(fixture);
    }
}

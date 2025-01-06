package me.madmagic.ravevisuals.handlers.anim;

import me.madmagic.ravevisuals.Util;
import me.madmagic.ravevisuals.config.StateConfig;
import me.madmagic.ravevisuals.ents.Fixture;
import me.madmagic.ravevisuals.handlers.FixtureHandler;
import me.madmagic.ravevisuals.handlers.GroupHandler;
import me.madmagic.ravevisuals.instances.State;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.List;

public class StateHandler {

    private static HashMap<String, State> states = new HashMap<>();

    public static State getByName(String name) {
        return states.get(name);
    }

    public static void addState(String name, State state) {
        states.put(name, state);
    }

    public static List<String> getLoadedStateNames() {
        return states.keySet().stream().toList();
    }

    public static void applyFromCommand(CommandSender sender, String[] args, boolean isGroup) {
        String name = args[1];
        String stateName = args[2];

        Util.runIfNotNull(states.get(stateName), m -> {
            if (isGroup) {
                Util.runIfNotNull(GroupHandler.getByName(name), l -> l.forEach(f -> applyTo(f, m)), () -> sender.sendMessage("Group not found"));
            } else {
                Util.runIfNotNull(FixtureHandler.getByName(name), f -> applyTo(f, m), () -> sender.sendMessage("Fixture not found"));
            }
        }, () -> sender.sendMessage("State not found"));
    }

    public static void applyTo(Fixture fixture, State state) {
        state.applyTo(fixture);
    }

    public static void reload() {
        states.clear();
        StateConfig.init();
    }
}

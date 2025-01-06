package me.madmagic.ravevisuals.handlers.sequences;

import me.madmagic.ravevisuals.instances.State;

import java.util.HashMap;

public class StateHandler {

    private static HashMap<String, State> states = new HashMap<>();

    public static State getByName(String name) {
        return states.get(name);
    }
}

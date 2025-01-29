package me.madmagic.ravevisuals.api;

import me.madmagic.ravevisuals.ents.Fixture;
import me.madmagic.ravevisuals.handlers.anim.StateHandler;
import me.madmagic.ravevisuals.instances.State;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApiSetInstance {

    private List<Fixture> fixtures = new ArrayList<>();
    private State state;

    public void addFixture(Fixture fixture) {
        fixtures.add(fixture);
    }

    public void tryDefineState(JSONObject post) {
        Object o = post.opt("state");

        if (o instanceof String)
            state = StateHandler.getByName(o.toString());
        else if (o instanceof JSONObject)
            state = new State((JSONObject) o);
    }

    public boolean isValid() {
        return !fixtures.isEmpty() && state != null;
    }

    public void apply() {
        fixtures.forEach(f ->
                state.applyTo(f)
        );
    }
}

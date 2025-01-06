package me.madmagic.ravevisuals.instances.motion;

import me.madmagic.ravevisuals.handlers.PositioningHelper;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Motion {

    public final String name;
    public final int delay;
    public final boolean relative;
    public final List<Vector> motions;

    public Motion(ConfigurationSection config, String name) {
        this.name = name;
        delay = config.getInt("delay");
        relative = config.getBoolean("relative");

        motions = new ArrayList<>();
        config.getStringList("motion").forEach(s ->
            motions.add(PositioningHelper.vecStringToVector(s))
        );
    }
}
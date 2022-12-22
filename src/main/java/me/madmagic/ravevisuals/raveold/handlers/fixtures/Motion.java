package me.madmagic.ravevisuals.raveold.handlers.fixtures;

import me.madmagic.ravevisuals.raveold.handlers.PositioningHelper;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Motion {

    public final String name;
    public final int delay;
    public final boolean relative;
    public final List<Vector> motion;

    public Motion(ConfigurationSection config, String name) {
        this.name = name;
        delay = config.getInt("delay");
        relative = config.getBoolean("relative");

        motion = new ArrayList<>();
        config.getStringList("motion").forEach(s -> {
            double[] split = Stream.of(s.split(" ")).mapToDouble(Double::parseDouble).toArray();
            motion.add(new Vector(PositioningHelper.fixRotation((float) split[0]), 0, PositioningHelper.fixRotation((float) split[1])));
        });
    }
}

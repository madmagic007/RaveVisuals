package me.madmagic.ravevisuals.instances.motion;

import me.madmagic.ravevisuals.handlers.PositioningHelper;
import me.madmagic.ravevisuals.handlers.VarHandler;
import me.madmagic.ravevisuals.instances.VarInstance;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Motion {

    public final String name;
    public final VarInstance delay;
    public final boolean relative;
    public final List<Vector> motions;

    public Motion(ConfigurationSection config, String name) {
        this.name = name;
        delay = VarHandler.createFromConfig(config, "delay");
        relative = config.getBoolean("relative");

        motions = new ArrayList<>();
        config.getStringList("motion").forEach(s ->
            motions.add(PositioningHelper.vecStringToVector(s))
        );
    }
}
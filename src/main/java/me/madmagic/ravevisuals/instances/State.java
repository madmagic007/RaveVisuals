package me.madmagic.ravevisuals.instances;

import me.madmagic.ravevisuals.Util;
import me.madmagic.ravevisuals.ents.Fixture;
import me.madmagic.ravevisuals.handlers.PositioningHelper;
import me.madmagic.ravevisuals.handlers.sequences.MotionHandler;
import me.madmagic.ravevisuals.instances.Effect.BeamType;
import org.bukkit.Color;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;

public class State {

    private Boolean enable; //yes I use class boolean here for a reason
    private BeamType beamType;
    private Vector rotation;
    private String motionName;
    private Color color;

    public State(ConfigurationSection config) {
        if (!config.contains("enable"))
            enable = null;
        else
            enable = config.getBoolean("enable");

        Util.runIfNotNull(config.getString("beamType"), beamTypeName ->
            Util.runIfNotNull(BeamType.valueOf(beamTypeName.toUpperCase()), effectType ->
                this.beamType = effectType
            )
        );

        Util.runIfNotNull(config.getString("color"), color -> {
            try {
                this.color = Util.hexToColor(color);
            } catch (Exception ignored) {}
        });

        motionName = config.getString("motionName");

        Util.runIfNotNull(config.getString("rotation"), rotationStr ->
            rotation = PositioningHelper.vecStringToVector(rotationStr)
        );
    }

    public void applyTo(Fixture fixture) {
        if (rotation != null && motionName != null) {
            fixture.setHeadPose((float) rotation.getX(), (float) rotation.getY())
                    .syncHeadPose();
        }

        Util.runIfNotNull(motionName, motionName ->
            MotionHandler.startMotion(fixture, motionName)
        );

        Util.runIfNotNull(beamType, fixture::changeBeamTypeRunning);

        Util.runIfNotNull(color, color -> fixture.effect.col = color);

        Util.runIfNotNull(enable, enable -> {
            if (enable)
                fixture.turnOn();
            else
                fixture.turnOff();
        });
    }
}

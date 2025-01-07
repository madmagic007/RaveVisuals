package me.madmagic.ravevisuals.instances;

import me.madmagic.ravevisuals.Util;
import me.madmagic.ravevisuals.ents.Fixture;
import me.madmagic.ravevisuals.handlers.PositioningHelper;
import me.madmagic.ravevisuals.handlers.anim.MotionHandler;
import me.madmagic.ravevisuals.handlers.anim.SequenceHandler;
import me.madmagic.ravevisuals.instances.Effect.EffectType;
import me.madmagic.ravevisuals.instances.motion.Motion;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;
import org.json.JSONObject;

public class State {

    private Boolean enable; //yes I use class boolean here for a reason
    private EffectType effectType;
    private Particle particle;
    private Effect.ParticleShape shape;
    private Vector rotation;
    private Motion motion;
    private String sequenceName; //we need name because states load before sequences
    private Color color;

    public State(ConfigurationSection config) {
        if (!config.contains("enable"))
            enable = null;
        else
            enable = config.getBoolean("enable");

        Util.runIfNotNull(config.getString("effect"), effectTypeName ->
                Util.runIfNotNull(EffectType.valueOf(effectTypeName.toUpperCase()), effectType ->
                        this.effectType = effectType
                )
        );

        Util.runIfNotNull(config.getString("particle"), particleName ->
                Util.runIfNotNull(Particle.valueOf(particleName.toUpperCase()), particle ->
                        this.particle = particle
                )
        );

        Util.runIfNotNull(config.getString("shape"), shapeName ->
                Util.runIfNotNull(Effect.ParticleShape.valueOf(shapeName.toUpperCase()), shape ->
                        this.shape = shape
                )
        );

        Util.runIfNotNull(config.getString("color"), color -> {
            try {
                this.color = Util.hexToColor(color);
            } catch (Exception ignored) {            }
        });

        Util.runIfNotNull(config.getString("motion"), motionName ->
                Util.runIfNotNull(MotionHandler.getByName(motionName), motion ->
                        this.motion = motion
                )
        );

        Util.runIfNotNull(config.getString("rotation"), rotationStr ->
                rotation = PositioningHelper.vecStringToVector(rotationStr)
        );

        sequenceName = config.getString("sequence");
    }

    public State(JSONObject o) {
        Util.runIfNotNull(o.opt("enable"), enable ->
                this.enable = (Boolean) enable
        );

        Util.runIfNotNull(o.optString("effect", null), effectTypeName ->
                Util.runIfNotNull(EffectType.valueOf(effectTypeName.toUpperCase()), effectType ->
                        this.effectType = effectType
                )
        );

        Util.runIfNotNull(o.optString("particle", null), particleName ->
                Util.runIfNotNull(Particle.valueOf(particleName.toUpperCase()), particle ->
                        this.particle = particle
                )
        );

        Util.runIfNotNull(o.optString("shape", null), shapeName ->
                Util.runIfNotNull(Effect.ParticleShape.valueOf(shapeName.toUpperCase()), shape ->
                        this.shape = shape
                )
        );

        Util.runIfNotNull(o.optString("color", null), color -> {
            try {
                this.color = Util.hexToColor(color);
            } catch (Exception ignored) {}
        });

        Util.runIfNotNull(o.optString("motion", null), motionName ->
                Util.runIfNotNull(MotionHandler.getByName(motionName), motion ->
                        this.motion = motion
                )
        );

        Util.runIfNotNull(o.optString("rotation", null), rotationStr ->
                rotation = PositioningHelper.vecStringToVector(rotationStr)
        );

        sequenceName = o.optString("sequence", null);
    }

    public void applyTo(Fixture fixture) {
        if (rotation != null && motion != null) {
            fixture.setHeadPose((float) rotation.getX(), (float) rotation.getY())
                    .syncHeadPose();
        }

        Util.runIfNotNull(motion, motion ->
            MotionHandler.startMotion(fixture, motion)
        );

        Util.runIfNotNull(effectType, fixture::changeEffectTypeRunning);

        Util.runIfNotNull(particle, particle -> fixture.effect.particle = particle);

        Util.runIfNotNull(shape, shape -> fixture.effect.shape = shape);

        Util.runIfNotNull(color, color -> fixture.effect.col = color);

        Util.runIfNotNull(sequenceName, sequenceName ->
                SequenceHandler.startSequence(fixture, sequenceName)
        );

        Util.runIfNotNull(enable, enable -> {
            if (enable)
                fixture.turnOn();
            else
                fixture.turnOff();
        });
    }
}

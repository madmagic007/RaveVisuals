package me.madmagic.ravevisuals.config.fixture;

import me.madmagic.ravevisuals.Main;
import me.madmagic.ravevisuals.ents.NMSGuardian;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Consumer;
import org.bukkit.util.Vector;

import java.awt.*;
import java.util.stream.Stream;

public class Effect {

    private BukkitTask task;
    public Location particleLocation;

    public EffectType effect = EffectType.GUARDIAN;
    //public ParticleEffect particle = ParticleEffect.REDSTONE;
    public ParticleShape shape = ParticleShape.LINE;
    public Color col = Color.decode("#0071fd");
    public Vector directionModifier = new Vector(0, 0, 0);
    public double speed = 0;
    public int amount = 0;
    public double length = 16;

    public NMSGuardian guardian;
    public static Effect fromConfig(ConfigurationSection config) {
        Effect effect = new Effect();
        effect.effect = EffectType.valueOf(config.getString("effect").toUpperCase());

        config = config.getConfigurationSection("particle");

        //particle = ParticleEffect.valueOf(config.getString("particle").toUpperCase());
        effect.shape = ParticleShape.valueOf(config.getString("shape").toUpperCase());
        effect.col = Color.decode(config.getString("color"));
        double[] split = Stream.of(config.getString("direction").split(";")).mapToDouble(Double::parseDouble).toArray();
        effect.directionModifier = new Vector(split[0], split[1], split[2]);
        effect.speed = config.getDouble("speed");
        effect.amount = config.getInt("amount");
        effect.length = config.getDouble("length");

        return effect;
    }

    public String dirToString() {
        return directionModifier.getX() + ";" + directionModifier.getY() + ";" + directionModifier.getZ();
    }

    public void start(Location location) {
        stop();

        if (effect.equals(EffectType.GUARDIAN)) {
            if (guardian == null) guardian = new NMSGuardian(location, length);
            guardian.spawn();
            return;
        } else {
            particleLocation = location;
        }

        task = new BukkitRunnable() {
            @Override
            public void run() {
                shape.action.accept(Effect.this);
            }
        }.runTaskTimerAsynchronously(Main.instance, 0, 0);
    }

    private void createLine() {
//        Vector direction = particleLocation.getDirection();
//        for (double i = 0.1; i < length; i+= 0.3) {
//            direction.multiply(i);
//            particleLocation.add(direction);
//            ParticleBuilder pb = new ParticleBuilder(particle, particleLocation)
//                    .setColor(col)
//                    .setAmount(amount)
//                    .setOffset(directionModifier)
//                    .setSpeed((float) speed);
//            pb.display();
//            particleLocation.subtract(direction);
//            direction.normalize();
//        }
    }

    private double radius = 0;
    private void createCircle() {
//        double amt = 90 * amount;
//
//        for (double i = .2; i < amt; i++) {
//            double x = radius * Math.cos(i);
//            double y = radius * Math.sin(i);
//            Vector dir = new Vector(x, 0, y);
//            particleLocation.add(dir);
//
//            ParticleBuilder pb = new ParticleBuilder(particle, particleLocation)
//                    .setColor(col)
//                    .setOffset(dir)
//                    .setSpeed((float) speed);
//
//            pb.display();
//
//            particleLocation.subtract(dir);
//        }
//
//        radius += .2;
//        if (radius > length) radius = 0;
    }

    public void stop() {
        if (guardian != null) {
            guardian.deSpawn();
            guardian = null;
        }
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    public void setGuardianTarget(Location location) {
        if (guardian != null) guardian.moveBeam(location, length);
    }

    public enum EffectType {
        GUARDIAN,
        PARTICLE
    }

    public enum ParticleShape {
        LINE(Effect::createLine),
        CIRCLE(Effect::createCircle);

        public final Consumer<Effect> action;
        ParticleShape(Consumer<Effect> action) {
            this.action = action;
        }
    }
}

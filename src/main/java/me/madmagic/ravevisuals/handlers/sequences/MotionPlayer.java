package me.madmagic.ravevisuals.handlers.sequences;

import me.madmagic.ravevisuals.Util;
import me.madmagic.ravevisuals.config.motion.MotionConfig;
import me.madmagic.ravevisuals.config.motion.MotionConfig.Motion;
import me.madmagic.ravevisuals.config.motion.MotionConfig.Motion.MotionInstance;
import me.madmagic.ravevisuals.ents.Fixture;
import me.madmagic.ravevisuals.handlers.FixtureHandler;
import me.madmagic.ravevisuals.handlers.GroupHandler;
import me.madmagic.ravevisuals.handlers.PositioningHelper;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MotionPlayer {

    public static final Map<String, Motion> motions = new HashMap<>();
    private static final HashMap<Fixture, MotionInstance> activeMotions = new HashMap<>();

    private static BukkitTask motionTask;

    public static void startFromCommand(CommandSender sender, String[] args, boolean isGroup) {
        String name = args[1];
        String motionName = args[2];

        Util.runIfNotNull(motions.get(motionName), m -> {
            if (isGroup) {
                Util.runIfNotNull(GroupHandler.groups.get(name), l -> startMotion(l, m), () -> sender.sendMessage("Group not found"));
            } else {
                Util.runIfNotNull(FixtureHandler.activeFixtures.get(name), f -> startMotion(f, m), () -> sender.sendMessage("Fixture not found"));
            }
        }, () -> sender.sendMessage("Motion not found"));
    }

    public static void stopFromCommand(CommandSender sender, String[] args, boolean isGroup) {
        String name = args[1];
        if (isGroup) {
            Util.runIfNotNull(GroupHandler.groups.get(name), l -> l.forEach(MotionPlayer::stopMotion), () -> sender.sendMessage("Group not found"));
        } else {
            Util.runIfNotNull(FixtureHandler.activeFixtures.get(name), MotionPlayer::stopMotion, () -> sender.sendMessage("Fixture not found"));
        }
    }

    public static void startMotion(Fixture fixture, String motionName) {
        Util.runIfNotNull(motions.get(motionName), m -> startMotion(fixture, m));
    }

    public static void startMotion(List<Fixture> fixtures, Motion motion) {
        fixtures.forEach(f -> startMotion(f, motion));
    }

    public static void startMotion(Fixture fixture, Motion motion) {
        if (motion.motions.isEmpty()) return;
        fixture.turnOn();

        MotionInstance motionInstance = new MotionInstance(fixture, motion);

        if (!motion.relative) {
            motionInstance.curPos = 1;
            Vector initPos = motion.motions.get(0);
            fixture.setHeadPose((float) initPos.getX(), (float) initPos.getY()).syncMetaData();
        }

        motionInstance.startTask();
        activeMotions.put(fixture, motionInstance);
    }

    public static void stopMotion(Fixture fixture) {
        Util.runIfNotNull(activeMotions.get(fixture), m -> {
            m.cancelTask();
            activeMotions.remove(fixture);
        });
    }

    public static void reload() {
        MotionConfig.init();
    }

    public static void nextMotionStep(MotionInstance motionInstance) {
        Vector dest = motionInstance.getNextVector();
        Fixture fixture = motionInstance.fixture;
        Motion motion = motionInstance.motion;

        if (motion.relative) {
            dest.setX(dest.getX() + fixture.restYaw);
            dest.setZ(dest.getZ() + fixture.restPitch);
        }

        Location location = fixture.getLocation();

        int ticks = motion.delay;
        float stepYaw = (float) ((dest.getX() - location.getYaw()) / ticks);
        float stepPitch = (float) ((dest.getZ() - location.getPitch()) / ticks);

        int curTick = 0;
        while (curTick < (ticks - 1)) {
            float newPitch = PositioningHelper.fixRotation(location.getPitch() + stepPitch);
            float newYaw = PositioningHelper.fixRotation(location.getYaw() + stepYaw);

            fixture.setHeadPose(newYaw, newPitch).syncHeadPose();

            curTick++;
            try {
                TimeUnit.MILLISECONDS.sleep(1000 / 20);
                if (!motionInstance.run) return;
            } catch (Exception ignored) {}
        }

        //force move in case of any rounding errors
        fixture.setHeadPose((float) dest.getX(), (float) dest.getZ())
                .syncHeadPose();
    }
}

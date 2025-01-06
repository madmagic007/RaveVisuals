package me.madmagic.ravevisuals.handlers.sequences;

import me.madmagic.ravevisuals.Util;
import me.madmagic.ravevisuals.config.MotionConfig;
import me.madmagic.ravevisuals.ents.Fixture;
import me.madmagic.ravevisuals.handlers.FixtureHandler;
import me.madmagic.ravevisuals.handlers.GroupHandler;
import me.madmagic.ravevisuals.instances.motion.Motion;
import me.madmagic.ravevisuals.instances.motion.MotionInstance;
import org.bukkit.command.CommandSender;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MotionHandler {

    private static final Map<String, Motion> motions = new HashMap<>();
    private static final HashMap<Fixture, MotionInstance> activeMotions = new HashMap<>();

    public static void add(String name, Motion motion) {
        motions.put(name, motion);
    }

    public static List<String> getLoadedMotionNames() {
        return motions.keySet().stream().toList();
    }

    public static void startFromCommand(CommandSender sender, String[] args, boolean isGroup) {
        String name = args[1];
        String motionName = args[2];

        Util.runIfNotNull(motions.get(motionName), m -> {
            if (isGroup) {
                Util.runIfNotNull(GroupHandler.getByName(name), l -> l.forEach(f -> startMotion(f, m)), () -> sender.sendMessage("Group not found"));
            } else {
                Util.runIfNotNull(FixtureHandler.getByName(name), f -> startMotion(f, m), () -> sender.sendMessage("Fixture not found"));
            }
        }, () -> sender.sendMessage("Motion not found"));
    }

    public static void stopFromCommand(CommandSender sender, String[] args, boolean isGroup) {
        String name = args[1];

        if (isGroup) {
            Util.runIfNotNull(GroupHandler.getByName(name), l -> l.forEach(MotionHandler::stopMotion), () -> sender.sendMessage("Group not found"));
        } else {
            Util.runIfNotNull(FixtureHandler.getByName(name), MotionHandler::stopMotion, () -> sender.sendMessage("Fixture not found"));
        }
    }

    public static void startMotion(Fixture fixture, String motionName) {
        Util.runIfNotNull(motions.get(motionName), m -> startMotion(fixture, m));
    }

    public static void startMotion(Fixture fixture, Motion motion) {
        if (motion.motions.isEmpty()) return;
        fixture.turnOn();

        Util.runIfNotNull(activeMotions.get(fixture), MotionInstance::stop);

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
            m.stop();
            activeMotions.remove(fixture);
        });
    }

    public static void reload() {
        MotionConfig.init();
    }
}

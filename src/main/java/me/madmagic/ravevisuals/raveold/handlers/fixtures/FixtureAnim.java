package me.madmagic.ravevisuals.raveold.handlers.fixtures;

import me.madmagic.ravevisuals.Util;
import me.madmagic.ravevisuals.raveold.config.MotionConfig;
import me.madmagic.ravevisuals.raveold.fixture.Fixture;
import me.madmagic.ravevisuals.raveold.handlers.GroupHandler;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FixtureAnim {

    public static Map<String, Motion> motions = new HashMap<>();

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
            Util.runIfNotNull(GroupHandler.groups.get(name), l -> l.forEach(Fixture::turnOff), () -> sender.sendMessage("Group not found"));
        } else {
            Util.runIfNotNull(FixtureHandler.activeFixtures.get(name), Fixture::turnOff, () -> sender.sendMessage("Fixture not found"));
        }
    }

    public static void startMotion(Fixture fixture, String motionName) {
        Util.runIfNotNull(motions.get(motionName), m -> startMotion(fixture, m));
    }

    public static void startMotion(List<Fixture> fixtures, Motion motion) {
        fixtures.forEach(f -> startMotion(f, motion));
    }

    public static void startMotion(Fixture fixture, Motion motion) {
        fixture.startMotion(motion);
    }

    public static void start(String name) {
        Util.runIfNotNull(FixtureHandler.activeFixtures.get(name), Fixture::turnOn);
    }

    public static void stop(String name) {
            Util.runIfNotNull(FixtureHandler.activeFixtures.get(name), Fixture::turnOff);
    }

    public static void reload() {
        MotionConfig.init();
    }
}

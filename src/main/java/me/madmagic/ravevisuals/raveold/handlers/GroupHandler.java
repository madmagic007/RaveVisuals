package me.madmagic.ravevisuals.raveold.handlers;

import me.madmagic.ravevisuals.Util;
import me.madmagic.ravevisuals.commands.subcommands.GroupSubCommand;
import me.madmagic.ravevisuals.raveold.config.GroupConfig;
import me.madmagic.ravevisuals.raveold.fixture.Effect;
import me.madmagic.ravevisuals.raveold.fixture.Fixture;
import me.madmagic.ravevisuals.raveold.handlers.fixtures.FixtureHandler;
import org.bukkit.command.CommandSender;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupHandler {

    public static Map<String, List<Fixture>> groups = new HashMap<>();

    public static void deleteGroup(CommandSender sender, String[] args) {
        String gName = args[1];

        Util.runIfNotNull(groups.get(gName), g -> {
            groups.remove(gName);
            sender.sendMessage("Group deleted");
        }, () -> {
            sender.sendMessage("Group not found");
        });
    }

    public static void addOrRemove(CommandSender sender, String[] args, boolean add, boolean createNew) {
        String gName = args[1];
        List<String> fNames = GroupSubCommand.getFixtureNamesInCommand(args, 2);
        List<Fixture> list = groups.get(gName);

        if (createNew) list = Util.listNotNull(list);
        else {
            list = Util.listOnlyNullCheck(list, () -> sender.sendMessage("Group not found"));
            if (list == null) return;
        }

        List<Fixture> finalList = list;
        fNames.forEach(fName -> Util.runIfNotNull(FixtureHandler.activeFixtures.get(fName), f -> {
            if (add && !finalList.contains(f)) {
                finalList.add(f);
                sender.sendMessage("Fixture added to group");
            }
            else {
                finalList.remove(f);
                sender.sendMessage("Fixture removed from group");
            }
        }));
        groups.put(gName, list);
    }

    public static void reload() {
        groups.clear();
        GroupConfig.init();
    }

    public static void toggleAllFromCommand(CommandSender sender, String[] args) {
        Util.runIfNotNull(groups.get(args[1]), g -> toggleAll(g, args[0].equals("start")), () -> sender.sendMessage("Group not found"));
    }

    public static void toggleAll(String groupName, boolean on) {
        toggleAll(groups.get(groupName), on);
    }

    public static void toggleAll(List<Fixture> list, boolean on) {
        if (list == null) return;
        list.forEach(fixture -> {
            if (on) fixture.turnOn();
            else fixture.turnOff();
        });
    }

    public static void setAllColor(String groupName, Color color) {
        Util.runIfNotNull(groups.get(groupName), g -> g.forEach(f -> f.effect.col = color));
    }

    public static void setAllBeamType(String groupName, Effect.EffectType beamType) {
        Util.runIfNotNull(groups.get(groupName), g -> g.forEach(f -> f.changeBeamTypeRunning(beamType)));
    }

    public static void setAllHeadPose(String groupName, float pitch, float yaw) {
        Util.runIfNotNull(groups.get(groupName), g -> g.forEach(f -> f.setHeadPose(pitch, yaw)));
    }
}

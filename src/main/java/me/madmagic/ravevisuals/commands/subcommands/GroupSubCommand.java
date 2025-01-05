package me.madmagic.ravevisuals.commands.subcommands;

import me.madmagic.ravevisuals.config.GroupConfig;
import me.madmagic.ravevisuals.ents.Fixture;
import me.madmagic.ravevisuals.handlers.GroupHandler;
import me.madmagic.ravevisuals.handlers.sequences.MotionPlayer;
import me.madmagic.ravevisuals.handlers.FixtureHandler;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public class GroupSubCommand extends SubCommand {
    public GroupSubCommand() {
        super("group");
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        switch (args[0]) {
            case "create" -> GroupHandler.addOrRemove(sender, args, true, true);
            case "delete" -> GroupHandler.deleteGroup(sender, args);
            case "addfixture" -> GroupHandler.addOrRemove(sender, args, true, false);
            case "removefixture" -> GroupHandler.addOrRemove(sender, args, false, false);
            case "start", "stop" -> GroupHandler.toggleAllFromCommand(sender, args);
            case "startmotion" -> MotionPlayer.startFromCommand(sender, args, true);
            case "stopmotion" -> MotionPlayer.stopFromCommand(sender, args, true);
        }

        GroupConfig.save();
    }

    @Override
    public List<String> getTabCompletions(String path) {
        String[] split = path.split("\\.");

        if (path.startsWith("group.startmotion") && path.split("\\.").length == 3) return MotionPlayer.motions.keySet().stream().toList();
        if (path.startsWith("group.stopmotion") && path.split("\\.").length == 3) return MotionPlayer.motions.keySet().stream().toList();
        if (path.startsWith("group.addfixture") && path.split("\\.").length >= 3) return getAvailableFixtures(split);
        if (path.startsWith("group.create") && path.split("\\.").length >= 3) return getExistingNoDuplicates(split);
        if (path.startsWith("group.removefixture") && path.split("\\.").length >= 3) {
            List<String> names = new ArrayList<>();
            List<Fixture> fixturesInGroup = GroupHandler.groups.get(split[2]);
            if (fixturesInGroup != null) fixturesInGroup.forEach(f -> names.add(f.name));
            names.removeAll(getFixtureNamesInCommand(split, 3));
            return names;
        }

        if (split.length < 5) return GroupHandler.groups.keySet().stream().toList();
        return super.getTabCompletions(path);
    }

    public static List<String> getFixtureNamesInCommand(String[] splitPath, int from) {
        return new ArrayList<>(new LinkedHashSet<>(List.of(Arrays.copyOfRange(splitPath, from, splitPath.length))));
    }

    private static List<String> getExistingNoDuplicates(String[] splitPath) {
        List<String> available = new ArrayList<>(FixtureHandler.activeFixtures.keySet());
        available.removeAll(getFixtureNamesInCommand(splitPath, 2));
        return available;
    }

    private static List<String> getAvailableFixtures(String[] splitPath) {
        List<String> available = new ArrayList<>(FixtureHandler.activeFixtures.keySet());
        List<String> fNamesInCommand = getFixtureNamesInCommand(splitPath, 3);
        String groupName = splitPath[2];
        if (GroupHandler.groups.containsKey(groupName)) {
            GroupHandler.groups.get(groupName).forEach(fixture -> available.remove(fixture.name));
        }

        available.removeAll(fNamesInCommand);
        return available;
    }
}

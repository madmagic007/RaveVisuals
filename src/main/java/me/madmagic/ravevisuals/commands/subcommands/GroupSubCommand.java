package me.madmagic.ravevisuals.commands.subcommands;

import me.madmagic.ravevisuals.Util;
import me.madmagic.ravevisuals.config.GroupConfig;
import me.madmagic.ravevisuals.ents.Fixture;
import me.madmagic.ravevisuals.handlers.FixtureHandler;
import me.madmagic.ravevisuals.handlers.GroupHandler;
import me.madmagic.ravevisuals.handlers.anim.MotionHandler;
import me.madmagic.ravevisuals.handlers.anim.SequenceHandler;
import me.madmagic.ravevisuals.handlers.anim.StateHandler;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
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
            case "startmotion" -> MotionHandler.startFromCommand(sender, args, true);
            case "stopmotion" -> MotionHandler.stopFromCommand(sender, args, true);
            case "startsequence" -> SequenceHandler.startFromCommand(sender, args, true);
            case "stopsequence" -> SequenceHandler.stopFromCommand(sender, args, true);
            case "applystate" -> StateHandler.applyFromCommand(sender, args, true);
        }

        GroupConfig.save();
    }

    @Override
    public List<String> getTabCompletions(String path) {
        String[] split = path.split("\\.");

        if (path.startsWith("group.startmotion") && path.split("\\.").length == 3) return MotionHandler.getLoadedMotionNames();
        if (path.startsWith("group.startsequence") && path.split("\\.").length == 3) return SequenceHandler.getLoadedSequenceNames();
        if (path.startsWith("group.applystate") && split.length == 3) return StateHandler.getLoadedStateNames();
        if (path.startsWith("group.addfixture") && path.split("\\.").length >= 3) return getAvailableFixtures(split);
        if (path.startsWith("group.create") && path.split("\\.").length >= 3) return getExistingNoDuplicates(split);
        if (path.startsWith("group.removefixture") && path.split("\\.").length >= 3) {
            List<String> names = new ArrayList<>();
            List<Fixture> fixturesInGroup = GroupHandler.getByName(split[2]);
            if (fixturesInGroup != null) fixturesInGroup.forEach(f -> names.add(f.name));
            names.removeAll(getFixtureNamesInCommand(split, 3));
            return names;
        }

        if (split.length < 4) return GroupHandler.getLoadedGroupNames();
        return super.getTabCompletions(path);
    }

    public static List<String> getFixtureNamesInCommand(String[] splitPath, int from) {
        return new ArrayList<>(List.of(Arrays.copyOfRange(splitPath, from, splitPath.length)));
    }

    private static List<String> getExistingNoDuplicates(String[] splitPath) {
        List<String> available = new ArrayList<>(FixtureHandler.getLoadedFixtureNames());
        available.removeAll(getFixtureNamesInCommand(splitPath, 2));
        return available;
    }

    private static List<String> getAvailableFixtures(String[] splitPath) {
        List<String> available = new ArrayList<>(FixtureHandler.getLoadedFixtureNames());
        List<String> fNamesInCommand = getFixtureNamesInCommand(splitPath, 3);

        Util.runIfNotNull(GroupHandler.getByName(splitPath[2]), list -> list.forEach(f ->
            available.remove(f.name)
        ));

        available.removeAll(fNamesInCommand);
        return available;
    }
}

package me.madmagic.ravevisuals.handlers;

import me.madmagic.ravevisuals.Util;
import me.madmagic.ravevisuals.commands.subcommands.GroupSubCommand;
import me.madmagic.ravevisuals.config.GroupConfig;
import me.madmagic.ravevisuals.ents.Fixture;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GroupHandler {

    private static Map<String, List<Fixture>> groups = new LinkedHashMap<>();

    public static void add(String name, List<Fixture> fixtures) {
        groups.put(name, fixtures);
    }

    public static List<String> getLoadedGroupNames() {
        return groups.keySet().stream().toList();
    }

    public static List<Fixture> getByName(String name) {
        return groups.get(name);
    }

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
        fNames.forEach(fName -> Util.runIfNotNull(FixtureHandler.getByName(fName), f -> {
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

    public static void toggleAll(List<Fixture> list, boolean on) {
        if (list == null) return;
        list.forEach(fixture -> {
            if (on) fixture.turnOn();
            else fixture.turnOff();
        });
    }

    public static YamlConfiguration createConfig() {
        StringBuilder sb = new StringBuilder();

        groups.forEach((s, l) -> {
            YamlConfiguration groupRoot = new YamlConfiguration();

            List<String> names = new ArrayList<>();
            l.forEach(f -> names.add(f.name));

            groupRoot.set(s, names);

            sb.append(groupRoot.saveToString()).append("\n");
        });

        YamlConfiguration conf = new YamlConfiguration();

        try {
            conf.loadFromString(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return conf;
    }
}

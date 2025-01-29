package me.madmagic.ravevisuals.handlers;

import me.madmagic.ravevisuals.Main;
import me.madmagic.ravevisuals.Util;
import me.madmagic.ravevisuals.config.CommandsConfig;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandsHandler {

    private static final Map<String, List<String>> commands = new HashMap<>();

    public static List<String> getByName(String name) {
        return commands.get(name);
    }

    public static List<String> getLoadedCommandsNames() {
        return commands.keySet().stream().toList();
    }

    public static void createFromConfig(String name, List<String> list) {
        commands.put(name, list);
    }

    public static void runFromCommand(CommandSender sender, String[] args) {
        Util.runIfNotNull(getByName(args[0]), CommandsHandler::runCommands, () -> sender.sendMessage("Commands not found"));
    }

    public static void runCommands(List<String> cmds) {
        if (!Bukkit.isPrimaryThread()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    runCmds(cmds);
                }
            }.runTask(Main.instance);
        } else {
            runCmds(cmds);
        }
    }

    private static void runCmds(List<String> cmds) {
        Server server = Bukkit.getServer();
        CommandSender console = server.getConsoleSender();

        cmds.forEach(cmd -> server.dispatchCommand(console, cmd));
    }

    public static void reload() {
        commands.clear();
        CommandsConfig.init();
    }
}

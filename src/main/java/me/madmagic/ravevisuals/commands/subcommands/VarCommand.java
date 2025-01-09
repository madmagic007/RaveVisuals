package me.madmagic.ravevisuals.commands.subcommands;

import me.madmagic.ravevisuals.handlers.VarHandler;
import me.madmagic.ravevisuals.instances.VarInstance;
import org.bukkit.command.CommandSender;

import java.util.List;

public class VarCommand extends SubCommand {

    public VarCommand() {
        super("var");
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        VarInstance var = VarHandler.getByName(args[1]);
        if (var == null) {
            sender.sendMessage(args[1] + " not found");
            return;
        }

        switch (args[0]) {
            case "set" -> {
                var.setValue(Double.parseDouble(args[2]));
                sender.sendMessage(String.format("Set the value of %s to %s", args[1], var));
            }
            case "get" -> sender.sendMessage(args[1] + ": " + var);
        }
    }

    @Override
    public List<String> getTabCompletions(String path) {
        String[] split = path.split("\\.");

        if ((path.startsWith("var.set") || path.startsWith("var.get")) && split.length == 2) return VarHandler.getLoadedVarNames();

        return super.getTabCompletions(path);
    }
}

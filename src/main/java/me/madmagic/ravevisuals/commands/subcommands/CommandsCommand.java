package me.madmagic.ravevisuals.commands.subcommands;

import me.madmagic.ravevisuals.handlers.CommandsHandler;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandsCommand extends SubCommand {

    public CommandsCommand() {
        super("commands");
    }

    @Override
    public void run(CommandSender player, String[] args) {
        CommandsHandler.runFromCommand(player, args);
    }

    @Override
    public List<String> getTabCompletions(String path) {
        return CommandsHandler.getLoadedCommandsNames();
    }
}

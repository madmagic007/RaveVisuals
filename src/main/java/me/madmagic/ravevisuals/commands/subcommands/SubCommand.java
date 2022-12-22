package me.madmagic.ravevisuals.commands.subcommands;

import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public abstract class SubCommand {

    public final String name;

    public SubCommand(String name) {
        this.name = name;
    }

    public abstract void run(CommandSender player, String[] args);
    public List<String> getTabCompletions(String path) {
        return Collections.EMPTY_LIST;
    }
}

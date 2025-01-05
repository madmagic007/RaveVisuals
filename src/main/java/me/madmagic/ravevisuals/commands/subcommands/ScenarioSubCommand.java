package me.madmagic.ravevisuals.commands.subcommands;

import me.madmagic.ravevisuals.handlers.sequences.ScenarioPlayer;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ScenarioSubCommand extends SubCommand {

    public ScenarioSubCommand() {
        super("scenario");
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        ScenarioPlayer.runScenario(sender, args[0]);
    }

    @Override
    public List<String> getTabCompletions(String path) {
        return ScenarioPlayer.scenarios.keySet().stream().toList();
    }
}

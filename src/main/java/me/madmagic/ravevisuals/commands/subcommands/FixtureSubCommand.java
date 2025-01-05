package me.madmagic.ravevisuals.commands.subcommands;

import me.madmagic.ravevisuals.handlers.EditorHandler;
import me.madmagic.ravevisuals.handlers.sequences.MotionPlayer;
import me.madmagic.ravevisuals.handlers.FixtureHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class FixtureSubCommand extends SubCommand {

    public FixtureSubCommand() {
        super("fixture");
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        try {
            switch (args[0]) {
                case "create" -> FixtureHandler.createFromCommand(sender, args);
                case "remove" -> FixtureHandler.removeFromCommand(sender, args);
                case "save" -> FixtureHandler.save(sender);
                case "edit" -> {
                    if (!(sender instanceof Player player))
                        sender.sendMessage("Only a player may run this command");
                    else
                        EditorHandler.startEditMode(player);
                }
                case "start", "stop" -> FixtureHandler.turnOn(args[1]);
                case "startmotion" -> MotionPlayer.startFromCommand(sender, args, false);
                case "stopmotion" -> MotionPlayer.stopFromCommand(sender, args, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getTabCompletions(String path) {
        String[] split = path.split("\\.");

        if (path.startsWith("fixture.startmotion") && split.length == 3) return MotionPlayer.motions.keySet().stream().toList();
        if (split.length < 4) return FixtureHandler.activeFixtures.keySet().stream().toList();

        return super.getTabCompletions(path);
    }
}

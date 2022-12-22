package me.madmagic.ravevisuals.commands.subcommands;

import me.madmagic.ravevisuals.raveold.handlers.EditorHandler;
import me.madmagic.ravevisuals.raveold.handlers.fixtures.FixtureAnim;
import me.madmagic.ravevisuals.raveold.handlers.fixtures.FixtureHandler;
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
                    if (!(sender instanceof Player player)) {
                        sender.sendMessage("Only a player may run this command");
                        return;
                    }
                    EditorHandler.startEditMode(player);
                }
                case "start" -> FixtureAnim.start(args[1]);
                case "stop" -> FixtureAnim.stop(args[1]);
                case "startmotion" -> FixtureAnim.startFromCommand(sender, args, false);
                case "stopmotion" -> FixtureAnim.stopFromCommand(sender, args, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getTabCompletions(String path) {
        String[] split = path.split("\\.");

        if (path.startsWith("fixture.startmotion") && split.length == 3) return FixtureAnim.motions.keySet().stream().toList();
        if (path.startsWith("fixture.stopmotion") && split.length == 3) return FixtureAnim.motions.keySet().stream().toList();
        if (split.length < 4) return FixtureHandler.activeFixtures.keySet().stream().toList();

        return super.getTabCompletions(path);
    }
}

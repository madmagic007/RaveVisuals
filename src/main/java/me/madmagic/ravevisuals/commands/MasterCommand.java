package me.madmagic.ravevisuals.commands;

import me.madmagic.ravevisuals.commands.subcommands.FixtureSubCommand;
import me.madmagic.ravevisuals.commands.subcommands.GroupSubCommand;
import me.madmagic.ravevisuals.commands.subcommands.ScenarioSubCommand;
import me.madmagic.ravevisuals.commands.subcommands.SubCommand;
import me.madmagic.ravevisuals.raveold.handlers.GroupHandler;
import me.madmagic.ravevisuals.raveold.handlers.fixtures.FixtureAnim;
import me.madmagic.ravevisuals.raveold.handlers.fixtures.FixtureHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MasterCommand extends CommandBase {

    public MasterCommand() {
        super("rv");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) return false;

        String subName = args[0];
        if (subName.isEmpty()) return false;

        if (subName.equals("reload")) {
            FixtureHandler.reload();
            GroupHandler.reload();
            FixtureAnim.reload();
            sender.sendMessage("Reloaded RaveVisuals");
            return true;
        }

        AtomicBoolean suc6 = new AtomicBoolean(false);
        subCommands.forEach(subCommand -> {
            if (subCommand.name.equals(subName)) {
                subCommand.run(sender, Arrays.copyOfRange(args, 1, args.length));
                suc6.set(true);
            }
        });

        return suc6.get();
    }

    @Override
    public List<String> getCustomCompletion(String path) {
        String[] split = path.split("\\.");
        String subName = split[1];

        List<String> completion = new ArrayList<>();
        subCommands.forEach(subCommand -> {
            if (subCommand.name.equals(subName)) completion.addAll(subCommand.getTabCompletions(path.replaceFirst("rostercraft\\.", "")));
        });

        return completion;
    }

    private final List<SubCommand> subCommands = Arrays.asList(
            new FixtureSubCommand(), new GroupSubCommand(), new ScenarioSubCommand()
    );
}

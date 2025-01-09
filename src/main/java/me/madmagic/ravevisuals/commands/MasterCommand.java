package me.madmagic.ravevisuals.commands;

import me.madmagic.ravevisuals.api.ApiServerHandler;
import me.madmagic.ravevisuals.commands.subcommands.*;
import me.madmagic.ravevisuals.handlers.EditorHandler;
import me.madmagic.ravevisuals.handlers.GroupHandler;
import me.madmagic.ravevisuals.handlers.VarHandler;
import me.madmagic.ravevisuals.handlers.anim.MotionHandler;
import me.madmagic.ravevisuals.handlers.FixtureHandler;
import me.madmagic.ravevisuals.handlers.anim.SceneHandler;
import me.madmagic.ravevisuals.handlers.anim.SequenceHandler;
import me.madmagic.ravevisuals.handlers.anim.StateHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MasterCommand extends CommandBase {

    public MasterCommand() {
        super("rv");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) return false;

        String subName = args[0];
        if (subName.isEmpty()) return false;

        if (subName.equals("reload")) {
            VarHandler.reload();
            FixtureHandler.reload();
            GroupHandler.reload();
            MotionHandler.reload();
            StateHandler.reload();
            SequenceHandler.reload();
            SceneHandler.reload();
            ApiServerHandler.init();
            EditorHandler.reload();

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
            if (subCommand.name.equals(subName)){
                List<String> completions = subCommand.getTabCompletions(path.replaceFirst("rv\\.", ""));
                completion.addAll(completions);
            }
        });

        return completion;
    }

    private final List<SubCommand> subCommands = Arrays.asList(
            new FixtureSubCommand(), new GroupSubCommand(), new SceneSubCommand(), new NewAuthCommand(), new VarCommand()
    );
}

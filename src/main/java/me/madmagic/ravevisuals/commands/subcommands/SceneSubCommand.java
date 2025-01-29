package me.madmagic.ravevisuals.commands.subcommands;

import me.madmagic.ravevisuals.handlers.anim.SceneHandler;
import org.bukkit.command.CommandSender;

import java.util.List;

public class SceneSubCommand extends SubCommand {

    public SceneSubCommand() {
        super("scene");
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        switch (args[0]) {
            case "start" -> SceneHandler.startSceneFromCommand(sender, args[1]);
            case "stop" -> SceneHandler.stopSceneFromCommand(sender, args[1]);
        }
    }

    @Override
    public List<String> getTabCompletions(String path) {
        return SceneHandler.getLoadedSceneNames();
    }
}

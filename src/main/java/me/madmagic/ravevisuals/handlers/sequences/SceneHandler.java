package me.madmagic.ravevisuals.handlers.sequences;

import me.madmagic.ravevisuals.Util;
import me.madmagic.ravevisuals.instances.scenes.Scene;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SceneHandler {

    private static Map<String, Scene> scenes = new HashMap<>();

    public static void addScene(String name, Scene scene) {
        scenes.put(name, scene);
    }

    public static List<String> getLoadedSceneNames() {
        return scenes.keySet().stream().toList();
    }

    public static void startSceneFromCommand(CommandSender sender, String scenarioName) {
        Util.runIfNotNull(scenes.get(scenarioName), Scene::startTask, () -> sender.sendMessage("Scenario not found"));
    }

    public static void stopSceneFromCommand(CommandSender sender, String scenarioName) {
        Util.runIfNotNull(scenes.get(scenarioName), Scene::stop, () -> sender.sendMessage("Scenario not found"));
    }
}

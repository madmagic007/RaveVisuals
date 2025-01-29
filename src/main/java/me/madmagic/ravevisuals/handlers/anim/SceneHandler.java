package me.madmagic.ravevisuals.handlers.anim;

import me.madmagic.ravevisuals.Util;
import me.madmagic.ravevisuals.config.SceneConfig;
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

    public static Scene getByName(String name) {
        return scenes.get(name);
    }

    public static List<String> getLoadedSceneNames() {
        return scenes.keySet().stream().toList();
    }

    public static void startSceneFromCommand(CommandSender sender, String scenarioName) {
        Util.runIfNotNull(scenes.get(scenarioName), SceneHandler::startScene, () -> sender.sendMessage("Scenario not found"));
    }

    public static void stopSceneFromCommand(CommandSender sender, String scenarioName) {
        Util.runIfNotNull(scenes.get(scenarioName), SceneHandler::stopScene, () -> sender.sendMessage("Scenario not found"));
    }

    public static void startScene(Scene scene) {
        scene.runInitialIfDefined();
        scene.start();
    }

    public static void stopScene(Scene scene) {
        scene.stop();
        scene.runFinalIfDefined();
    }

    public static void reload() {
        scenes.clear();
        SceneConfig.init();
    }
}

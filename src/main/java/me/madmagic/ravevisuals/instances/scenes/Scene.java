package me.madmagic.ravevisuals.instances.scenes;

import me.madmagic.ravevisuals.Main;
import me.madmagic.ravevisuals.Util;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class Scene {

    public List<ScenePart> sceneParts = new ArrayList<>();
    private int repetitions;
    private BukkitTask sceneTask;
    private boolean run = true;
    private int curPos = 0;
    private int curRepetition = 0;

    public Scene(ConfigurationSection config) {
        if (config == null) return;

        repetitions = config.getInt("repetitions");

        ConfigurationSection sceneSection = config.getConfigurationSection("scene");
        Util.runIfNotNull(sceneSection, sceneConfig ->
            sceneConfig.getKeys(false).forEach(key ->
                sceneParts.add(new ScenePart(config.getConfigurationSection(key)))
            )
        );
    }

    private ScenePart getNextScenePart() {
        ScenePart scenePart = sceneParts.get(curPos);

        curPos++;
        if (curPos >= sceneParts.size()) {
            curPos = 0;
            curRepetition++;

            if (curRepetition >= repetitions) return null;
        }

        return scenePart;
    }

    public void stop() {
        if (sceneTask != null)
            sceneTask.cancel();
        run = false;
    }

    public void startTask() {
        ScenePart scenePart = getNextScenePart();

        if (!run || scenePart == null)
            stop();;

        sceneTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (!run) return;

                scenePart.run();

                startTask();
            }
        }.runTaskLaterAsynchronously(Main.instance, scenePart.afterDelay);
    }
}

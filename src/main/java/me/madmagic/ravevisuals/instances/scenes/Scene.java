package me.madmagic.ravevisuals.instances.scenes;

import me.madmagic.ravevisuals.Main;
import me.madmagic.ravevisuals.Util;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class Scene {

    private final List<ScenePart> sceneParts = new ArrayList<>();
    private ScenePart initialPart;
    private ScenePart finalPart;
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
                        sceneParts.add(new ScenePart(sceneConfig.getConfigurationSection(key)))
                )
        );

        ConfigurationSection initialCfg = config.getConfigurationSection("initial");
        Util.runIfNotNull(initialCfg, cfg ->
                initialPart = new ScenePart(cfg)
        );

        ConfigurationSection finalCfg = config.getConfigurationSection("final");
        Util.runIfNotNull(finalCfg, cfg ->
                finalPart = new ScenePart(cfg)
        );
    }

    private ScenePart getNextScenePart() {
        ScenePart scenePart = sceneParts.get(curPos);

        curPos++;
        if (curPos >= sceneParts.size()) {
            curPos = 0;
            curRepetition++;

            if (curRepetition >= repetitions && repetitions != 0) return null;
        }

        return scenePart;
    }

    public void stop() {
        if (sceneTask != null)
            sceneTask.cancel();
        run = false;
    }

    public void start() {
        run = true;
        startTask();
    }

    private void startTask() {
        ScenePart scenePart = getNextScenePart();

        if (!run || scenePart == null) {
            stop();
            return;
        }

        sceneTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (!run) return;

                scenePart.run();

                startTask();
            }
        }.runTaskLaterAsynchronously(Main.instance, scenePart.afterDelay.getInt());
    }

    public void runInitialIfDefined() {
        Util.runIfNotNull(initialPart, ScenePart::run);
    }

    public void runFinalIfDefined() {
        Util.runIfNotNull(finalPart, ScenePart::run);
    }
}

package me.madmagic.ravevisuals.config.scenario;

import me.madmagic.ravevisuals.Main;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class Scenario {

    public List<ScenarioPart> scenarioParts = new ArrayList<>();
    private int pos = 0;
    private BukkitTask ongoing;

    public Scenario(ConfigurationSection config) {
        config.getKeys(false).forEach(key -> {
            scenarioParts.add(new ScenarioPart(config.getConfigurationSection(key)));
        });
    }

    public void run() {
        pos = 0;
        if (ongoing != null) ongoing.cancel();
        runPart();
    }

    private void runPart() {
        ScenarioPart curPart = scenarioParts.get(pos);
        ongoing = new BukkitRunnable() {
            @Override
            public void run() {
                curPart.run();
                pos++;
                if (pos < scenarioParts.size()) runPart();
            }
        }.runTaskLaterAsynchronously(Main.instance, curPart.afterDelay);
    }
}

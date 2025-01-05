package me.madmagic.ravevisuals.handlers.sequences;

import me.madmagic.ravevisuals.Util;
import me.madmagic.ravevisuals.config.scenario.Scenario;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class ScenarioPlayer {

    public static Map<String, Scenario> scenarios = new HashMap<>();

    public static void runScenario(CommandSender sender, String scenarioName) {
        Util.runIfNotNull(ScenarioPlayer.scenarios.get(scenarioName), Scenario::run, () -> Util.runIfNotNull(sender, s -> s.sendMessage("Scenario not found")));
    }
}

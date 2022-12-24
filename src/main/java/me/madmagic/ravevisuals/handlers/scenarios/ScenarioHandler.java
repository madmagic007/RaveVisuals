package me.madmagic.ravevisuals.handlers.scenarios;

import me.madmagic.ravevisuals.Util;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class ScenarioHandler {

    public static Map<String, Scenario> scenarios = new HashMap<>();

    public static void runScenario(CommandSender sender, String scenarioName) {
        Util.runIfNotNull(ScenarioHandler.scenarios.get(scenarioName), Scenario::run, () -> Util.runIfNotNull(sender, s -> s.sendMessage("Scenario not found")));
    }
}

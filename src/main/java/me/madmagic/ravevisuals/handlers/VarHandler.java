package me.madmagic.ravevisuals.handlers;

import me.madmagic.ravevisuals.config.VarConfig;
import me.madmagic.ravevisuals.instances.VarInstance;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VarHandler {

    private static Map<String, VarInstance> vars = new HashMap<>();

    public static void addVar(String name, double defaultValue) {
        vars.put(name, new VarInstance(name, defaultValue));
    }

    public static VarInstance getByName(String name) {
        return getByName(name, true);
    }

    public static VarInstance getByName(String name, boolean returnDefault) {
        return vars.getOrDefault(name, returnDefault ? new VarInstance(0.0) : null);
    }

    public static List<String> getLoadedVarNames() {
        return vars.keySet().stream().toList();
    }

    public static VarInstance createFromConfig(ConfigurationSection configSection, String keyName) {
        if (!configSection.contains(keyName)) return new VarInstance(0.0);

        Object value = configSection.get(keyName);

        if (value instanceof String) {
            return getByName(value.toString(), true);
        }

        if (value instanceof Number) {
            return new VarInstance(((Number) value).doubleValue());
        }

        return new VarInstance(0.0);
    }

    public static void reload() {
        vars.clear();
        VarConfig.init();
    }
}

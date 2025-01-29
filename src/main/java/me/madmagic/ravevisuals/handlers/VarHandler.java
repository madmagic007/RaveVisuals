package me.madmagic.ravevisuals.handlers;

import me.madmagic.ravevisuals.Util;
import me.madmagic.ravevisuals.config.VarConfig;
import me.madmagic.ravevisuals.instances.VarInstance;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VarHandler {

    private static Map<String, VarInstance> vars = new HashMap<>();

    public static void registerVar(String name, Object value) {
        vars.put(name, createOrGet(name, value));
    }

    public static VarInstance getByName(String name) {
        return getByName(name, true);
    }

    public static VarInstance getByName(String name, boolean returnDefault) {
        return vars.getOrDefault(name, returnDefault ? new VarInstance(name, 0.0) : null);
    }

    public static List<String> getLoadedVarNames() {
        return vars.keySet().stream().toList();
    }

    public static void setValue(String varName, Object value) {
        Util.runIfNotNull(getByName(varName, false),
                varByName ->
                        setValue(varByName, value)
        );
    }

    public static void setValue(VarInstance var, Object value) {
        if (value == null) return;

        Util.runIfNotNull(getByName(value.toString(), false),
                varByName ->
                        var.setValue(varByName.getDouble()),
                () ->
                        var.setValue(Double.parseDouble(value.toString()))
        );
    }

    public static VarInstance createOrGet(String name, @Nullable Object value) {
        value = value == null ? "" : value;

        if ("".equals(name))
            name = value.toString();

        VarInstance var = getByName(name);
        setValue(var, value);

        return var;
    }

    public static VarInstance createFromConfig(ConfigurationSection configSection, String keyName) {
        return createOrGet("", configSection.get(keyName));
    }

    public static void reload() {
        vars.clear();
        VarConfig.init();
    }
}

package me.madmagic.ravevisuals.config;

import me.madmagic.ravevisuals.Main;
import me.madmagic.ravevisuals.Util;
import me.madmagic.ravevisuals.ents.Fixture;
import me.madmagic.ravevisuals.handlers.FixtureHandler;
import me.madmagic.ravevisuals.handlers.GroupHandler;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GroupConfig {

    private static final File file = new File(Main.instance.getDataFolder(), "groups.yml");

    public static void init() {
        try {
            file.createNewFile();
        } catch (Exception ignored) {}

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.getKeys(false).forEach(gName -> {
            List<Fixture> fList = new ArrayList<>();

            config.getStringList(gName).forEach(fName -> Util.runIfNotNull(FixtureHandler.getByName(fName), fList::add));

            GroupHandler.add(gName, fList);
        });
        save();
    }

    public static void save() {
        try {
            GroupHandler.createConfig().save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

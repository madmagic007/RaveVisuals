package me.madmagic.ravevisuals.raveold.config;

import me.madmagic.ravevisuals.Main;
import me.madmagic.ravevisuals.Util;
import me.madmagic.ravevisuals.raveold.fixture.Fixture;
import me.madmagic.ravevisuals.raveold.handlers.fixtures.FixtureHandler;
import me.madmagic.ravevisuals.raveold.handlers.GroupHandler;
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

            config.getStringList(gName).forEach(fName -> Util.runIfNotNull(FixtureHandler.activeFixtures.get(fName), fList::add));
            GroupHandler.groups.put(gName, fList);
        });
        save();
    }

    public static void save() {
        YamlConfiguration config = new YamlConfiguration();
        GroupHandler.groups.forEach((s, l) -> {
            List<String> names = new ArrayList<>();
            l.forEach(f -> names.add(f.name));
            config.set(s, names);
        });

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

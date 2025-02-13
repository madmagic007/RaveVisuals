package me.madmagic.ravevisuals.commands;

import me.madmagic.ravevisuals.Main;
import org.bukkit.command.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.StringUtil;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class CommandBase implements CommandExecutor, TabCompleter {

    protected static YamlConfiguration completions = new YamlConfiguration();
    private final String name;

    public CommandBase(String name) {
        this.name = name;
        PluginCommand c = Main.instance.getCommand(name);
        c.setExecutor(this);
        c.setTabCompleter(this);
    }

    public static void init() {
        try (InputStream in = CommandBase.class.getResourceAsStream("/completions.yml");
             Reader reader = new InputStreamReader(in)) {
            completions.load(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }

        new MasterCommand();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completes = new ArrayList<>();
        int lPos = args.length -1;
        String path = name;
        String longestPath = name;
        ConfigurationSection conf;

        for(int i = 0; i < lPos; i++) {
            path += ("." + args[i]);
            conf = completions.getConfigurationSection(path);
            if (conf != null || completions.getString(path) != null) longestPath += ("." + args[i]);
        }

        conf = completions.getConfigurationSection(path);
        if (conf != null) {
            List<String> str = StringUtil.copyPartialMatches(args[lPos], conf.getKeys(false), completes);
            str.add(args[lPos]);
            return str;
        }

        if ("/".equals(completions.getString(path)) || "//".equals(completions.getString(longestPath))) {
            StringUtil.copyPartialMatches(args[lPos], getCustomCompletion(path), completes);
        } else {
            StringUtil.copyPartialMatches(args[lPos], completions.getStringList(path), completes);
        }

        return completes;
    }

    public List<String> getCustomCompletion(String path) {
        return Collections.EMPTY_LIST;
    }
}

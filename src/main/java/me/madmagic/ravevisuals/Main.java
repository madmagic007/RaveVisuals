package me.madmagic.ravevisuals;

import me.madmagic.ravevisuals.commands.CommandBase;
import me.madmagic.ravevisuals.config.FixtureConfig;
import me.madmagic.ravevisuals.config.GroupConfig;
import me.madmagic.ravevisuals.config.MotionConfig;
import me.madmagic.ravevisuals.config.ScenarioConfig;
import me.madmagic.ravevisuals.handlers.fixtures.FixtureHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static ConsoleCommandSender console;
    public static JavaPlugin instance;

    @Override
    public void onEnable() {
        long timeNow = System.currentTimeMillis();
        console = getServer().getConsoleSender();
        console.sendMessage(ChatColor.BLUE + "Loading RaveVisuals...");

        instance = this;
        getServer().getPluginManager().registerEvents(new EventListener(), this);
        CommandBase.init();

        FixtureConfig.init();
        GroupConfig.init();
        MotionConfig.init();
        ScenarioConfig.init();

        console.sendMessage(ChatColor.BLUE + "Finished loading RaveVisuals. Took " + (System.currentTimeMillis() - timeNow) + "ms.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Shutting RaveVisuals down");
        FixtureHandler.despawnAll();
    }
}

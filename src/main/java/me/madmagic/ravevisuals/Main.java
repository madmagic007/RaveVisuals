package me.madmagic.ravevisuals;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.madmagic.ravevisuals.commands.CommandBase;
import me.madmagic.ravevisuals.raveold.config.FixtureConfig;
import me.madmagic.ravevisuals.raveold.config.GroupConfig;
import me.madmagic.ravevisuals.raveold.config.MotionConfig;
import me.madmagic.ravevisuals.raveold.config.ScenarioConfig;
import me.madmagic.ravevisuals.raveold.handlers.fixtures.FixtureHandler;
import me.madmagic.ravevisuals.raveold.handlers.packets.PacketListener;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static ConsoleCommandSender console;
    public static JavaPlugin instance;
    public static ProtocolManager pm;

    @Override
    public void onEnable() {
        long timeNow = System.currentTimeMillis();
        console = getServer().getConsoleSender();
        console.sendMessage(ChatColor.BLUE + "Loading RaveVisuals...");

        instance = this;
        getServer().getPluginManager().registerEvents(new EventListener(), this);
        CommandBase.init();


        pm = ProtocolLibrary.getProtocolManager();
        PacketListener.init();

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

package me.madmagic.ravevisuals;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.madmagic.ravevisuals.api.ApiServerHandler;
import me.madmagic.ravevisuals.commands.CommandBase;
import me.madmagic.ravevisuals.config.*;
import me.madmagic.ravevisuals.handlers.FixtureHandler;
import me.madmagic.ravevisuals.handlers.packets.PacketListener;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static ConsoleCommandSender console;
    public static JavaPlugin instance;
    public static ProtocolManager pm;
    public static FileConfiguration pluginConfig;

    @Override
    public void onEnable() {
        long timeNow = System.currentTimeMillis();
        console = getServer().getConsoleSender();
        console.sendMessage(ChatColor.BLUE + "Loading RaveVisuals...");
        pm = ProtocolLibrary.getProtocolManager();
        instance = this;
        saveDefaultConfig();
        pluginConfig = getConfig();

        getServer().getPluginManager().registerEvents(new EventListener(), this);

        CommandBase.init();
        VarConfig.init();
        CommandsConfig.init();
        FixtureConfig.init();
        GroupConfig.init();
        MotionConfig.init();
        StateConfig.init();
        SceneConfig.init();
        SequenceConfig.init();

        PacketListener.init();

        ApiServerHandler.init();

        console.sendMessage(ChatColor.BLUE + "Finished loading RaveVisuals. Took " + (System.currentTimeMillis() - timeNow) + "ms.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Shutting RaveVisuals down");
        FixtureHandler.despawnAll();
        ApiServerHandler.stop();
    }
}

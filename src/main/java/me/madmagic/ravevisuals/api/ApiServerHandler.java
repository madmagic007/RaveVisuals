package me.madmagic.ravevisuals.api;

import me.madmagic.ravevisuals.Main;
import me.madmagic.ravevisuals.Util;
import org.bukkit.configuration.ConfigurationSection;

public class ApiServerHandler {

    private static Server server;

    public static void init() {
        stop();
        start();
    }

    private static void start() {
        ConfigurationSection config = Main.pluginConfig.getConfigurationSection("api");
        if (config == null || !config.getBoolean("enabled")) return;

        int port = config.getInt("port", -1);
        if (port == -1) {
            Main.console.sendMessage("Invalid HTTP server port defined");
            return;
        }

        try {
            server = new Server(port);

            server.setCheckAuth(config.getBoolean("useAuthentication"));

            Util.runIfNotNull(config.getString("authenticationToken"), token -> {
                if (token.isEmpty()) return;
                server.setAuthToken(token);
            });
        } catch (Exception e) {
            Main.console.sendMessage("Unable to start HTTP server");
            e.printStackTrace();
        }
    }

    public static void stop() {
        if (server == null || !server.isAlive()) return;
        server.stop();
        Main.console.sendMessage("HTTP server has been stopped");
    }
}

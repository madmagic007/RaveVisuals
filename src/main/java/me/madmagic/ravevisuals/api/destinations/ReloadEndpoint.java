package me.madmagic.ravevisuals.api.destinations;

import fi.iki.elonen.NanoHTTPD;
import me.madmagic.ravevisuals.Main;
import me.madmagic.ravevisuals.api.DestinationIMPL;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class ReloadEndpoint extends DestinationIMPL {

    public ReloadEndpoint() {
        super("reload");
    }

    @Override
    public NanoHTTPD.Response handleRequest(NanoHTTPD.IHTTPSession session, String file) throws Exception {
        new BukkitRunnable() {
            @Override
            public void run() {
                Server server = Bukkit.getServer();
                CommandSender console = server.getConsoleSender();

                server.dispatchCommand(console, "plugman reload RaveVisuals");
            }
        }.runTask(Main.instance);

        return textResponse("ok");
    }
}

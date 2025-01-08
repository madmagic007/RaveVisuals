package me.madmagic.ravevisuals.commands.subcommands;

import me.madmagic.ravevisuals.Main;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.command.CommandSender;

public class NewAuthCommand extends SubCommand {

    public NewAuthCommand() {
        super("generateNewAuthorizationToken");
    }

    @Override
    public void run(CommandSender player, String[] args) {
        String string = RandomStringUtils.randomAlphanumeric(16);
        Main.pluginConfig.getConfigurationSection("api").set("authorizationToken", string);
        Main.instance.saveConfig();
        player.sendMessage("Generated new authorization token and stored in the config. Reload the api with '/rv reload' if necessary.");
    }
}

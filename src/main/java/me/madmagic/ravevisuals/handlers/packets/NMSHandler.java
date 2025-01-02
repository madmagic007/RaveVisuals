package me.madmagic.ravevisuals.handlers.packets;

import net.minecraft.network.protocol.Packet;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_21_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NMSHandler {

    public static void sendPackets(Packet<?>... packets) {
        Bukkit.getServer().getOnlinePlayers().forEach(player -> sendPackets(player, packets));
    }

    public static void sendPacket(Packet<?> packet) {
        Bukkit.getServer().getOnlinePlayers().forEach(player -> sendPacket(player, packet));
    }

    public static void sendPackets(Player player, Packet<?>... packets) {
        for (Packet<?> packet : packets) sendPacket(player, packet);
    }

    public static void sendPacket(Player player, Packet<?> packet) {
        ((CraftPlayer)player).getHandle().f.a(packet);
    }
}

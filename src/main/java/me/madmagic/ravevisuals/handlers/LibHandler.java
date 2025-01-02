package me.madmagic.ravevisuals.handlers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import me.madmagic.ravevisuals.Main;
import me.madmagic.ravevisuals.base.NMSEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LibHandler {

    public static PacketContainer createSpawnPackage(NMSEntity entity) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY);

        // Crucial data, do not remove
        packet.getIntegers().write(0, entity.entityId());
        packet.getUUIDs().write(0, UUID.randomUUID());
        packet.getEntityTypeModifier().write(0, entity.getEntityType());

        Location location = entity.location;

        packet.getDoubles()
                .write(0, location.getX())
                .write(1, location.getY())
                .write(2, location.getZ());

        packet.getBytes()
                .write(0, (byte) (location.getPitch() * 256.0F / 360.0F))
                .write(0, (byte) (location.getYaw() * 256.0F / 360.0F));

        return packet;
    }

    public static PacketContainer createMovePacket(NMSEntity entity) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_TELEPORT);

        Location location = entity.location;

        var modifier = packet.getModifier();

        modifier.write(0, entity.entityId());
        modifier.write(1, location.getX());
        modifier.write(2, location.getY());
        modifier.write(3, location.getZ());

//        packet.getBytes()
//                .write(4, (byte) (location.getYaw() * 256.0F / 360.0F))
//                .write(5, (byte) (location.getPitch() * 256.0F / 360.0F))
//                .write(5, (byte) (location.getYaw() * 256.0F / 360.0F));

        return packet;
    }

    public static void sendPackets(PacketContainer... packets) {
        Bukkit.getServer().getOnlinePlayers().forEach(player -> sendPackets(player, packets));
    }

    public static void sendPacket(PacketContainer packet) {
        Bukkit.getServer().getOnlinePlayers().forEach(player -> sendPacket(player, packet));
    }

    public static void sendPackets(Player player, PacketContainer... packets) {
        for (PacketContainer packet : packets) sendPacket(player, packet);
    }

    public static void sendPacket(Player player, PacketContainer packet) {
        Main.pm.sendServerPacket(player, packet);
    }
}

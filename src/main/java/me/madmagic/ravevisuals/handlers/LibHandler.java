package me.madmagic.ravevisuals.handlers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.InternalStructure;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import me.madmagic.ravevisuals.Main;
import me.madmagic.ravevisuals.base.NMSEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;
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

        packet.getIntegers().write(0, entity.entityId());

        InternalStructure is = packet.getStructures().getValues().get(0);

        is.getVectors()
                .write(0, new Vector(location.getX(), location.getY(), location.getZ()))
                .write(1, new Vector(0, 0, 0));

        is.getFloat()
                .write(0, location.getYaw())
                .write(1, location.getYaw());

        return packet;
    }

    public static PacketContainer createSetHelmetPacket(NMSEntity entity, ItemStack item) {
        var packet = new PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT);

        packet.getIntegers().write(0, entity.entityId());
        packet.getSlotStackPairLists().write(0, List.of(new Pair<>(EnumWrappers.ItemSlot.HEAD, item)));

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

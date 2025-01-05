package me.madmagic.ravevisuals.handlers.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.InternalStructure;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import me.madmagic.ravevisuals.Main;
import me.madmagic.ravevisuals.Util;
import me.madmagic.ravevisuals.ents.NMSEntity;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.UUID;

public class PackerSender {

    public static void spawnEntity(NMSEntity<?, ?> entity, Player... player) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY);

        packet.getIntegers().write(0, entity.entityId());
        packet.getUUIDs().write(0, UUID.randomUUID());
        packet.getEntityTypeModifier().write(0, entity.getEntityType());

        Location location = entity.getLocation();

        packet.getDoubles()
                .write(0, location.getX())
                .write(1, location.getY())
                .write(2, location.getZ());

        packet.getBytes()
                .write(0, (byte) (location.getPitch() * 256.0F / 360.0F))
                .write(1, (byte) (location.getYaw() * 256.0F / 360.0F));

        sendPacket(packet, player);
    }

    public static void deSpawnEntity(NMSEntity<?, ?> entity, Player... player) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);

        packet.getModifier().write(0, new IntArrayList(new int[] { entity.entityId() }));

        PackerSender.sendPacket(packet, player);
    }

    public static void moveEntity(NMSEntity<?, ?> entity, Player... player) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_TELEPORT);

        Location location = entity.getLocation();

        packet.getIntegers().write(0, entity.entityId());

        InternalStructure is = packet.getStructures().getValues().get(0);

        is.getVectors()
                .write(0, new Vector(location.getX(), location.getY(), location.getZ()))
                .write(1, new Vector(0, 0, 0));

        is.getFloat()
                .write(0, location.getYaw());
//                .write(1, location.getYaw());

        sendPacket(packet, player);
    }

    public static void setHelmetForEntity(NMSEntity<?, ?> entity, ItemStack item, Player... player) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT);

        packet.getIntegers().write(0, entity.entityId());
        packet.getSlotStackPairLists().write(0, List.of(new Pair<>(EnumWrappers.ItemSlot.HEAD, item)));

        sendPacket(packet, player);
    }

    public static void syncMetaData(NMSEntity<?, ?> entity, Player... player) {
        sendCustomEntityData(entity, entity.getEntity().getEntityData().getNonDefaultValues(), player);

    }

    public static void sendCustomEntityData(NMSEntity<?, ?> entity, List<SynchedEntityData.DataValue<?>> data, Player... player) {
        ClientboundSetEntityDataPacket packet = new ClientboundSetEntityDataPacket(entity.entityId(), data);
        sendPacket(packet, player);
    }

    public static void sendPacket(PacketContainer packet, Player... player) {
        Util.foreachPopulateIfEmpty(player, Bukkit.getServer()::getOnlinePlayers, ply -> {
            Main.pm.sendServerPacket(ply, packet);
        });
    }

    public static void sendPacket(Packet<?> packet, Player... player) {
        Util.foreachPopulateIfEmpty(player, Bukkit.getServer()::getOnlinePlayers, ply -> {
            ((CraftPlayer) ply).getHandle().connection.send(packet);
        });
    }
}

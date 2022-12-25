package me.madmagic.ravevisuals.base;

import com.mojang.datafixers.util.Pair;
import me.madmagic.ravevisuals.handlers.packets.NMSHandler;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EnumItemSlot;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class NMSEntity {

    protected Entity entity = null;
    public Location location;

    public void spawn(Location location, Player player) {
        this.location = location;

        entity.a(location.getX(), location.getY(), location.getZ(), 0, 0);
        PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity(entity);

        if (player == null) NMSHandler.sendPackets(packet);
        else NMSHandler.sendPackets(player, packet);
    }

    public void spawn(Location location) {
        spawn(location, null);
    }

    public void spawn(Player player) {
        spawn(location, player);
    }

    public void deSpawn() {
        NMSHandler.sendPacket(new PacketPlayOutEntityDestroy(entityId()));
    }

    public NMSEntity setInvisible(boolean invisible) {
        System.out.println("invisible called: " + invisible);
        entity.j(invisible);
        return this;
    }

    public NMSEntity setLocation(Location loc) {
        this.location = loc;
        entity.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), 0);
        NMSHandler.sendPacket(new PacketPlayOutEntityTeleport(entity));
        return this;
    }

    public NMSEntity setHelmet(ItemStack item) {
        NMSHandler.sendPacket(new PacketPlayOutEntityEquipment(entityId(), List.of(new Pair<>(EnumItemSlot.a("head"), CraftItemStack.asNMSCopy(item)))));
        return this;
    }

    public NMSEntity setCustomName(String name) {
        entity.b(IChatBaseComponent.b(name));
        entity.n(true);
        return this;
    }

    public NMSEntity hideCustomName() {
        entity.n(false);
        return this;
    }

    public void update() {
        NMSHandler.sendPacket(entityMetaPacket());
    }

    public void update(Player player) {
        if (player == null) NMSHandler.sendPacket(entityMetaPacket());
        else NMSHandler.sendPacket(player, entityMetaPacket());
    }

    public int entityId() {
        return entity.ae();
    }

    public DataWatcher entityDataWatcher() {
        return entity.ai();
    }

    public PacketPlayOutEntityMetadata entityMetaPacket() {
        return new PacketPlayOutEntityMetadata(entityId(), entityDataWatcher(), true);
    }
}

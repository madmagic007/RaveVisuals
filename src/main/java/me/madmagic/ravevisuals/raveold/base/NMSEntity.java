package me.madmagic.ravevisuals.raveold.base;

import com.mojang.datafixers.util.Pair;
import me.madmagic.ravevisuals.raveold.handlers.packets.NMSHandler;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class NMSEntity {

    protected Entity entity = null;
    public Location location;

    public void spawn(Location location, Player player) {
        setLocation(location);
        PacketPlayOutSpawnEntity packet = entity instanceof EntityLiving ?
                new PacketPlayOutSpawnEntity(entity) :
                new PacketPlayOutSpawnEntity(entityId(), entityUniqueId(), location.getX(), location.getY(), location.getZ(), 0.0F, 0.0F, this.entity.ad(), 0, new Vec3D(0.0, 0.0, 0.0), 0.0);

        if (player == null) NMSHandler.sendPackets(packet, entityMetaPacket());
        else NMSHandler.sendPackets(player, packet, entityMetaPacket());
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
        entity.j(invisible);
        return this;
    }

    public NMSEntity setLocation(Location loc) {
        this.location = loc;
        entity.a(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        entity.pi
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

    public UUID entityUniqueId() {
        return entity.co();
    }

    public DataWatcher entityDataWatcher() {
        return entity.ai();
    }

    public PacketPlayOutEntityMetadata entityMetaPacket() {
        return new PacketPlayOutEntityMetadata(entityId(), entityDataWatcher(), true);
    }
}

package me.madmagic.ravevisuals.base;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import me.madmagic.ravevisuals.Main;
import me.madmagic.ravevisuals.handlers.LibHandler;
import me.madmagic.ravevisuals.handlers.packets.NMSHandler;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.server.level.EntityTrackerEntry;
import net.minecraft.world.entity.Entity;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R2.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NMSEntity {

    protected Entity entity;
    public Location location;
    public EntityTrackerEntry tracker;

    public void spawn(Location location, Player player) {
        this.location = location;

        entity.a(location.getX(), location.getY(), location.getZ(), 0, 0);

        PacketContainer spawnPacket = LibHandler.createSpawnPackage(this);

        if (player == null) LibHandler.sendPackets(spawnPacket);
        else LibHandler.sendPackets(player, spawnPacket);
    }

    public void spawn(Location location) {
        spawn(location, null);
    }

    public void spawn(Player player) {
        spawn(location, player);
    }

    public void deSpawn() {
        //NMSHandler.sendPacket(new PacketPlayOutEntityDestroy(entityId()));

        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
        packet.getModifier().write(0, new IntArrayList(new int[] { entityId() }));

        LibHandler.sendPacket(packet);
    }

    public NMSEntity setInvisible(boolean invisible) {
        entity.j(invisible);
        return this;
    }

    public NMSEntity setLocation(Location loc) {
        this.location = loc;
        entity.a(location.getX(), location.getY(), location.getZ(), 0, 0);

        var locationPacket = LibHandler.createMovePacket(this);
        LibHandler.sendPacket(locationPacket);

        return this;
    }

    public NMSEntity setHelmet(ItemStack item) {
        //NMSHandler.sendPacket(new PacketPlayOutEntityEquipment(entityId(), List.of(new Pair<>(EnumItemSlot.a("head"), CraftItemStack.asNMSCopy(item)))));
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
        tracker.a(((CraftPlayer) player).getHandle());
        Main.console.sendMessage("Update for");
        if (player == null) NMSHandler.sendPacket(entityMetaPacket());
        else NMSHandler.sendPacket(player, entityMetaPacket());
    }

    public int entityId() {
        return entity.ar();
    }

    public DataWatcher entityDataWatcher() {
        return entity.au();
    }

    public PacketPlayOutEntityMetadata entityMetaPacket() {
        return new PacketPlayOutEntityMetadata(entityId(), entityDataWatcher().c());
    }

    public EntityType getEntityType() {
        return null;
    }
}

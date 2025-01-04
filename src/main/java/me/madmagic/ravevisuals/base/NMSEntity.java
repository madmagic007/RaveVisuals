package me.madmagic.ravevisuals.base;

import me.madmagic.ravevisuals.handlers.packets.LibHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R2.CraftWorld;
import org.bukkit.entity.Player;

public abstract class NMSEntity<T extends NMSEntity<?, ?>, E extends Entity> {

    private E entity;
    private Location location;

    public NMSEntity(Class<E> entClass, EntityType<E> entType, Location location) {
        this.location = location;
        try {
            this.entity = entClass.getConstructor(EntityType.class, Level.class).newInstance(entType, ((CraftWorld) location.getWorld()).getHandle());
            entity.setInvisible(true);
        } catch (Exception ignored) {} //won't happen
    }

    public E getEntity() {
        return entity;
    }

    public Location getLocation() {
        return location;
    }

    public T spawn(Player... player) {
        LibHandler.spawnEntity(this, player);
        return (T) this;
    }

    public void deSpawn(Player... player) {
        LibHandler.deSpawnEntity(this, player);
    }

    public T setLocation(Location loc) {
        this.location = loc;
        return (T) this;
    }

    public T syncLocation(Player... player) {
        LibHandler.moveEntity(this, player);
        return (T) this;
    }

    public T setCustomName(String name) {
        entity.setCustomName(Component.literal(name));
        entity.setCustomNameVisible(true);
        return (T) this;
    }

    public T hideCustomName() {
        entity.setCustomNameVisible(false);
        return (T) this;
    }

    public T syncMetaData(Player... player) {
        LibHandler.syncMetaData(this, player);
        return (T) this;
    }

    public T syncAll(Player... player) {
        syncLocation(player);
        syncMetaData(player);
        return (T) this;
    }

    public int entityId() {
        return entity.getId();
    }

    public abstract org.bukkit.entity.EntityType getEntityType();
}

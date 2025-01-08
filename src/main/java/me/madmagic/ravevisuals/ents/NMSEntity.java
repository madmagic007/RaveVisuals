package me.madmagic.ravevisuals.ents;

import me.madmagic.ravevisuals.handlers.packets.PackerSender;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R2.CraftWorld;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class NMSEntity<T extends NMSEntity<?, ?>, E extends Entity> {

    private E entity;
    private Location location;

    private static EntityDataAccessor<Boolean> nameVisAccessor;

    public NMSEntity(Class<E> entClass, EntityType<E> entType, Location location) {
        this.location = location;
        try {
            entity = entClass.getConstructor(EntityType.class, Level.class).newInstance(entType, ((CraftWorld) location.getWorld()).getHandle());
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
        PackerSender.spawnEntity(this, player);
        return (T) this;
    }

    public void deSpawn(Player... player) {
        PackerSender.deSpawnEntity(this, player);
    }

    public T setLocation(Location loc) {
        this.location = loc;
        return (T) this;
    }

    public T syncLocation(Player... player) {
        PackerSender.moveEntity(this, player);
        return (T) this;
    }

    protected SynchedEntityData.DataItem<?>[] getDataItems() {
        SynchedEntityData data = entity.getEntityData();

        try {
            Field fieldItems = data.getClass().getDeclaredField("itemsById");
            fieldItems.setAccessible(true);
            return (SynchedEntityData.DataItem<?>[]) fieldItems.get(data);
        } catch (Exception ignored) {
            return new SynchedEntityData.DataItem<?>[] {};
        }
    }

    public T setCustomNameVisible(boolean visible, Player player) {
        List<SynchedEntityData.DataValue<?>> cloned = new ArrayList<>();

        for (SynchedEntityData.DataItem<?> dataItem : getDataItems()) {
            if (!dataItem.isSetToDefault() && dataItem.getAccessor().equals(nameVisAccessor))
                cloned.add(SynchedEntityData.DataValue.create(nameVisAccessor, visible));
        }

        if (!cloned.isEmpty())
            PackerSender.sendCustomEntityData(this, cloned, player);

        return (T) this;
    }

    public T syncMetaData(Player... player) {
        PackerSender.syncMetaData(this, player);
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

    static {
        try {
            Field fieldNameAc = Entity.class.getDeclaredField("DATA_CUSTOM_NAME_VISIBLE");
            fieldNameAc.setAccessible(true);
            nameVisAccessor = (EntityDataAccessor<Boolean>) fieldNameAc.get(null);
        } catch (Exception ignored) {}
    }
}

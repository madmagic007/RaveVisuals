package me.madmagic.ravevisuals.ents;

import me.madmagic.ravevisuals.handlers.packets.PackerSender;
import net.minecraft.core.Rotations;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class NMSArmorStand<T extends NMSArmorStand<?>> extends NMSEntity<T, ArmorStand> {

    private ItemStack helmet;

    private static EntityDataAccessor<Rotations> headPoseAccessor;

    public NMSArmorStand(Location location) {
        super(ArmorStand.class, EntityType.ARMOR_STAND, location);
        getEntity().setHeadPose(new Rotations(location.getPitch(), 0, 0));
    }

    public T setHelmet(ItemStack helmet) {
        this.helmet = helmet;
        return (T) this;
    }

    public T syncHelmet(Player... player) {
        PackerSender.setHelmetForEntity(this, helmet, player);
        return (T) this;
    }

    public T setHeadPose(float yaw, float pitch) {
        Location loc = getLocation();
        loc.setPitch(pitch);
        loc.setYaw(yaw);

        getEntity().setHeadPose(new Rotations(pitch, 0, 0));

        return (T) this;
    }

    public T syncHeadPose(Player... player) {
        syncLocation(player);

        List<SynchedEntityData.DataValue<?>> cloned = new ArrayList<>();

        for (SynchedEntityData.DataItem<?> dataItem : getDataItems()) {
            if (dataItem.getAccessor().equals(headPoseAccessor))
                cloned.add(dataItem.value());
        }

        if (!cloned.isEmpty())
            PackerSender.sendCustomEntityData(this, cloned, player);

        return (T) this;
    }

    @Override
    public T syncAll(Player... player) {
        super.syncAll(player);
        syncHeadPose(player);
        syncHelmet(player);
        return (T) this;
    }

    @Override
    public org.bukkit.entity.EntityType getEntityType() {
        return org.bukkit.entity.EntityType.ARMOR_STAND;
    }

    static {
        try {
            Field fieldHeadPoseAc = ArmorStand.class.getDeclaredField("DATA_HEAD_POSE");
            fieldHeadPoseAc.setAccessible(true);
            headPoseAccessor = (EntityDataAccessor<Rotations>) fieldHeadPoseAc.get(null);
        } catch (Exception ignored) {}
    }
}

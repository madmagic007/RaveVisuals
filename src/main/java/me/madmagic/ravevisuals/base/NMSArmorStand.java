package me.madmagic.ravevisuals.base;

import me.madmagic.ravevisuals.handlers.packets.LibHandler;
import net.minecraft.core.Rotations;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NMSArmorStand extends NMSEntity<NMSArmorStand, ArmorStand> {

    private ItemStack helmet;

    public NMSArmorStand(Location location) {
        super(ArmorStand.class, EntityType.ARMOR_STAND, location.subtract(0, 0.5, 0));
    }

    public NMSArmorStand setHelmet(ItemStack helmet) {
        this.helmet = helmet;
        return this;
    }

    public NMSArmorStand syncHelmet(Player... player) {
        LibHandler.setHelmetForEntity(this, helmet, player);
        return this;
    }

    public NMSArmorStand setHeadPose(float yaw, float pitch) {
        Location loc = getLocation();
        loc.setPitch(pitch);
        loc.setYaw(yaw);

        getEntity().setHeadPose(new Rotations(pitch, 0, 0));

        return this;
    }

    public NMSArmorStand syncHeadPose(Player... player) {
        syncLocation(player);
        syncMetaData();
        return this;
    }

    @Override
    public org.bukkit.entity.EntityType getEntityType() {
        return org.bukkit.entity.EntityType.ARMOR_STAND;
    }
}

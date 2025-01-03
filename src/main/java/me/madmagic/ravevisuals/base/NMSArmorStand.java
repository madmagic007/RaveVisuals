package me.madmagic.ravevisuals.base;

import net.minecraft.core.Vector3f;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_21_R2.CraftWorld;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

public class NMSArmorStand extends NMSEntity {

    protected final EntityArmorStand as;
    protected final ArmorStand nas;

    public NMSArmorStand(World w) {
        as = new EntityArmorStand(EntityTypes.f, ((CraftWorld)w).getHandle());
        this.entity = as;
        nas = (ArmorStand) as.getBukkitEntity();
    }

    public NMSArmorStand setHeadPose(float yaw, float pitch) {
        //location.setPitch(pitch);
        //location.setYaw(yaw);
        as.a(new Vector3f(pitch, 0, 0));
        return this;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.ARMOR_STAND;
    }
}

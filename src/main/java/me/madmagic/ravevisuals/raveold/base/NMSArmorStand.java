package me.madmagic.ravevisuals.raveold.base;

import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.entity.ArmorStand;

public class NMSArmorStand extends NMSEntity {

    protected final EntityArmorStand as;
    protected final ArmorStand nas;

    public NMSArmorStand(World w) {
        as = new EntityArmorStand(EntityTypes.d, ((CraftWorld)w).getHandle());
        this.entity = as;
        nas = (ArmorStand) as.getBukkitEntity();
    }

    public NMSArmorStand setPitch(float pitch) {
        location.setPitch(pitch);
        //as.a(new Vector3f(pitch, 0, 0));
        return this;
    }

    public NMSArmorStand setYaw(float yaw) {
        location.setYaw(yaw);
        return this;
    }

    public NMSArmorStand setHeadPose(float pitch, float yaw) {
        setPitch(pitch);
        setYaw(yaw);
        return this;
    }
}

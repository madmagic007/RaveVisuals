package me.madmagic.ravevisuals.base;

import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.monster.EntityGuardian;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R2.CraftWorld;
import org.bukkit.util.Vector;

public class NMSGuardian extends NMSEntity {
    protected final EntityGuardian g;
    protected final NMSArmorStand target;

    public NMSGuardian(Location location) {
        g = new EntityGuardian(EntityTypes.N, ((CraftWorld)location.getWorld()).getHandle());
        this.entity = g;

        location.subtract(0, 0.5, 0);

        spawn(location);
        setInvisible(true).update();

        target = new NMSArmorStand(location.getWorld());
        target.spawn(location.clone().subtract(0, 0.5, 0));
        target.setInvisible(true).update();//WHY CANT I SET IT INVISIBLE //update 2025, WHAT DID I MEAN WITH THIS

        g.au().a(new DataWatcherObject<>(17, DataWatcherRegistry.b), target.entityId());
        update();
    }

    public void setTarget(Location location, double length) {
        Vector dir = location.getDirection().multiply(length);
        Location loc = location.clone();
        loc.add(dir).subtract(0, 0.5, 0);

        target.setLocation(loc);
    }

    @Override
    public void deSpawn() {
        super.deSpawn();
        target.deSpawn();
    }
}

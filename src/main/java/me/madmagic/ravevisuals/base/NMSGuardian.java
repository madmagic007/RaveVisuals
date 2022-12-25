package me.madmagic.ravevisuals.base;

import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.monster.EntityGuardian;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class NMSGuardian extends NMSEntity {
    protected final EntityGuardian g;
    protected final Guardian ng;
    protected final NMSArmorStand target;

    public NMSGuardian(Location location) {
        System.out.println("new guardian");
        g = new EntityGuardian(EntityTypes.N, ((CraftWorld)location.getWorld()).getHandle());
        this.entity = g;
        ng = (Guardian) g.getBukkitEntity();

        location.subtract(0, 0.5, 0);

        spawn(location);
        setInvisible(true).update();

        target = new NMSArmorStand(location.getWorld());
        target.spawn(location.clone().subtract(0, 0.5, 0));
        target.setInvisible(true).update();//WHY CAN I SET IT INVISIBLE
    }

    @Override
    public void spawn(Player player) {
        super.spawn(player);
        target.spawn(player);
        g.ai().b(new DataWatcherObject<>(17, DataWatcherRegistry.b), target.as.ae());
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

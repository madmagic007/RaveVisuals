package me.madmagic.ravevisuals.ents;

import me.madmagic.ravevisuals.Main;
import me.madmagic.ravevisuals.instances.VarInstance;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Guardian;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class NMSGuardian extends NMSEntity<NMSGuardian, Guardian> {

    protected NMSArmorStand target;

    public NMSGuardian(Location location, VarInstance beamLength) {
        super(Guardian.class, EntityType.GUARDIAN, location.subtract(0, 0.47, 0));

        target = new NMSArmorStand(calcTargetLocation(location, beamLength));
    }

    private Location calcTargetLocation(Location location, VarInstance beamLength) {
        Location targetLoc = location.clone();
        Vector dir = targetLoc.getDirection().multiply(beamLength.getDouble());
        return targetLoc.add(dir).subtract(0, 0.58, 0);
    }

    public NMSGuardian updateBeam(Location location, VarInstance beamLength) {
        target.setLocation(calcTargetLocation(location, beamLength));
        setLocation(location);

        return this;
    }

    public NMSGuardian syncBeam(Player... player) {
        target.syncLocation(player);
        syncLocation(player);
        return this;
    }

    @Override
    public NMSGuardian spawn(Player... player) {
        target.spawn(player);
        super.spawn(player);

        target.syncMetaData(player);
        syncMetaData(player);

        // For some reason the guardian beam will start in a different direction, not aiming at the target.
        // This only sets the target of the guardian one tick after the target has spawned, to ensure the beam is aimed correctly from the start.
        Bukkit.getScheduler().runTask(Main.instance, () -> {
            getEntity().getEntityData().set(new EntityDataAccessor<>(17, EntityDataSerializers.INT), target.entityId());
            syncMetaData(player);
        });

        return this;
    }

    @Override
    public void deSpawn(Player... player) {
        super.deSpawn(player);
        target.deSpawn(player);
    }

    @Override
    public org.bukkit.entity.EntityType getEntityType() {
        return org.bukkit.entity.EntityType.GUARDIAN;
    }
}

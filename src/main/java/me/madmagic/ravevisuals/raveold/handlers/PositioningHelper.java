package me.madmagic.ravevisuals.raveold.handlers;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class PositioningHelper {

    public static Location inFrontOf(Player pl, int distance) {
        Location loc = pl.getEyeLocation();
        Vector direction = pl.getLocation().getDirection().multiply(distance);
        return loc.add(direction);
    }

    public static Location lookAt(Location curLoc, Entity at) {
        Location atLoc = at.getLocation();
        curLoc.setPitch(0-atLoc.getPitch());
        curLoc.setYaw(atLoc.getYaw() + 180);
        return curLoc;
    }

    public static Location inFrontOfLookAt(Player pl, int distance) {
        return lookAt(inFrontOf(pl, distance), pl);
    }


    /**
     * because i know this wont make any sense tomorrow, minecrafts rotation ranges -180 to 180 instead of the normal, everywhere else used, very standard implementation of 0 to 360
     */
    public static float fixRotation(float value) {
        if (value > 180) return -180 + (value - 180);
        if (value < -180) return 180 - (value + 180);
        else return value;
    }
}

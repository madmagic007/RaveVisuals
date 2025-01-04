package me.madmagic.ravevisuals.fixture;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import me.madmagic.ravevisuals.Main;
import me.madmagic.ravevisuals.base.NMSArmorStand;
import me.madmagic.ravevisuals.handlers.PositioningHelper;
import me.madmagic.ravevisuals.handlers.fixtures.Motion;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.awt.*;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Fixture extends NMSArmorStand {

    public final String name;
    public boolean isOn = false;
    public boolean showHead = true;
    public Effect effect = new Effect();
    public final float restPitch;
    public final float restYaw;

    private Motion curMotion;
    private BukkitTask motionTask;

    public Fixture(Location location, String name) {
        super(location.subtract(0, 1.7, 0));

        this.name = name;
        restPitch = location.getPitch();
        restYaw = location.getYaw();

        setHeadTexture(HeadTexture.SPOT_OFF).setHeadPose(location.getYaw(), location.getPitch());
    }

    public Fixture(Location location, String name, ConfigurationSection config) {
        this(location, name);

        showHead = config.getBoolean("head");
        if (showHead) setHeadTexture(HeadTexture.SPOT_OFF);
        setHeadPose(location.getYaw(), location.getPitch());

        effect = new Effect(config);
    }

    public void lateSpawn(Player player) {
        spawn(player);

        if (showHead) syncHelmet(player);

        if (isOn && effect.effect.equals(Effect.EffectType.GUARDIAN)) {
            effect.guardian.spawn(player);
        }
    }

    public void turnOn(Player... player) {
        isOn = true;

        if (showHead) setHeadTexture(HeadTexture.SPOT_BLUE).syncHelmet(player);

        effect.start(getLocation().clone().add(0, 1.7, 0));
    }

    public void turnOff(Player... player) {
        isOn = false;

        if (showHead) setHeadTexture(HeadTexture.SPOT_OFF).syncHelmet(player);

        effect.stop();
        stopMotionTimer();
        curMotion = null;

        setHeadPose(restYaw, restPitch).syncHeadPose(player);
    }

    @Override
    public NMSArmorStand setHeadPose(float yaw, float pitch) {
        super.setHeadPose(yaw, pitch);

        Location clone = getLocation().clone();

        if (effect.effect.equals(Effect.EffectType.GUARDIAN)) effect.setGuardianTarget(clone.add(0, 1.2, 0));
        else effect.particleLocation = clone.add(0, 1.7, 0);

        return this;
    }

    public Fixture setHeadTexture(HeadTexture texture) {
        setHelmet(texture.toItemStack());
        return this;
    }

    public void changeBeamTypeRunning(Effect.EffectType type) {
        if (effect.effect.equals(type)) return;
        if (isOn) effect.stop();
        effect.effect = type;

        Vec3 loc = getEntity().getEyePosition();
        if (isOn) effect.start(getLocation().clone().set(loc.x, loc.y, loc.z));
    }

    int curPos = 1;
    public void startMotion(Motion motion) {
        curMotion = motion;
        curPos = 1;
        stopMotionTimer();
        turnOn();

        if (!motion.relative) {
            Vector initPos = motion.motion.get(0);
            setHeadPose((float) initPos.getX(), (float) initPos.getY()).syncMetaData();
        }

        doTimer(motion);
    }

    private void doTimer(Motion motion) {
        if (!isOn) return;
        stopMotionTimer();
        motionTask = new BukkitRunnable() {
            @Override
            public void run() {
                moveTo(curMotion.motion.get(curPos), curMotion.name);
                curPos++;
                if (curMotion == null || !Objects.equals(curMotion.name, motion.name) || !isOn) return;
                if (curPos >= curMotion.motion.size()) curPos = 0;
                doTimer(motion);
            }
        }.runTaskLaterAsynchronously(Main.instance, 1);
    }

    private void stopMotionTimer() {
        if (motionTask == null) return;
        motionTask.cancel();
    }

    private void moveTo(Vector dest, String name) {
        if (curMotion.relative) {
            dest.setX(dest.getX() + restYaw);
            dest.setZ(dest.getZ() + restPitch);
        }

        Location location = getLocation();

        int ticks = curMotion.delay;
        float stepYaw = (float) ((dest.getX() - location.getYaw()) / ticks);
        float stepPitch = (float) ((dest.getZ() - location.getPitch()) / ticks);

        int curTick = 0;
        while (curTick < (ticks -1)) {
            if (!isOn || !curMotion.name.equals(name)) return;

            float newPitch = PositioningHelper.fixRotation(location.getPitch() + stepPitch);
            float newYaw = PositioningHelper.fixRotation(location.getYaw() + stepYaw);

            setHeadPose(newYaw, newPitch).syncMetaData();

            curTick++;
            try {
                TimeUnit.MILLISECONDS.sleep(1000/20);
                if (!isOn) return;
            } catch (Exception ignored) {}
        }

        //force move in case of any rounding errors
        setHeadPose((float) dest.getX(), (float) dest.getZ()).syncMetaData();
    }

    public enum HeadTexture {
        SPOT_OFF("NDhjZjIyZDZlYjExYzQ2M2UyZDI1ZDhhODFlOTY3NWY5MTg2ODgzMmIwNDM4ODc4YzczOWZkZGRjNDJhIn19fQ==", null),
        SPOT_BLUE("YzJjYWE1NGJkMGJkY2ZkNzdjYjA2YzI1OGM3YzViODNiNTI2NzE4ZDA0ZDg0ZTRkMTg3YjZkOTcwMjYyYyJ9fX0", Color.BLUE);

        public final String base64;
        public final Color baseColor;
        HeadTexture(String base64, Color baseColor) {
            this.base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv" + base64;
            this.baseColor = baseColor;
        }

        public ItemStack toItemStack() {
            ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);

            playerHead.editMeta(SkullMeta.class, skullMeta -> {
                PlayerProfile playerProfile = Bukkit.createProfile(UUID.randomUUID(), "");
                playerProfile.setProperty(new ProfileProperty("textures", base64));

                skullMeta.setPlayerProfile(playerProfile);
            });

            return playerHead;
        }
    }
}

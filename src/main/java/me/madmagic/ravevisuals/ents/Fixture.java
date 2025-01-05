package me.madmagic.ravevisuals.ents;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import me.madmagic.ravevisuals.config.fixture.Effect;
import me.madmagic.ravevisuals.handlers.sequences.MotionPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.awt.*;
import java.util.UUID;

public class Fixture extends NMSArmorStand<Fixture> {

    public String name;
    public boolean isOn = false;
    public boolean showHead = true;
    public Effect effect = new Effect();
    public final float restPitch;
    public final float restYaw;


    public Fixture(Location location, String name) {
        super(location);

        this.name = name;
        restPitch = location.getPitch();
        restYaw = location.getYaw();

        setHeadTexture(HeadTexture.SPOT_OFF).getEntity().setCustomName(Component.literal(name));
    }

    public Fixture(Location location, String name, ConfigurationSection config) {
        this(location, name);

        if (showHead) setHeadTexture(HeadTexture.SPOT_OFF);

        effect = Effect.fromConfig(config);
    }

    public void turnOn(Player... player) {
        isOn = true;

        if (showHead) setHeadTexture(HeadTexture.SPOT_BLUE).syncHelmet(player);

        effect.start(getLocation().clone().add(0, 1.7, 0));
    }

    public Fixture turnOff(Player... player) {
        isOn = false;

        if (showHead) setHeadTexture(HeadTexture.SPOT_OFF).syncHelmet(player);
        effect.stop();
        MotionPlayer.stopMotion(this);

        setHeadPose(restYaw, restPitch).syncHeadPose(player);

        return this;
    }

    @Override
    public Fixture syncAll(Player... player) {
        if (showHead) syncHelmet(player);

        if (isOn && effect.effect.equals(Effect.EffectType.GUARDIAN)) {
            effect.guardian.spawn(player);
        }

        super.syncAll(player);

        return this;
    }

    @Override
    public Fixture setHeadPose(float yaw, float pitch) {
        super.setHeadPose(yaw, pitch);

        Location clone = getLocation().clone();

        if (effect.effect.equals(Effect.EffectType.GUARDIAN)) effect.setGuardianTarget(clone.add(0, 1.2, 0));
        else effect.particleLocation = clone.add(0, 1.7, 0);

        return this;
    }

    @Override
    public Fixture syncHeadPose(Player... player) {
        if (isOn && effect.effect.equals(Effect.EffectType.GUARDIAN)) effect.guardian.syncBeam(player);
        return super.syncHeadPose(player);
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

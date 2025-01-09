package me.madmagic.ravevisuals.instances.motion;

import me.madmagic.ravevisuals.Main;
import me.madmagic.ravevisuals.ents.Fixture;
import me.madmagic.ravevisuals.handlers.PositioningHelper;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.concurrent.TimeUnit;

public class MotionInstance {

    public final Fixture fixture;
    public final Motion motion;
    public BukkitTask motionTask;
    public boolean run = true;
    public int curPos = 0;

    public MotionInstance(Fixture fixture, Motion motion) {
        this.fixture = fixture;
        this.motion = motion;
    }

    public Vector getNextVector() {
        Vector vector = motion.motions.get(curPos);

        curPos++;
        if (curPos >= motion.motions.size()) curPos = 0;

        return vector;
    }

    public void stop() {
        if (motionTask != null)
            motionTask.cancel();
        run = false;
    }

    public void startTask() {
        if (!run) return;

        Vector nextVec = getNextVector();

        motionTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (!run) return;

                runOnFixture(fixture, nextVec);

                startTask();
            }
        }.runTaskLaterAsynchronously(Main.instance, 0);
    }

    private void runOnFixture(Fixture fixture, Vector vector) {
        if (motion.relative) {
            vector.setX(vector.getX() + fixture.restYaw);
            vector.setZ(vector.getZ() + fixture.restPitch);
        }

        Location location = fixture.getLocation();
        int ticks = motion.delay.getInt();

        float deltaYaw = (float) ((vector.getX() - location.getYaw()) % 360);
        float stepYaw = PositioningHelper.fixRotation(deltaYaw) / ticks;

        float deltaPitch = (float) ((vector.getZ() - location.getPitch()) % 360);
        float stepPitch = PositioningHelper.fixRotation(deltaPitch) / ticks;

        float newYaw = location.getYaw();
        float newPitch = location.getPitch();

        for (int i = 0; i < ticks; i++) {
            if (motion.delay.getInt() != ticks) break; //handle live updates of var

            newYaw = PositioningHelper.fixRotation((newYaw + stepYaw + 360) % 360);
            newPitch = PositioningHelper.fixRotation((newPitch + stepPitch + 360) % 360);

            fixture.setHeadPose(newYaw, newPitch).syncHeadPose();

            try {
                TimeUnit.MILLISECONDS.sleep(1000 / 20);
                if (!run) return;
            } catch (Exception ignored) {}
        }

        //force move in case of any rounding errors
        fixture.setHeadPose((float) vector.getX(), (float) vector.getZ())
                .syncHeadPose();
    }
}
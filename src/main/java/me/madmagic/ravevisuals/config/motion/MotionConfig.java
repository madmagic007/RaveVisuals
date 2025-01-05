package me.madmagic.ravevisuals.config.motion;

import me.madmagic.ravevisuals.Main;
import me.madmagic.ravevisuals.ents.Fixture;
import me.madmagic.ravevisuals.handlers.PositioningHelper;
import me.madmagic.ravevisuals.handlers.sequences.MotionPlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class MotionConfig {

    private static final File file = new File(Main.instance.getDataFolder(), "motions.yml");

    public static void init() {
        try {
            file.createNewFile();
        } catch (Exception ignored) {}
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        config.getKeys(false).forEach(name -> MotionPlayer.motions.put(name, new Motion(config.getConfigurationSection(name), name)));
    }

    public static class Motion {

        public final String name;
        public final int delay;
        public final boolean relative;
        public final List<Vector> motions;

        public Motion(ConfigurationSection config, String name) {
            this.name = name;
            delay = config.getInt("delay");
            relative = config.getBoolean("relative");

            motions = new ArrayList<>();
            config.getStringList("motion").forEach(s -> {
                double[] split = Stream.of(s.split(" ")).mapToDouble(Double::parseDouble).toArray();
                motions.add(new Vector(PositioningHelper.fixRotation((float) split[0]), 0, PositioningHelper.fixRotation((float) split[1])));
            });
        }

        public static class MotionInstance {

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

            public void cancelTask() {
                if (motionTask != null)
                    motionTask.cancel();
                run = false;
            }

            public void startTask() {
                motionTask = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!run) return;

                        MotionPlayer.nextMotionStep(MotionInstance.this);
                        startTask();
                    }
                }.runTaskLaterAsynchronously(Main.instance, 0);
            }
        }
    }
}

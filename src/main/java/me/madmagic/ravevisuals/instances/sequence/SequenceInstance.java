package me.madmagic.ravevisuals.instances.sequence;

import me.madmagic.ravevisuals.Main;
import me.madmagic.ravevisuals.ents.Fixture;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class SequenceInstance {

    private Fixture fixture;
    private Sequence sequence;
    private BukkitTask sequenceTask;
    private boolean run = true;
    private int curPos = 0;
    private int curRepetition = 0;

    public SequenceInstance(Fixture fixture, Sequence sequence) {
        this.fixture = fixture;
        this.sequence = sequence;
    }

    private SequencePart getNextSequencePart() {
        SequencePart sequencePart = sequence.sequenceParts.get(curPos);

        curPos++;
        if (curPos >= sequence.sequenceParts.size()) {
            curPos = 0;
            curRepetition++;

            if (curRepetition >= sequence.repetitions && sequence.repetitions != 0) return null;
        }

        return sequencePart;
    }

    public void stop() {
        if (sequenceTask != null)
            sequenceTask.cancel();
        run = false;
    }

    public void startTask() {
        SequencePart sequencePart = getNextSequencePart();

        if (!run || sequencePart == null) {
            stop();
            return;
        }

        sequenceTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (!run) return;

                sequencePart.runOn(fixture);

                startTask();
            }
        }.runTaskLaterAsynchronously(Main.instance, sequencePart.afterDelay);
    }

    public void applyInitialStateToIfDefined(Fixture fixture) {
        sequence.applyInitialStateToIfDefined(fixture);
    }
}

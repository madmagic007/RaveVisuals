package me.madmagic.ravevisuals.instances.sequence;

import me.madmagic.ravevisuals.Main;
import me.madmagic.ravevisuals.ents.Fixture;
import me.madmagic.ravevisuals.handlers.anim.SequenceHandler;
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
        if (curPos > sequence.sequenceParts.size() - 1) {
            curPos = 0;
            curRepetition++;

            if (curRepetition >= sequence.repetitions && sequence.repetitions != 0) return null;
        }

        SequencePart sequencePart = sequence.sequenceParts.get(curPos);
        curPos++;

        return sequencePart;
    }

    public void stop() {
        if (sequenceTask != null)
            sequenceTask.cancel();
        run = false;
    }

    public void start() {
        curPos = 0;
        curRepetition = 0;

        run = true;
        startTask();
    }

    private void startTask() {
        if (!run) return;

        SequencePart sequencePart = getNextSequencePart();

        if (sequencePart == null) {
            run = false;
            new BukkitRunnable() {
                @Override
                public void run() {
                    SequenceHandler.stopSequence(fixture, true);
                }
            }.runTaskLaterAsynchronously(Main.instance, 2);
            return;
        }

        sequenceTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (!run) return;

                sequencePart.runOn(fixture);

                startTask();
            }
        }.runTaskLaterAsynchronously(Main.instance, sequencePart.afterDelay.getInt());
    }

    public void applyInitialStateToIfDefined(Fixture fixture) {
        sequence.applyInitialStateToIfDefined(fixture);
    }

    public void applyFinalStateToIfDefined(Fixture fixture) {
        sequence.applyFinalStateToIfDefined(fixture);
    }
}

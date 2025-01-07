package me.madmagic.ravevisuals.handlers.anim;

import me.madmagic.ravevisuals.Util;
import me.madmagic.ravevisuals.config.SequenceConfig;
import me.madmagic.ravevisuals.ents.Fixture;
import me.madmagic.ravevisuals.handlers.FixtureHandler;
import me.madmagic.ravevisuals.handlers.GroupHandler;
import me.madmagic.ravevisuals.instances.sequence.Sequence;
import me.madmagic.ravevisuals.instances.sequence.SequenceInstance;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SequenceHandler {

    private static Map<String, me.madmagic.ravevisuals.instances.sequence.Sequence> sequences = new HashMap<>();
    private static final HashMap<Fixture, SequenceInstance> activeSequences = new HashMap<>();

    public static void addSequence(String name, me.madmagic.ravevisuals.instances.sequence.Sequence sequence) {
        sequences.put(name, sequence);
    }

    public static Sequence getByName(String name) {
        return sequences.get(name);
    }

    public static List<String> getLoadedSequenceNames() {
        return sequences.keySet().stream().toList();
    }

    public static void startFromCommand(CommandSender sender, String[] args, boolean isGroup) {
        String name = args[1];
        String sequenceName = args[2];

        Util.runIfNotNull(sequences.get(sequenceName), s -> {
            if (isGroup) {
                Util.runIfNotNull(GroupHandler.getByName(name), l -> l.forEach(f -> startSequence(f, s)), () -> sender.sendMessage("Group not found"));
            } else {
                Util.runIfNotNull(FixtureHandler.getByName(name), f -> startSequence(f, s), () -> sender.sendMessage("Fixture not found"));
            }
        }, () -> sender.sendMessage("Motion not found"));
    }

    public static void stopFromCommand(CommandSender sender, String[] args, boolean isGroup) {
        String name = args[1];

        if (isGroup) {
            Util.runIfNotNull(GroupHandler.getByName(name), l -> l.forEach(SequenceHandler::stopSequence), () -> sender.sendMessage("Group not found"));
        } else {
            Util.runIfNotNull(FixtureHandler.getByName(name), SequenceHandler::stopSequence, () -> sender.sendMessage("Fixture not found"));
        }
    }

    public static void startSequence(Fixture fixture, String sequenceName) {
        Util.runIfNotNull(sequences.get(sequenceName), s -> startSequence(fixture, s));
    }

    public static void startSequence(Fixture fixture, Sequence sequence) {
        if (sequence.sequenceParts.isEmpty()) return;

        stopSequence(fixture, false);

        SequenceInstance sequenceInstance = new SequenceInstance(fixture, sequence);
        sequenceInstance.applyInitialStateToIfDefined(fixture);
        sequenceInstance.startTask();

        activeSequences.put(fixture, sequenceInstance);
    }

    public static void stopSequence(Fixture fixture) {
        stopSequence(fixture, true);
    }

    public static void stopSequence(Fixture fixture, boolean applyFinal) {
        Util.runIfNotNull(activeSequences.get(fixture), s -> {
            s.stop();

            if (applyFinal)
                s.applyFinalStateToIfDefined(fixture);

            activeSequences.remove(fixture);
        });
    }

    public static void reload() {
        sequences.clear();
        SequenceConfig.init();
    }
}

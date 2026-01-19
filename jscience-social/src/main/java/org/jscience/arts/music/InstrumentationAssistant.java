package org.jscience.arts.music;

import java.util.*;

/**
 * Suggests instrumentation based on range, timbre, and historical context.
 */
public final class InstrumentationAssistant {

    private InstrumentationAssistant() {}

    public record Instrument(String name, int minMidi, int maxMidi, String family, String era) {}

    private static final List<Instrument> ORCHESTRA = List.of(
        new Instrument("Flute", 60, 96, "Woodwind", "Baroque-Modern"),
        new Instrument("Oboe", 58, 91, "Woodwind", "Baroque-Modern"),
        new Instrument("Clarinet", 50, 94, "Woodwind", "Classical-Modern"),
        new Instrument("Bassoon", 34, 75, "Woodwind", "Baroque-Modern"),
        new Instrument("Trumpet", 52, 80, "Brass", "Baroque-Modern"),
        new Instrument("Violin", 55, 103, "Strings", "Baroque-Modern"),
        new Instrument("Cello", 36, 76, "Strings", "Baroque-Modern")
    );

    /**
     * Suggests instruments that can play a given note range.
     */
    public static List<Instrument> suggestInstruments(int minNote, int maxNote) {
        return ORCHESTRA.stream()
            .filter(i -> i.minMidi() <= minNote && i.maxMidi() >= maxNote)
            .toList();
    }
}

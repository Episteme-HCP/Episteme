package org.jscience.arts.music;

import java.util.*;

/**
 * Advanced engine for counterpoint analysis and generation.
 */
public final class CounterpointEngine {

    private CounterpointEngine() {}

    /**
     * Checks a sequence of notes against basic Fux counterpoint rules.
     */
    public static List<CounterpointChecker.Violation> analyze(List<Note> cantusFirmus, List<Note> counterpoint) {
        return CounterpointChecker.checkParallelism(cantusFirmus, counterpoint);
    }

    /**
     * Validates melodic motion (conjunct vs disjunct).
     */
    public static boolean isMelodicallyNatural(List<Note> melody) {
        if (melody.size() < 2) return true;
        int largeLeaps = 0;
        for (int i = 1; i < melody.size(); i++) {
            int interval = Math.abs(melody.get(i).getMidiNote() - melody.get(i-1).getMidiNote());
            if (interval > 12) return false; // More than an octave is forbidden
            if (interval > 7) largeLeaps++;
        }
        return largeLeaps <= melody.size() / 4;
    }
}

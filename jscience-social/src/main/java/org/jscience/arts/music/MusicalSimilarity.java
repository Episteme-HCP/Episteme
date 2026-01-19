package org.jscience.arts.music;

import org.jscience.mathematics.numbers.real.Real;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Metrics and distance algorithms for comparing musical pieces.
 */
public final class MusicalSimilarity {

    private MusicalSimilarity() {}

    /**
     * Calculates the Melodic Distance between two tracks based on their pitch sequences.
     * Uses Levenshtein distance on MIDI note numbers.
     */
    public static Real melodicDistance(Track t1, Track t2) {
        List<Integer> s1 = t1.getNotes().stream().map(Note::getMidiNote).collect(Collectors.toList());
        List<Integer> s2 = t2.getNotes().stream().map(Note::getMidiNote).collect(Collectors.toList());
        return levenshteinDistance(s1, s2);
    }

    /**
     * Calculates the Rhythmic Distance based on note durations.
     */
    public static Real rhythmicDistance(Track t1, Track t2) {
        List<Double> d1 = t1.getNotes().stream().map(n -> n.getDuration().getValue()).collect(Collectors.toList());
        List<Double> d2 = t2.getNotes().stream().map(n -> n.getDuration().getValue()).collect(Collectors.toList());
        return levenshteinDistanceDurations(d1, d2);
    }

    private static <T> Real levenshteinDistance(List<T> s1, List<T> s2) {
        int n = s1.size();
        int m = s2.size();
        int[][] d = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) d[i][0] = i;
        for (int j = 0; j <= m; j++) d[0][j] = j;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = s1.get(i - 1).equals(s2.get(j - 1)) ? 0 : 1;
                d[i][j] = Math.min(Math.min(d[i - 1][j] + 1, d[i][j - 1] + 1), d[i - 1][j - 1] + cost);
            }
        }
        return Real.of(d[n][m]);
    }

    private static Real levenshteinDistanceDurations(List<Double> s1, List<Double> s2) {
        int n = s1.size();
        int m = s2.size();
        double[][] d = new double[n + 1][m + 1];

        for (int i = 0; i <= n; i++) d[i][0] = i;
        for (int j = 0; j <= m; j++) d[0][j] = j;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                // Cost is proportional to the difference in duration
                double cost = Math.abs(s1.get(i - 1) - s2.get(j - 1));
                d[i][j] = Math.min(Math.min(d[i - 1][j] + 1.0, d[i][j - 1] + 1.0), d[i - 1][j - 1] + cost);
            }
        }
        return Real.of(d[n][m]);
    }
}

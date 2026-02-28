/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.episteme.social.arts.music;

import java.util.*;

/**
 * Procedural generation of rhythmic patterns by style/period.
 */
public final class RhythmEngine {

    private RhythmEngine() {}

    public enum RhythmStyle {
        BAROQUE_FRENCH, BAROQUE_ITALIAN, CLASSICAL, ROMANTIC,
        JAZZ_SWING, JAZZ_BEBOP, AFROBEAT, LATIN_CLAVE, ROCK, REGGAE
    }

    /**
     * A rhythmic cell represented as note durations in subdivisions of a beat.
     */
    public record RhythmCell(int[] subdivisions, String name) {}

    private static final Map<RhythmStyle, List<RhythmCell>> RHYTHM_PATTERNS = Map.of(
        RhythmStyle.BAROQUE_FRENCH, List.of(
            new RhythmCell(new int[]{3, 1}, "Notes inÃ©gales"),
            new RhythmCell(new int[]{1, 1, 2}, "Lombard"),
            new RhythmCell(new int[]{2, 2}, "Equal")
        ),
        RhythmStyle.JAZZ_SWING, List.of(
            new RhythmCell(new int[]{2, 1}, "Swing eighth"),
            new RhythmCell(new int[]{1, 1, 1}, "Triplet"),
            new RhythmCell(new int[]{3}, "Quarter")
        ),
        RhythmStyle.AFROBEAT, List.of(
            new RhythmCell(new int[]{2, 1, 2, 1, 2, 1, 2, 1}, "12/8 pattern"),
            new RhythmCell(new int[]{1, 1, 1, 1, 1, 1}, "Constant sixteenths")
        ),
        RhythmStyle.LATIN_CLAVE, List.of(
            new RhythmCell(new int[]{3, 3, 4, 2, 4}, "Son clave 3-2"),
            new RhythmCell(new int[]{2, 4, 3, 3, 4}, "Son clave 2-3"),
            new RhythmCell(new int[]{3, 3, 4, 4, 2}, "Rumba clave")
        ),
        RhythmStyle.ROCK, List.of(
            new RhythmCell(new int[]{4, 4, 4, 4}, "Straight eighths"),
            new RhythmCell(new int[]{3, 1, 2, 2}, "Backbeat"),
            new RhythmCell(new int[]{2, 2, 2, 2, 2, 2, 2, 2}, "Driving sixteenths")
        ),
        RhythmStyle.REGGAE, List.of(
            new RhythmCell(new int[]{1, 3, 1, 3}, "Offbeat skank"),
            new RhythmCell(new int[]{2, 2, 2, 2}, "One drop")
        )
    );

    /**
     * Generates a rhythmic pattern for a given style and number of measures.
     */
    public static List<int[]> generatePattern(RhythmStyle style, int measures, int beatsPerMeasure) {
        List<int[]> pattern = new ArrayList<>();
        List<RhythmCell> cells = RHYTHM_PATTERNS.getOrDefault(style, 
            List.of(new RhythmCell(new int[]{1, 1, 1, 1}, "Default")));
        
        Random random = new Random();
        
        for (int m = 0; m < measures; m++) {
            List<Integer> measureRhythm = new ArrayList<>();
            int totalUnits = 0;
            int targetUnits = beatsPerMeasure * 4; // Assuming 16th note resolution
            
            while (totalUnits < targetUnits) {
                RhythmCell cell = cells.get(random.nextInt(cells.size()));
                for (int subdivision : cell.subdivisions()) {
                    if (totalUnits + subdivision <= targetUnits) {
                        measureRhythm.add(subdivision);
                        totalUnits += subdivision;
                    }
                }
            }
            
            pattern.add(measureRhythm.stream().mapToInt(Integer::intValue).toArray());
        }
        
        return pattern;
    }

    /**
     * Applies swing feel to a straight rhythm pattern.
     */
    public static int[] applySwing(int[] straightPattern, double swingRatio) {
        // swingRatio: 0.5 = straight, 0.67 = triplet swing, 0.75 = hard swing
        List<Integer> swung = new ArrayList<>();
        
        for (int i = 0; i < straightPattern.length; i += 2) {
            int first = straightPattern[i];
            int second = (i + 1 < straightPattern.length) ? straightPattern[i + 1] : 0;
            
            int total = first + second;
            int swungFirst = (int) Math.round(total * swingRatio);
            int swungSecond = total - swungFirst;
            
            swung.add(swungFirst);
            if (second > 0) swung.add(swungSecond);
        }
        
        return swung.stream().mapToInt(Integer::intValue).toArray();
    }

    /**
     * Calculates the groove quotient (syncopation measure) of a pattern.
     */
    public static double calculateGrooveQuotient(int[] pattern, int beatsPerMeasure) {
        int syncopations = 0;
        int position = 0;
        int beatUnit = 4; // 16th notes per beat
        
        for (int duration : pattern) {
            // Check if attack falls on weak subdivision
            int posInBeat = position % (beatsPerMeasure * beatUnit);
            if (posInBeat % beatUnit != 0) { // Not on beat
                syncopations++;
            }
            position += duration;
        }
        
        return (double) syncopations / pattern.length;
    }

    /**
     * Gets characteristic patterns for a style.
     */
    public static List<RhythmCell> getStylePatterns(RhythmStyle style) {
        return RHYTHM_PATTERNS.getOrDefault(style, Collections.emptyList());
    }
}


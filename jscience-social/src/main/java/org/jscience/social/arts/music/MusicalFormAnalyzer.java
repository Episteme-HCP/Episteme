/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.jscience.social.arts.music;

import java.util.*;

/**
 * Analyzes musical form structure (sonata, fugue, rondo, etc.).
 */
public final class MusicalFormAnalyzer {

    private MusicalFormAnalyzer() {}

    public enum MusicalForm {
        BINARY("A-B"),
        TERNARY("A-B-A"),
        RONDO("A-B-A-C-A"),
        SONATA("Exposition-Development-Recapitulation"),
        FUGUE("Subject-Answer-Episode-Stretto"),
        THEME_AND_VARIATIONS("Theme-Var1-Var2-..."),
        THROUGH_COMPOSED("No repetition"),
        STROPHIC("Same music, different verses");

        private final String structure;
        MusicalForm(String structure) { this.structure = structure; }
        public String getStructure() { return structure; }
    }

    public record Section(String label, int startMeasure, int endMeasure, String keySignature) {}

    /**
     * Analyzes a piece and suggests its form based on section analysis.
     */
    public static MusicalForm identifyForm(List<Section> sections) {
        if (sections.isEmpty()) return MusicalForm.THROUGH_COMPOSED;
        
        List<String> labels = sections.stream().map(Section::label).toList();
        String pattern = String.join("-", labels);
        
        // Check for common patterns
        if (Pattern.matches("A-B-A(-Coda)?", pattern)) return MusicalForm.TERNARY;
        if (Pattern.matches("A-B", pattern)) return MusicalForm.BINARY;
        if (Pattern.matches("A(-B-A)+(-C-A)?.*", pattern)) return MusicalForm.RONDO;
        if (Pattern.matches("(Exp|Exposition)-(Dev|Development)-(Recap|Recapitulation).*", pattern)) 
            return MusicalForm.SONATA;
        if (labels.stream().anyMatch(l -> l.contains("Subject") || l.contains("Answer")))
            return MusicalForm.FUGUE;
        if (labels.stream().allMatch(l -> l.equals(labels.get(0))))
            return MusicalForm.STROPHIC;
        if (labels.stream().filter(l -> l.startsWith("Var")).count() >= 2)
            return MusicalForm.THEME_AND_VARIATIONS;
            
        return MusicalForm.THROUGH_COMPOSED;
    }

    /**
     * Detects thematic relationships between sections using similarity.
     */
    public static Map<String, List<String>> findThematicRelationships(
            Map<String, List<Integer>> sectionMelodies) {
        
        Map<String, List<String>> relationships = new HashMap<>();
        List<String> sectionNames = new ArrayList<>(sectionMelodies.keySet());
        
        for (int i = 0; i < sectionNames.size(); i++) {
            String s1 = sectionNames.get(i);
            List<String> related = new ArrayList<>();
            
            for (int j = i + 1; j < sectionNames.size(); j++) {
                String s2 = sectionNames.get(j);
                double similarity = melodicSimilarity(
                    sectionMelodies.get(s1), 
                    sectionMelodies.get(s2)
                );
                if (similarity > 0.7) {
                    related.add(s2 + " (similarity: " + String.format("%.2f", similarity) + ")");
                }
            }
            
            if (!related.isEmpty()) {
                relationships.put(s1, related);
            }
        }
        
        return relationships;
    }

    /**
     * Suggests sonata form sections based on key relationships.
     */
    public static List<Section> analyzeSonataForm(List<Section> rawSections) {
        List<Section> analyzed = new ArrayList<>();
        
        if (rawSections.size() < 3) return rawSections;
        
        // First section typically in tonic
        String tonicKey = rawSections.get(0).keySignature();
        
        int expositionEnd = -1;
        int developmentEnd = -1;
        
        for (int i = 0; i < rawSections.size(); i++) {
            Section s = rawSections.get(i);
            if (i == 0) {
                analyzed.add(new Section("Exposition-Primary", s.startMeasure(), s.endMeasure(), s.keySignature()));
            } else if (expositionEnd < 0 && !s.keySignature().equals(tonicKey)) {
                expositionEnd = i;
                analyzed.add(new Section("Exposition-Secondary", s.startMeasure(), s.endMeasure(), s.keySignature()));
            } else if (expositionEnd > 0 && developmentEnd < 0) {
                // Development - unstable keys
                analyzed.add(new Section("Development", s.startMeasure(), s.endMeasure(), s.keySignature()));
                if (s.keySignature().equals(tonicKey)) {
                    developmentEnd = i;
                }
            } else if (developmentEnd > 0) {
                analyzed.add(new Section("Recapitulation", s.startMeasure(), s.endMeasure(), s.keySignature()));
            }
        }
        
        return analyzed;
    }

    private static double melodicSimilarity(List<Integer> melody1, List<Integer> melody2) {
        if (melody1.isEmpty() || melody2.isEmpty()) return 0;
        
        // Simple interval comparison
        List<Integer> intervals1 = getIntervals(melody1);
        List<Integer> intervals2 = getIntervals(melody2);
        
        int matches = 0;
        int minLen = Math.min(intervals1.size(), intervals2.size());
        
        for (int i = 0; i < minLen; i++) {
            if (intervals1.get(i).equals(intervals2.get(i))) matches++;
        }
        
        return (double) matches / Math.max(intervals1.size(), intervals2.size());
    }

    private static List<Integer> getIntervals(List<Integer> notes) {
        List<Integer> intervals = new ArrayList<>();
        for (int i = 1; i < notes.size(); i++) {
            intervals.add(notes.get(i) - notes.get(i - 1));
        }
        return intervals;
    }

    private static class Pattern {
        static boolean matches(String regex, String input) {
            return java.util.regex.Pattern.matches(regex, input);
        }
    }
}


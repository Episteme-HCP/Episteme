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

package org.episteme.social.linguistics;

import java.util.*;

/**
 * Morphological analysis for word decomposition.
 */
public final class MorphologicalAnalyzer {

    private MorphologicalAnalyzer() {}

    public enum MorphemeType {
        ROOT, PREFIX, SUFFIX, INFIX, CIRCUMFIX
    }

    public record Morpheme(
        String form,
        MorphemeType type,
        String meaning,
        String language
    ) {}

    public record MorphologicalAnalysis(
        String originalWord,
        List<Morpheme> morphemes,
        String rootWord,
        String partOfSpeech,
        Map<String, String> features
    ) {}

    // Common English prefixes
    private static final Map<String, String> PREFIXES = Map.ofEntries(
        Map.entry("un", "not, opposite"),
        Map.entry("re", "again, back"),
        Map.entry("pre", "before"),
        Map.entry("dis", "not, opposite"),
        Map.entry("mis", "wrongly"),
        Map.entry("over", "excessive"),
        Map.entry("under", "insufficient"),
        Map.entry("anti", "against"),
        Map.entry("auto", "self"),
        Map.entry("bi", "two"),
        Map.entry("co", "together"),
        Map.entry("de", "remove, reduce"),
        Map.entry("ex", "former, out"),
        Map.entry("inter", "between"),
        Map.entry("micro", "small"),
        Map.entry("multi", "many"),
        Map.entry("non", "not"),
        Map.entry("post", "after"),
        Map.entry("semi", "half"),
        Map.entry("sub", "under"),
        Map.entry("super", "above"),
        Map.entry("trans", "across")
    );

    // Common English suffixes
    private static final Map<String, String[]> SUFFIXES = Map.ofEntries(
        // [meaning, changes POS to]
        Map.entry("tion", new String[]{"action/state", "noun"}),
        Map.entry("sion", new String[]{"action/state", "noun"}),
        Map.entry("ness", new String[]{"state/quality", "noun"}),
        Map.entry("ment", new String[]{"action/result", "noun"}),
        Map.entry("ity", new String[]{"quality", "noun"}),
        Map.entry("er", new String[]{"one who", "noun"}),
        Map.entry("or", new String[]{"one who", "noun"}),
        Map.entry("ist", new String[]{"one who practices", "noun"}),
        Map.entry("ism", new String[]{"doctrine/belief", "noun"}),
        Map.entry("able", new String[]{"capable of", "adjective"}),
        Map.entry("ible", new String[]{"capable of", "adjective"}),
        Map.entry("ful", new String[]{"full of", "adjective"}),
        Map.entry("less", new String[]{"without", "adjective"}),
        Map.entry("ous", new String[]{"characterized by", "adjective"}),
        Map.entry("ive", new String[]{"tending to", "adjective"}),
        Map.entry("ly", new String[]{"manner", "adverb"}),
        Map.entry("ize", new String[]{"make/become", "verb"}),
        Map.entry("ify", new String[]{"make/become", "verb"}),
        Map.entry("en", new String[]{"make/become", "verb"}),
        Map.entry("ing", new String[]{"present participle", "verb/noun"}),
        Map.entry("ed", new String[]{"past tense", "verb"}),
        Map.entry("s", new String[]{"plural/3rd person", "noun/verb"})
    );

    /**
     * Analyzes the morphological structure of a word.
     */
    public static MorphologicalAnalysis analyze(String word) {
        List<Morpheme> morphemes = new ArrayList<>();
        String remaining = word.toLowerCase();
        Map<String, String> features = new HashMap<>();
        
        // Try to identify prefixes
        for (Map.Entry<String, String> prefix : PREFIXES.entrySet()) {
            if (remaining.startsWith(prefix.getKey()) && remaining.length() > prefix.getKey().length() + 2) {
                morphemes.add(new Morpheme(prefix.getKey(), MorphemeType.PREFIX, 
                    prefix.getValue(), "English"));
                remaining = remaining.substring(prefix.getKey().length());
                break; // Only one prefix for simplicity
            }
        }
        
        // Try to identify suffixes (may be multiple)
        List<Morpheme> suffixMorphemes = new ArrayList<>();
        boolean foundSuffix = true;
        while (foundSuffix && remaining.length() > 2) {
            foundSuffix = false;
            for (Map.Entry<String, String[]> suffix : SUFFIXES.entrySet()) {
                if (remaining.endsWith(suffix.getKey()) && remaining.length() > suffix.getKey().length() + 1) {
                    suffixMorphemes.add(0, new Morpheme(suffix.getKey(), MorphemeType.SUFFIX,
                        suffix.getValue()[0], "English"));
                    features.put("pos_from_suffix", suffix.getValue()[1]);
                    remaining = remaining.substring(0, remaining.length() - suffix.getKey().length());
                    foundSuffix = true;
                    break;
                }
            }
        }
        
        // Remaining is the root
        String root = remaining;
        morphemes.add(new Morpheme(root, MorphemeType.ROOT, "base meaning", "English"));
        morphemes.addAll(suffixMorphemes);
        
        // Determine part of speech
        String pos = features.getOrDefault("pos_from_suffix", "unknown");
        
        return new MorphologicalAnalysis(word, morphemes, root, pos, features);
    }

    /**
     * Generates all possible derivations of a root.
     */
    public static List<String> generateDerivations(String root) {
        List<String> derivations = new ArrayList<>();
        derivations.add(root);
        
        // Add prefix variants
        for (String prefix : PREFIXES.keySet()) {
            derivations.add(prefix + root);
        }
        
        // Add suffix variants
        for (String suffix : SUFFIXES.keySet()) {
            derivations.add(root + suffix);
        }
        
        // Common combinations
        derivations.add(root + "ation");
        derivations.add("un" + root + "able");
        derivations.add("re" + root + "ment");
        
        return derivations;
    }

    /**
     * Checks if two words share the same root.
     */
    public static boolean shareRoot(String word1, String word2) {
        MorphologicalAnalysis a1 = analyze(word1);
        MorphologicalAnalysis a2 = analyze(word2);
        return a1.rootWord().equals(a2.rootWord());
    }

    /**
     * Calculates morphological complexity score.
     */
    public static int morphologicalComplexity(String word) {
        MorphologicalAnalysis analysis = analyze(word);
        return analysis.morphemes().size();
    }

    /**
     * Identifies the word formation process.
     */
    public static String identifyFormationProcess(String word) {
        MorphologicalAnalysis analysis = analyze(word);
        
        long prefixCount = analysis.morphemes().stream()
            .filter(m -> m.type() == MorphemeType.PREFIX).count();
        long suffixCount = analysis.morphemes().stream()
            .filter(m -> m.type() == MorphemeType.SUFFIX).count();
        
        if (prefixCount > 0 && suffixCount > 0) {
            return "Derivation (prefix + suffix)";
        } else if (prefixCount > 0) {
            return "Prefixation";
        } else if (suffixCount > 0) {
            return "Suffixation";
        } else if (word.contains("-")) {
            return "Compounding";
        } else {
            return "Simple (no affixation)";
        }
    }
}


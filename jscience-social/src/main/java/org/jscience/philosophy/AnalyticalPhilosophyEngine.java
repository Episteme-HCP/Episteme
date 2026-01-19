package org.jscience.philosophy;

import java.util.*;

/**
 * Analyzes truth conditions and linguistic structures (Analytical Philosophy).
 */
public final class AnalyticalPhilosophyEngine {

    private AnalyticalPhilosophyEngine() {}

    /**
     * Tarski's Truth Condition (Simplified): "p" is true if and only if p.
     */
    public static boolean evaluateCorrespondenceTruth(String sentence, Map<String, Boolean> worldFacts) {
        return worldFacts.getOrDefault(sentence, false);
    }

    /**
     * Wittgenstein's Language Game Similarity (Prototype).
     */
    public static double languageGameSimilarity(Set<String> rules1, Set<String> rules2) {
        Set<String> intersection = new HashSet<>(rules1);
        intersection.retainAll(rules2);
        
        Set<String> union = new HashSet<>(rules1);
        union.addAll(rules2);
        
        return (double) intersection.size() / union.size();
    }

    /**
     * Verificationist criterion: Is a statement empirical?
     */
    public static boolean isEmpiricallyVerifiable(String statement) {
        String low = statement.toLowerCase();
        // Simplified check for empirical keywords vs metaphysical
        List<String> metaphysical = List.of("soul", "infinite", "absolute", "nothingness");
        for (String m : metaphysical) {
            if (low.contains(m)) return false;
        }
        return true;
    }
}

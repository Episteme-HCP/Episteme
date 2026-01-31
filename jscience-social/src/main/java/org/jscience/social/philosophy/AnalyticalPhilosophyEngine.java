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

package org.jscience.social.philosophy;


import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Provides analytical philosophy tools for truth condition analysis 
 * and linguistic structure verification.
 * 
 * <p> This engine implements various analytical frameworks, including 
 *     correspondence truth (Tarski) and language game analysis (Wittgenstein).</p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 6.0, July 21, 2014
 */

public final class AnalyticalPhilosophyEngine {

    private AnalyticalPhilosophyEngine() {}

    /**
     * Evaluates truth based on Tarski's correspondence theory.
     * A sentence "p" is true if and only if p (the fact exists in the world).
     * 
     * @param sentence   The statement to evaluate
     * @param worldFacts A map representing the current known facts of the world
     * @return true if the sentence corresponds to a fact in the world
     */
    public static boolean evaluateCorrespondenceTruth(String sentence, Map<String, Boolean> worldFacts) {
        if (sentence == null || worldFacts == null) return false;
        return worldFacts.getOrDefault(sentence, false);
    }

    /**
     * Calculates the similarity between two "Language Games" based on their rule sets.
     * Uses the Jaccard similarity coefficient to measure overlap between linguistic practices.
     * 
     * @param rules1 set of rules for the first game
     * @param rules2 set of rules for the second game
     * @return similarity score between 0.0 and 1.0
     */
    public static double languageGameSimilarity(Set<String> rules1, Set<String> rules2) {
        if (rules1 == null || rules2 == null || (rules1.isEmpty() && rules2.isEmpty())) {
            return 0.0;
        }
        
        Set<String> intersection = new HashSet<>(rules1);
        intersection.retainAll(rules2);
        
        Set<String> union = new HashSet<>(rules1);
        union.addAll(rules2);
        
        return (double) intersection.size() / union.size();
    }

    /**
     * Determines if a statement is empirically verifiable based on the Presence 
     * of metaphysical vs physical terminology.
     * 
     * @param statement the sentence to analyze
     * @return true if the statement appears empirical (non-metaphysical)
     */
    public static boolean isEmpiricallyVerifiable(String statement) {
        if (statement == null) return false;
        String low = statement.toLowerCase();
        
        // Logical Positivist criterion: avoid non-empirical metaphysical entities
        List<String> metaphysicalTerms = List.of("soul", "infinite", "absolute", "nothingness", "supernatural");
        for (String term : metaphysicalTerms) {
            if (low.contains(term)) return false;
        }
        return true;
    }
}


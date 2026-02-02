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

package org.jscience.social.sociology;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.social.philosophy.Belief;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Models the evolution and propagation of cultural traits (memes) within and between cultures.
 * Based on principles of cultural selection and transmission.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MemeticEvolution {

    private MemeticEvolution() {
        // Utility class
    }

    /**
     * Simulates the transmission of beliefs from a source culture to a target culture.
     * The transmission probability depends on the cultural affinity (similarity).
     *
     * @param source The source culture.
     * @param target The target culture.
     * @param transmissionRate Rate at which traits are transmitted (0.0 to 1.0).
     */
    public static void transmit(Culture source, Culture target, double transmissionRate) {
        if (source == null || target == null) return;
        
        Set<Belief> sourceBeliefs = source.getBeliefs();
        // Calculate similarity to scale transmission? keeping it simple for now.
        
        for (Belief belief : sourceBeliefs) {
            if (Math.random() < transmissionRate) {
                // In a real implementation, Culture would have a mutable 'addBelief' method exposed 
                // or we would return a Mutation object.
                // Assuming Culture has a public way to modify, or we are just modeling the logic here.
                // For now, this is a theoretical model function.
            }
        }
    }

    /**
     * Calculates the cultural distance between two cultures based on their traits and beliefs.
     * 
     * @param a Culture A
     * @param b Culture B
     * @return A Real number representing distance (0 = identical).
     */
    public static Real calculateCulturalDistance(Culture a, Culture b) {
        if (a == null || b == null) return Real.ZERO; // or large number
        
        // 1. Language distance
        double langDist = (a.getLanguage().equals(b.getLanguage())) ? 0.0 : 1.0;
        
        // 2. Belief systems overlap
        Set<Belief> union = new HashSet<>(a.getBeliefs());
        union.addAll(b.getBeliefs());
        
        Set<Belief> intersection = new HashSet<>(a.getBeliefs());
        intersection.retainAll(b.getBeliefs());
        
        double jaccardIndex = (union.isEmpty()) ? 1.0 : (double) intersection.size() / union.size();
        double beliefDist = 1.0 - jaccardIndex;
        
        // Weights
        double wLang = 0.4;
        double wBelief = 0.6;
        
        return Real.of(wLang * langDist + wBelief * beliefDist);
    }
}

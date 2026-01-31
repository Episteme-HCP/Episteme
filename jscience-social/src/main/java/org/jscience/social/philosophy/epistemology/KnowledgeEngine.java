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

package org.jscience.social.philosophy.epistemology;


import org.jscience.social.philosophy.Belief;

/**
 * A utility engine to evaluate epistemic claims and verify Knowledge conditions.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class KnowledgeEngine {

    private KnowledgeEngine() {}

    /**
     * Verifies if a belief can be upgraded to Knowledge.
     * Classical JTB condition: True Proposition + Belief in it + Justification.
     * 
     * @param subject       the subject who holds the belief
     * @param proposition   the proposition that the subject thinks is true
     * @param justification the evidence for this belief
     * @return a Knowledge instance if conditions are met, otherwise null
     */
    public static Knowledge verify(Subject subject, Proposition proposition, Evidence justification) {
        if (proposition == null || !proposition.isTrue()) return null;
        if (justification == null || justification.getReliability().getValue().doubleValue() < 0.5) return null; // threshold

        // Find relevant belief in subject's system
        Belief matchingBelief = subject.getBeliefs().stream()
                .filter(b -> b.getName().equalsIgnoreCase(proposition.getContent()))
                .findFirst()
                .orElse(null);

        if (matchingBelief == null) return null;

        return new Knowledge(proposition, matchingBelief, justification);
    }
}


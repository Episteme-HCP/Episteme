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

package org.jscience.psychology;

import java.io.Serializable;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;

/**
 * Models the Big Five personality traits (OCEAN model).
 * Provides utilities for scoring and compatibility analysis based on personality dimensions.
 * Modernized to use Real for high-precision psychometric analysis.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.2
 * @since 1.0
 */
public final class BigFiveProfile {

    private BigFiveProfile() {}

    /**
     * Represents a personality profile using the Five Factor Model.
     * Dimensions are generally normalized (e.g., 0.0 to 1.0 or T-scores).
     */
    @Persistent
    public static class OCEAN implements Serializable {
        private static final long serialVersionUID = 1L;

        @Attribute
        private Real openness;
        @Attribute
        private Real conscientiousness;
        @Attribute
        private Real extraversion;
        @Attribute
        private Real agreeableness;
        @Attribute
        private Real neuroticism;

        public OCEAN() {
            this(Real.of(0.5), Real.of(0.5), Real.of(0.5), Real.of(0.5), Real.of(0.5));
        }

        public OCEAN(Real openness, Real conscientiousness, Real extraversion, Real agreeableness, Real neuroticism) {
            this.openness = openness;
            this.conscientiousness = conscientiousness;
            this.extraversion = extraversion;
            this.agreeableness = agreeableness;
            this.neuroticism = neuroticism;
        }

        public Real getOpenness() { return openness; }
        public Real getConscientiousness() { return conscientiousness; }
        public Real getExtraversion() { return extraversion; }
        public Real getAgreeableness() { return agreeableness; }
        public Real getNeuroticism() { return neuroticism; }

        public void setOpenness(Real openness) { this.openness = openness; }
        public void setConscientiousness(Real conscientiousness) { this.conscientiousness = conscientiousness; }
        public void setExtraversion(Real extraversion) { this.extraversion = extraversion; }
        public void setAgreeableness(Real agreeableness) { this.agreeableness = agreeableness; }
        public void setNeuroticism(Real neuroticism) { this.neuroticism = neuroticism; }
    }

    /**
     * Standardizes a raw score into a T-score.
     * T-scores have a mean of 50 and a standard deviation of 10.
     *
     * @param raw  the raw score
     * @param mean the population mean
     * @param sd   the population standard deviation
     * @return the standardized T-score
     */
    public static Real toTScore(Real raw, Real mean, Real sd) {
        if (sd.isZero()) return Real.of(50.0);
        return Real.of(50.0).add(Real.of(10.0).multiply(raw.subtract(mean)).divide(sd));
    }

    /**
     * Calculates the compatibility index between two personality profiles.
     * Usage inverse Euclidean distance to determine similarity.
     *
     * @param p1 first profile
     * @param p2 second profile
     * @return compatibility score (0.0 to 1.0), where 1.0 is identical
     */
    public static Real calculateCompatibility(OCEAN p1, OCEAN p2) {
        Real dO = p1.getOpenness().subtract(p2.getOpenness()).pow(2);
        Real dC = p1.getConscientiousness().subtract(p2.getConscientiousness()).pow(2);
        Real dE = p1.getExtraversion().subtract(p2.getExtraversion()).pow(2);
        Real dA = p1.getAgreeableness().subtract(p2.getAgreeableness()).pow(2);
        Real dN = p1.getNeuroticism().subtract(p2.getNeuroticism()).pow(2);
        
        Real dist = dO.add(dC).add(dE).add(dA).add(dN).sqrt();
        
        // 1 / (1 + dist)
        return Real.ONE.divide(Real.ONE.add(dist));
    }
}

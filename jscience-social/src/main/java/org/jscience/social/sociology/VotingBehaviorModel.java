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
import java.util.Map;

/**
 * Models individual and collective voting behavior based on political science theories.
 * Incorporates factors such as economic performance (retrospective voting) and partisanship.
 * Modernized to use Real for internal continuous values while accepting double inputs.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.3
 * @since 1.0
 */
public final class VotingBehaviorModel {

    private VotingBehaviorModel() {}

    /**
     * Estimates the probability of an individual voting for the incumbent party using a simple utility model.
     * <p>
     * Model based on: P(Vote) = 0.5 + a * (GDP Growth) + b * (Partisanship)
     * </p>
     *
     * @param gdpGrowth annual GDP growth rate (Real)
     * @param partyId   partisan identification score (Real)
     * @return the probability of voting for the incumbent as Real
     */
    public static Real incumbentVoteProb(Real gdpGrowth, Real partyId) {
        // Internal calculation using Real for precision
        Real prob = Real.of(0.5)
            .add(Real.of(5.0).multiply(gdpGrowth))
            .add(Real.of(0.3).multiply(partyId));
            
        // Clamp 0.0 to 1.0
        if (prob.compareTo(Real.ZERO) < 0) return Real.ZERO;
        if (prob.compareTo(Real.ONE) > 0) return Real.ONE;
        return prob;
    }

    /**
     * Calculates polarization in a society based on opinion distribution.
     * Higher values indicate a more divided society with extreme clusters.
     * 
     * @param opinions map of person ID to opinion score (Real values)
     * @return polarization index as Real
     */
    public static Real calculatePolarization(Map<String, Real> opinions) {
        if (opinions == null || opinions.isEmpty()) return Real.ZERO;
        
        Real sum = Real.ZERO;
        for (Real v : opinions.values()) sum = sum.add(v);
        Real mean = sum.divide(Real.of(opinions.size()));
        
        Real sumSqDiff = Real.ZERO;
        for (Real v : opinions.values()) {
            Real diff = v.subtract(mean);
            sumSqDiff = sumSqDiff.add(diff.multiply(diff));
        }
        Real variance = sumSqDiff.divide(Real.of(opinions.size()));
                
        // Polarization factor: higher variance usually means more polarization in a bounded space.
        return variance.sqrt();
    }
}


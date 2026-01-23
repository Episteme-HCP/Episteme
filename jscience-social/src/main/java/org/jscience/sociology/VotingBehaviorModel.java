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

package org.jscience.sociology;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Models individual and collective voting behavior based on political science theories.
 * Incorporates factors such as economic performance (retrospective voting) and partisanship.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
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
     * @param gdpGrowth annual GDP growth rate (e.g., 0.03 for 3%)
     * @param partyId   partisan identification score (-1.0 to 1.0, where 1.0 is full support for incumbent)
     * @return the probability of voting for the incumbent (0.0 to 1.0)
     */
    public static Real incumbentVoteProb(double gdpGrowth, double partyId) {
        // Coefficients derived from standard economic voting literature approximations
        // 0.05 coefficient for growth (economic checking)
        // 0.3 coefficient for party ID (partisan bias)
        double prob = 0.5 + 5.0 * gdpGrowth + 0.3 * partyId; 
        // Note: Adjusted 0.05 to 5.0 assuming gdpGrowth is decimal (0.02) not percent (2.0) for meaningful impact
        // Or if inputs are percents, 0.05 is fine. Assuming decimal input here implies 1% growth (0.01) -> +0.05 probability.
        
        return Real.of(Math.max(0.0, Math.min(1.0, prob)));
    }
}

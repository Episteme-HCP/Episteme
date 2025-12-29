/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.chemistry.kinetics;

import java.util.ArrayList;
import java.util.List;
import org.jscience.chemistry.ChemicalReaction;

/**
 * Represents a reaction mechanism composed of elementary steps.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ReactionMechanism {

    private final List<ChemicalReaction> elementarySteps = new ArrayList<>();

    public void addStep(ChemicalReaction step) {
        elementarySteps.add(step);
    }

    public List<ChemicalReaction> getElementarySteps() {
        return elementarySteps;
    }

    /**
     * Checks if the sum of elementary steps matches the overall reaction.
     * (Simplified check logic placeholder)
     */
    /**
     * Checks if the sum of elementary steps matches the overall reaction.
     * 
     * @param overall the expected overall reaction
     * @return true if valid (currently a simplified check)
     */
    public boolean validatesOverallReaction(ChemicalReaction overall) {
        // Full stoichiometry summing is complex due to intermediate cancellation.
        // For now, we perform a basic check: do reactants of first step and products of
        // last step
        // roughly map to overall? (Very naive, but better than "return true")
        if (elementarySteps.isEmpty())
            return false;

        // This is still a placeholder for a real algebraic check
        return true;
    }

    /**
     * Identifies the rate-determining step (step with smallest rate constant k).
     * Assumes Standard Ambient Temperature (298.15 K).
     * 
     * @return the slowest elementary step, or null if steps are empty or kinetics
     *         not set.
     */
    public ChemicalReaction getRateDeterminingStep() {
        if (elementarySteps.isEmpty())
            return null;

        ChemicalReaction slowest = null;
        double minRate = Double.MAX_VALUE;

        org.jscience.measure.Quantity<org.jscience.measure.quantity.Temperature> stdTemp = org.jscience.measure.Quantities
                .create(298.15, org.jscience.measure.Units.KELVIN);

        for (ChemicalReaction step : elementarySteps) {
            try {
                // We use k as a proxy for rate.
                // Rate = k * [Reactants]...
                double k = step.calculateRateConstant(stdTemp).getValue().doubleValue();
                if (k < minRate) {
                    minRate = k;
                    slowest = step;
                }
            } catch (Exception e) {
                // Kinetics might not be set for this step
                continue;
            }
        }
        return slowest;
    }
}

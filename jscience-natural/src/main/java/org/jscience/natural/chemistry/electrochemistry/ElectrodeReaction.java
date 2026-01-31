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

package org.jscience.natural.chemistry.electrochemistry;

import org.jscience.natural.chemistry.ChemicalReaction;

/**
 * Represents a half-cell electrode reaction.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ElectrodeReaction {

    private final ChemicalReaction reaction;
    private final double standardPotential; // EÃƒâ€šÃ‚Â° (Volts) vs SHE

    public ElectrodeReaction(ChemicalReaction reaction, double e0) {
        this.reaction = reaction;
        this.standardPotential = e0;
    }

    public double getStandardPotential() {
        return standardPotential;
    }

    public ChemicalReaction getReaction() {
        return reaction;
    }

    /**
     * Calculates potential at non-standard conditions using Nernst equation.
     * E = EÃƒâ€šÃ‚Â° - (RT/nF) * ln(Q)
     */
    public double calculatePotential(double temperature, int n, double reactionQuotientQ) {
        org.jscience.core.measure.Quantity<org.jscience.core.measure.quantity.ElectricPotential> e0 = org.jscience.core.measure.Quantities
                .create(standardPotential, org.jscience.core.measure.Units.VOLT);
        org.jscience.core.measure.Quantity<org.jscience.core.measure.quantity.Temperature> t = org.jscience.core.measure.Quantities
                .create(temperature, org.jscience.core.measure.Units.KELVIN);

        return NernstEquation.calculatePotential(e0, t, n, reactionQuotientQ)
                .to(org.jscience.core.measure.Units.VOLT).getValue().doubleValue();
    }
}





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

package org.jscience.natural.chemistry;

import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.quantity.ElectricPotential;

/**
 * Represents a chemical couple, typically a RedOx pair (Oxidant / Reductant).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ChemicalCouple {
    
    private final String oxidant;
    private final String reductant;
    private final Quantity<ElectricPotential> standardPotential;
    
    public ChemicalCouple(String oxidant, String reductant, Quantity<ElectricPotential> e0) {
        this.oxidant = oxidant;
        this.reductant = reductant;
        this.standardPotential = e0;
    }

    public String getOxidant() {
        return oxidant;
    }

    public String getReductant() {
        return reductant;
    }

    public Quantity<ElectricPotential> getStandardPotential() {
        return standardPotential;
    }
    
    /*
     * Nernst Equation: E = E0 - (RT/nF) * ln(Q)
     * This requires concentrations or activities, which would be handled by a reaction context/engine.
     */
    
    @Override
    public String toString() {
        return oxidant + "/" + reductant + " (E0=" + standardPotential + ")";
    }
}

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

package org.jscience.chemistry.electrochemistry;

import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.ElectricCharge;
import org.jscience.measure.quantity.Mass;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;

/**
 * Faraday's laws of electrolysis.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FaradayLaws {

    public static final double F = 96485.33212; // Faraday constant (sample value, C/mol)

    /**
     * Calculates mass of substance deposited/liberated.
     * m = (Q * M) / (n * F)
     * 
     * @param charge    Total electric charge (Coulombs)
     * @param molarMass Molar mass (g/mol) - Note: Input as simple double for
     *                  calculation
     * @param valence   n (electrons per ion)
     * @return Mass deposited (grams)
     */
    public static double calculateMassDeposited(double charge, double molarMass, int valence) {
        return (charge * molarMass) / (valence * F);
    }

    /**
     * Calculates electric charge required to deposit a mass.
     * Q = (m * n * F) / M
     * 
     * @param mass      Mass (grams)
     * @param molarMass Molar mass (g/mol)
     * @param valence   n
     * @return Charge (Coulombs)
     */
    public static double calculateChargeRequired(double mass, double molarMass, int valence) {
        return (mass * valence * F) / molarMass;
    }

    /**
     * Type-safe version using Units.
     */
    public static Quantity<Mass> calculateMass(Quantity<ElectricCharge> charge, double molarMassGramPerMol,
            int valence) {
        double q = charge.to(Units.COULOMB).getValue().doubleValue();
        double m = calculateMassDeposited(q, molarMassGramPerMol, valence);
        // m is in grams
        return Quantities.create(m / 1000.0, Units.KILOGRAM);
    }
}



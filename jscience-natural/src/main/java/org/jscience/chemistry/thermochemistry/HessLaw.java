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

package org.jscience.chemistry.thermochemistry;

import org.jscience.measure.Quantities;
import org.jscience.measure.Quantity;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Energy;

import java.util.Map;

/**
 * Thermochemistry calculations using Hess's Law.
 * <p>
 * Based on: G.H. Hess, "Thermochemische Untersuchungen", Annalen der Physik
 * und Chemie, Vol. 50, pp. 385-404, 1840.
 * </p>
 * <p>
 * Hess's Law states that the total enthalpy change of a reaction is
 * independent of the pathway taken (path independence of state functions).
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class HessLaw {

    private HessLaw() {
    }

    /**
     * Calculates the enthalpy of reaction using Hess's Law.
     * ÃŽâ€H_rxn = ÃŽÂ£(ÃŽâ€Hf products) - ÃŽÂ£(ÃŽâ€Hf reactants)
     * 
     * @param productEnthalpies  Map of product formula to (coefficient, ÃŽâ€Hf in
     *                           kJ/mol)
     * @param reactantEnthalpies Map of reactant formula to (coefficient, ÃŽâ€Hf in
     *                           kJ/mol)
     * @return Enthalpy of reaction in kJ/mol
     */
    public static Quantity<Energy> calculateEnthalpyOfReaction(
            Map<String, double[]> productEnthalpies,
            Map<String, double[]> reactantEnthalpies) {

        double sumProducts = 0;
        for (double[] coeffAndEnthalpy : productEnthalpies.values()) {
            sumProducts += coeffAndEnthalpy[0] * coeffAndEnthalpy[1];
        }

        double sumReactants = 0;
        for (double[] coeffAndEnthalpy : reactantEnthalpies.values()) {
            sumReactants += coeffAndEnthalpy[0] * coeffAndEnthalpy[1];
        }

        double deltaH = sumProducts - sumReactants;
        return Quantities.create(deltaH * 1000, Units.JOULE); // kJ -> J
    }

    /**
     * Determines if reaction is exothermic or endothermic.
     */
    public static boolean isExothermic(Quantity<Energy> enthalpyOfReaction) {
        return enthalpyOfReaction.to(Units.JOULE).getValue().doubleValue() < 0;
    }
}



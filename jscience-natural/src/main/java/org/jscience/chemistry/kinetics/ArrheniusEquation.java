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

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantities;
import org.jscience.measure.Quantity;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Energy;
import org.jscience.measure.quantity.Temperature;

/**
 * Arrhenius Equation and temperature dependence of reaction rates.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ArrheniusEquation {

    public static final Real R = Real.of(8.314462618);

    /** Rate constant: k = A * exp(-Ea / RT) */
    public static Real calculateK(Real A, Real Ea, Real T) {
        return A.multiply(Ea.negate().divide(R.multiply(T)).exp());
    }

    /** Activation energy from two rate constants */
    public static Real calculateActivationEnergy(Real k1, Real k2, Real t1, Real t2) {
        return R.negate().multiply(k2.divide(k1).log())
                .divide(t2.inverse().subtract(t1.inverse()));
    }

    /** Rate constant using Quantity types */
    public static Real calculateK(Real A, Quantity<Energy> Ea, Quantity<Temperature> temperature) {
        Real eaJoules = Ea.to(Units.JOULE).getValue();
        Real tempKelvin = temperature.to(Units.KELVIN).getValue();
        return calculateK(A, eaJoules, tempKelvin);
    }

    /** Activation energy using Quantity types */
    public static Quantity<Energy> calculateActivationEnergy(
            Real k1, Real k2, Quantity<Temperature> t1, Quantity<Temperature> t2) {
        Real t1K = t1.to(Units.KELVIN).getValue();
        Real t2K = t2.to(Units.KELVIN).getValue();
        Real ea = calculateActivationEnergy(k1, k2, t1K, t2K);
        return Quantities.create(ea, Units.JOULE);
    }
}



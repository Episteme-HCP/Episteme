/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.physics.thermodynamics;

import org.jscience.mathematics.number.Real;

/**
 * Thermodynamics constants and laws.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Thermodynamics {

    /** Boltzmann constant in J/K */
    public static final Real k = Real.of(1.380649e-23);

    /** Avogadro constant in mol^-1 */
    public static final Real NA = Real.of(6.02214076e23);

    /** Ideal gas constant in J/(mol·K) */
    public static final Real R = k.multiply(NA);

    private Thermodynamics() {
    }

    /**
     * Calculates pressure of an ideal gas.
     * P = nRT / V
     * 
     * @param n moles of gas
     * @param T temperature in Kelvin
     * @param V volume in cubic meters
     * @return Pressure in Pascals
     */
    public static Real idealGasPressure(Real n, Real T, Real V) {
        if (V.isZero())
            throw new ArithmeticException("Volume cannot be zero");
        return n.multiply(R).multiply(T).divide(V);
    }

    /**
     * Calculates the Carnot efficiency.
     * η = 1 - Tc/Th
     * 
     * @param Tc Cold reservoir temperature (K)
     * @param Th Hot reservoir temperature (K)
     * @return Efficiency (0 to 1)
     */
    public static Real carnotEfficiency(Real Tc, Real Th) {
        if (Th.isZero())
            throw new ArithmeticException("Hot reservoir temperature cannot be zero");
        return Real.ONE.subtract(Tc.divide(Th));
    }
}



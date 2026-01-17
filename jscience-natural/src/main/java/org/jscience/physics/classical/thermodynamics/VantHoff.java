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

package org.jscience.physics.classical.thermodynamics;

/**
 * Van't Hoff equation for temperature dependence of K.
 *
 * @author Silvere Martin-Michiellot
 * <p>
 * <b>Reference:</b><br>
 * van 't Hoff, J. H. (1884). <i>Études de Dynamique Chimique</i>.
 * </p>
 *
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class VantHoff {

    /**
     * Calculates K at T2 given K at T1 and ÃŽâ€HÃ‚Â°.
     * ln(K2/K1) = (-ÃŽâ€HÃ‚Â°/R) * (1/T2 - 1/T1)
     * 
     * @param k1     Equilibrium constant at T1
     * @param t1     Initial temperature (K)
     * @param t2     Final temperature (K)
     * @param deltaH Enthalpy of reaction (J/mol), assumed constant
     * @return Equilibrium constant at T2
     */
    public static double calculateK2(double k1, double t1, double t2, double deltaH) {
        double R = 8.314462618;
        double exponent = (-deltaH / R) * (1.0 / t2 - 1.0 / t1);
        return k1 * Math.exp(exponent);
    }
}



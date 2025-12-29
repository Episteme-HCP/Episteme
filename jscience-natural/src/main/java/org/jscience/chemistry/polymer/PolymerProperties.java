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

package org.jscience.chemistry.polymer;

/**
 * Physical properties of polymers.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PolymerProperties {

    /**
     * Mark-Houwink Equation.
     * Intrinsic viscosity [eta] = K * M^a
     * 
     * @param K Constant dependent on polymer/solvent/temp
     * @param M Molecular weight
     * @param a Scalar exponent (usually 0.5 - 0.8)
     * @return Intrinsic viscosity
     */
    public static double calculateIntrinsicViscosity(double K, double M, double a) {
        return K * Math.pow(M, a);
    }

    /**
     * Flory-Fox equation for Glass Transition Temperature (Tg).
     * Tg = Tg_inf - K / Mn
     * 
     * @param Tg_inf Tg of infinite molecular weight polymer
     * @param K      Constant
     * @param Mn     Number average molecular weight
     * @return Tg for the given molecular weight
     */
    public static double calculateTg(double Tg_inf, double K, double Mn) {
        return Tg_inf - (K / Mn);
    }
}

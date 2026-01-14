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

package org.jscience.chemistry.kinetics;

/**
 * Models for catalysis kinetics.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CatalysiModels {

    /**
     * Michaelis-Menten Kinetics.
     * v = (Vmax * [S]) / (Km + [S])
     * 
     * @param vmax Maximum rate
     * @param km   Michaelis constant
     * @param s    Substrate concentration
     * @return Reaction rate v
     */
    public static double michaelisMenten(double vmax, double km, double s) {
        return (vmax * s) / (km + s);
    }

    /**
     * Lineweaver-Burk plot transformation.
     * 1/v = (Km/Vmax) * (1/[S]) + 1/Vmax
     * 
     * @return Slope (Km/Vmax)
     */
    public static double lineweaverBurkSlope(double vmax, double km) {
        return km / vmax;
    }

    /**
     * Langmuir Adsorption Isotherm (Surface Catalysis).
     * theta = (K * P) / (1 + K * P)
     * 
     * @param K Adsorption equilibrium constant
     * @param P Partial pressure of gas
     * @return Fractional coverage theta
     */
    public static double langmuirAdsorption(double K, double P) {
        return (K * P) / (1.0 + K * P);
    }
}



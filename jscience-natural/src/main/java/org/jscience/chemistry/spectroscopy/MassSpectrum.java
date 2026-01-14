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

package org.jscience.chemistry.spectroscopy;

/**
 * Mass Spectrometry models.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MassSpectrum {

    /**
     * Calculates Mass-to-Charge ratio (m/z).
     * 
     * @param mass   Mass in Daltons (u)
     * @param charge Charge (integer, e.g. +1, +2)
     * @return m/z ratio
     */
    public static double calculateMZ(double mass, int charge) {
        if (charge == 0)
            throw new IllegalArgumentException("Charge cannot be zero");
        // Usually mass of electron is subtracted for positive ions formed by electron
        // loss
        // M+ = (M_neutral - m_e) / 1
        return mass / (double) Math.abs(charge);
    }

    /**
     * Radius of curvature in magnetic field.
     * r = (1/B) * sqrt(2Vm/q)
     * or for velocity selector:
     * qvB = mv^2/r -> r = mv/qB
     */
    public static double cycloidalRadius(double massKg, double velocity, double chargeC, double magneticFieldTesla) {
        return (massKg * velocity) / (chargeC * magneticFieldTesla);
    }
}



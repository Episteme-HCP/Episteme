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

package org.jscience.natural.chemistry.computational.quantum;

import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Represents a Contracted Gaussian Type Orbital (CGTO) basis function.
 * Currently supports S-type orbitals.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class BasisFunction {
    
    private final Vector<Real> center;
    private final double[] exponents;
    private final double[] coefficients;
    private final double[] normalization;

    public BasisFunction(Vector<Real> center, double[] exponents, double[] coefficients) {
        this.center = center;
        this.exponents = exponents;
        this.coefficients = coefficients;
        this.normalization = new double[exponents.length];
        calculateNormalization();
    }

    private void calculateNormalization() {
        // Normalization constant for S-type Gaussian: (2a/pi)^(3/4)
        for (int i = 0; i < exponents.length; i++) {
            normalization[i] = Math.pow(2.0 * exponents[i] / Math.PI, 0.75);
        }
    }

    public Vector<Real> getCenter() {
        return center;
    }

    public double[] getExponents() {
        return exponents;
    }

    public double[] getCoefficients() {
        return coefficients;
    }
    
    public double[] getNormalization() {
        return normalization;
    }
}

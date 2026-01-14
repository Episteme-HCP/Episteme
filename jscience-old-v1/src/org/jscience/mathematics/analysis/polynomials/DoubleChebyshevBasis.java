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

package org.jscience.mathematics.analysis.polynomials;

/**
 * The famous Chebychev basis for interpolating polynomials with minimal
 * variation
 *
 * @author b.dietrich
 */

//I wonder if this class is still useful along with ChebychevPolynomial
public class DoubleChebyshevBasis extends DoubleLagrangeBasis
    implements PolynomialBasis {
/**
     * Creates a new instance of ChebychevBase for a given degree
     *
     * @param dim Dimension (= degree)
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public DoubleChebyshevBasis(int dim) {
        super();

        if (dim <= 0) {
            throw new IllegalArgumentException();
        }

        super._dim = dim;
        _samplingsX = new double[_dim];

        int n = _dim - 1;

        for (int k = 0; k < _dim; k++) {
            _samplingsX[k] = Math.cos(((double) ((2 * k) + 1) * Math.PI) / (double) ((2 * n) +
                    2));
        }

        buildBasis();
    }
}

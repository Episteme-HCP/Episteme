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

import org.jscience.mathematics.algebraic.fields.Field;


/**
 * DOCUMENT ME!
 *
 * @author b.dietrich
 */
public class DoubleMonomialBasis implements PolynomialBasis {
    /** DOCUMENT ME! */
    private DoublePolynomial[] _basis;

    /** DOCUMENT ME! */
    private int _dim;

/**
     * Creates a new instance of RealMonomialBasis
     *
     * @param dim DOCUMENT ME!
     */
    public DoubleMonomialBasis(int dim) {
        _dim = dim;
        _basis = new DoublePolynomial[dim];
    }

    /**
     * DOCUMENT ME!
     *
     * @param k
     *
     * @return a basis vector
     *
     * @throws ArrayIndexOutOfBoundsException DOCUMENT ME!
     */
    public Polynomial getBasisVector(int k) {
        if (k >= _dim) {
            throw new ArrayIndexOutOfBoundsException();
        } else {
            if (_basis[k] == null) {
                synchronized (this) {
                    if (_basis[k] == null) {
                        double[] db = new double[_dim];
                        db[k] = 1.0;
                        _basis[k] = new DoublePolynomial(db);
                    }
                }
            }

            return _basis[k];
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return the dimension of this basis
     */
    public int dimension() {
        return _dim;
    }

    /**
     * 
     *
     * @return DOCUMENT ME!
     *
     * @throws UnsupportedOperationException DOCUMENT ME!
     */
    public Field.Member[] getSamplingPoints() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff
     *
     * @return DOCUMENT ME!
     */
    public Polynomial superposition(Field.Member[] coeff) {
        return superposition(DoublePolynomialRing.toDouble(coeff));
    }

    /**
     * DOCUMENT ME!
     *
     * @param d
     *
     * @return DOCUMENT ME!
     *
     * @throws NullPointerException DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public DoublePolynomial superposition(double[] d) {
        if (d == null) {
            throw new NullPointerException();
        }

        if (d.length != _dim) {
            throw new IllegalArgumentException("Dimension of basis is " + _dim +
                ". Got " + d.length + " coefficients");
        }

        return new DoublePolynomial(d);
    }
}

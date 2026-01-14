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
import org.jscience.mathematics.algebraic.numbers.Complex;


/**
 * A Lagrange base for polynomial over a complex field. For more detail
 *
 * @author b.dietrich
 *
 * @see org.jscience.mathematics.analysis.polynomials.DoubleLagrangeBasis
 */
public class ComplexLagrangeBasis implements PolynomialBasis {
    /** DOCUMENT ME! */
    private ComplexPolynomial[] _basis;

    /** DOCUMENT ME! */
    private Complex[] _samplingsX;

    /** DOCUMENT ME! */
    private int _dim;

/**
     * Creates a new instance of LagrangeBasis
     *
     * @param samplings DOCUMENT ME!
     * @throws NullPointerException DOCUMENT ME!
     */
    public ComplexLagrangeBasis(Field.Member[] samplings) {
        if (samplings == null) {
            throw new NullPointerException();
        }

        _dim = samplings.length;
        _samplingsX = ComplexPolynomialRing.toComplex(samplings);
        buildBasis();
    }

    /**
     * DOCUMENT ME!
     *
     * @param k
     *
     * @return DOCUMENT ME!
     */
    public Polynomial getBasisVector(int k) {
        return _basis[k];
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
     */
    public Field.Member[] getSamplingPoints() {
        return _samplingsX;
    }

    /**
     * DOCUMENT ME!
     *
     * @param coeff
     *
     * @return DOCUMENT ME!
     *
     * @throws NullPointerException DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Polynomial superposition(Field.Member[] coeff) {
        if (coeff == null) {
            throw new NullPointerException();
        }

        if (coeff.length != _dim) {
            throw new IllegalArgumentException("Dimensions do not match");
        }

        Complex[] d = ComplexPolynomialRing.toComplex(coeff);

        return superposition(d);
    }

    /**
     * DOCUMENT ME!
     *
     * @param c
     *
     * @return DOCUMENT ME!
     *
     * @throws NullPointerException DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public ComplexPolynomial superposition(Complex[] c) {
        if (c == null) {
            throw new NullPointerException();
        }

        if (c.length != _dim) {
            throw new IllegalArgumentException("Dimension of basis is " + _dim +
                ". Got " + c.length + " coefficients");
        }

        ComplexPolynomial rp = (ComplexPolynomial) ComplexPolynomialRing.getInstance()
                                                                        .zero();

        for (int k = 0; k < _dim; k++) {
            ComplexPolynomial b = (ComplexPolynomial) getBasisVector(k);
            ComplexPolynomial ba = b.scalarMultiply(c[k]);
            rp = (ComplexPolynomial) rp.add(ba);
        }

        return rp;
    }

    /**
     * DOCUMENT ME!
     */
    private void buildBasis() {
        _basis = new ComplexPolynomial[_dim];

        for (int k = 0; k < _dim; k++) {
            _basis[k] = (ComplexPolynomial) ComplexPolynomialRing.getInstance()
                                                                 .one();

            Complex fac = Complex.ONE;

            for (int j = 0; j < _dim; j++) {
                if (j == k) {
                    continue;
                } else {
                    ComplexPolynomial n = new ComplexPolynomial(new Complex[] {
                                (Complex) _samplingsX[j].negate(), Complex.ONE
                            });
                    _basis[k] = (ComplexPolynomial) _basis[k].multiply(n);

                    Complex a = _samplingsX[k];
                    Complex b = _samplingsX[j];
                    Complex dif = a.subtract(b);
                    fac = fac.multiply(dif);
                }
            }

            _basis[k] = _basis[k].scalarDivide(fac);
        }
    }
}

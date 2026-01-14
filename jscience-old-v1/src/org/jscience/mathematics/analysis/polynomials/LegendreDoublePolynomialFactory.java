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
import org.jscience.mathematics.algebraic.numbers.Double;

/**
 * This class implements Legendre polynomials.
 * <p/>
 * <p>Legendre polynomials can be defined by the following recurrence
 * relations:
 * <pre>
 *        P0(X)   = 1
 *        P1(X)   = X
 *  (k+1) Pk+1(X) = (2k+1) X Pk(X) - k Pk-1(X)
 * </pre></p>
 *
 * @author L. Maisonobe
 * @version $Id: LegendreDoublePolynomialFactory.java,v 1.2 2007-10-21 17:45:48 virtualcall Exp $
 */

public final class LegendreDoublePolynomialFactory extends OrthogonalDoublePolynomialFactory {

    public LegendreDoublePolynomialFactory() {
    }

    /**
     * Simple constructor.
     * Build a degree d Chebyshev polynomial
     *
     * @param degree degree of the polynomial
     */
    public DoublePolynomial getOrthogonalPolynomial(int degree) {
        return new DoublePolynomial(getOrthogonalPolynomialCoefficients(degree));
    }

    /**
     * Initialize the recurrence coefficients for degree 0 and 1.
     *
     * @return an array which contains the coefficients for degree 0 and 1
     */
    public Polynomial[] getFirstTermsPolynomials() {

        Polynomial[] result;
        org.jscience.mathematics.algebraic.numbers.Double[] coefficients;

        result = new Polynomial[2];
        // P0(X)  = 1
        coefficients = new Double[1];
        coefficients[0] = new Double(1);
        result[0] = new DoublePolynomial(coefficients);

        // P1(X)  = X
        coefficients = new Double[2];
        coefficients[0] = new Double(0);
        coefficients[1] = new Double(1);
        result[1] = new DoublePolynomial(coefficients);

        return result;

    }

    /**
     * Initialize the recurrence coefficients.
     * The recurrence relation is
     * (k+1) Pk+1(X) = (2k+1) X Pk(X) - k Pk-1(X)
     *
     * @param k index of the current step
     */
    public Field.Member[] getRecurrenceCoefficients(int k) {

        Field.Member[] result;

        result = new Double[3];
        result[0] = new Double(0);
        result[1] = new Double((2 * k + 1) / (k + 1));
        result[2] = new Double(k / (k + 1));

        return result;

    }

}

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
 * This class implements Gegenbauer polynomials.
 * <p/>
 * <p>Gegenbauer l (lambda) polynomials can be defined by the following recurrence
 * relations:
 * <pre>
 *  T0(X)   = 1
 *  T1(X)   = 2lX
 *  (k+1)Tk+1(X) = 2(k+l)X Tk(X) - (k+2l-1)Tk-1(X)
 * </pre></p>
 *
 * @author Silvere Martin-Michiellot
 */

public final class GegenbauerDoublePolynomialFactory extends OrthogonalDoublePolynomialFactory {

    private double lambda;

    public GegenbauerDoublePolynomialFactory(double lambda) {
        this.lambda = lambda;
    }

    public double getLambda() {
        return lambda;
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
        // T0(X)  = 1
        coefficients = new org.jscience.mathematics.algebraic.numbers.Double[1];
        coefficients[0] = new Double(1);
        result[0] = new DoublePolynomial(coefficients);

        // T1(X)  = 2lX
        coefficients = new Double[2];
        coefficients[0] = new Double(0);
        coefficients[1] = new Double(2 * lambda);
        result[1] = new DoublePolynomial(coefficients);

        return result;

    }

    /**
     * Initialize the recurrence coefficients.
     * The recurrence relation is
     * (k+1)Tk+1(X) = 2(k+l)X Tk(X) - (k+2l-1)Tk-1(X)
     *
     * @param k index of the current step
     */
    public Field.Member[] getRecurrenceCoefficients(int k) {

        Field.Member[] result;

        result = new Double[3];
        result[0] = new Double(0);
        result[1] = new Double(2);
        result[2] = new Double((k + 2 * lambda - 1) / (k + 1));

        return result;

    }

}

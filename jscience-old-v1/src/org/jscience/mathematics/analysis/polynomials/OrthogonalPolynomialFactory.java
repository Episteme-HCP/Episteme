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
 * This class is the base class for orthogonal polynomials.
 * <p/>
 * <p>Orthogonal polynomials can be defined by recurrence relations like:
 * <pre>
 *      O0(X)   = some 0 degree polynomial
 *      O1(X)   = some first degree polynomial
 *  a1k Ok+1(X) = (a2k + a3k X) Ok(X) - a4k Ok-1(X)
 * </pre>
 * where a0k, a1k, a2k and a3k are simple expressions which either are
 * constants or depend on k.</p>
 *
 * @author Silvere Martin-Michiellot
 */

//see http://mathworld.wolfram.com/OrthogonalPolynomials.html
public interface OrthogonalPolynomialFactory {
    /**
     * DOCUMENT ME!
     *
     * @param degree DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Polynomial getOrthogonalPolynomial(int degree);

    /**
     * Initialize the recurrence coefficients for degree 0 and 1.
     *
     * @return an array which contains the coefficients for degree 0 and 1
     */
    public Polynomial[] getFirstTermsPolynomials();

    /**
     * Initialize the recurrence coefficients. The recurrence relation
     * is<pre>a1k Ok+1(X) = (a2k + a3k X) Ok(X) - a4k Ok-1(X)</pre>
     *
     * @param k index of the current step
     *
     * @return a double array of 3 elements: b2k = double[0] coefficient to
     *         initialize (b2k = a2k / a1k) b3k = double[1] coefficient to
     *         initialize (b3k = a3k / a1k) b4k = double[2] coefficient to
     *         initialize (b4k = a4k / a1k)
     */
    public Field.Member[] getRecurrenceCoefficients(int k);
}

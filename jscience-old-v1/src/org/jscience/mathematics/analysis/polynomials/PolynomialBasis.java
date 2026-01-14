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
 * The vector space basis for polynomials
 *
 * @author b.dietrich
 */
public interface PolynomialBasis {
    /**
     * Get the <I>k</I>th basis vector
     *
     * @param k for the <I>k</I>th basis vector
     *
     * @return DOCUMENT ME!
     */
    public Polynomial getBasisVector(int k);

    /**
     * The dimension of the vector space.
     *
     * @return the dimension
     */
    public int dimension();

    /**
     * If available, get the one- points for the polynomials
     *
     * @return the one-points
     */
    public Field.Member[] getSamplingPoints();

    /**
     * Get a superposition of basis vectors
     *
     * @param coeff coefficients for the superposition
     *
     * @return a superposition
     */
    public Polynomial superposition(Field.Member[] coeff);
}

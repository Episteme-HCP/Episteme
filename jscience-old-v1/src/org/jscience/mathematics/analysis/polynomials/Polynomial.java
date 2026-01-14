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
import org.jscience.mathematics.algebraic.fields.Ring;


/**
 * A Polynomial as a <code>Ring.Member</code> over a <code>Field</code>
 *
 * @author b.dietrich
 */
public interface Polynomial extends Ring.Member {
    /**
     * Get the coefficient of degree k, i.e. <I>a_k</I> if <I>P(x)</I>
     * := sum_{k=0}^n <I>a_k x^k</I>
     *
     * @param k degree
     *
     * @return coefficient as described above
     */
    public Field.Member getCoefficient(int k);

    /**
     * Get the coefficients as an array
     *
     * @return the coefficients as an array
     */
    public Field.Member[] getCoefficients();

    /**
     * The degree understood as the highest degree
     *
     * @return the degree
     */
    public int degree();

    /**
     * Return a new Polynomial with coefficients divided by <I>a</I>
     *
     * @param a divisor
     *
     * @return new Polynomial with coefficients /= <I>a</I>
     */
    public Polynomial scalarDivide(Field.Member a);

    /**
     * Return a new Polynomial with coefficients multiplied by <I>a</I>
     *
     * @param a factor
     *
     * @return new Polynomial with coefficients = <I>a</I>
     */
    public Polynomial scalarMultiply(Field.Member a);
}

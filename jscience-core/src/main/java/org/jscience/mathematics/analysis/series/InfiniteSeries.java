/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.mathematics.analysis.series;

/**
 * Represents an infinite series.
 * <p>
 * An infinite series is the sum of an infinite sequence of terms:
 * S = Σ a(k), k=0..∞
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface InfiniteSeries<T> {

    /**
     * Computes the partial sum of the first n terms.
     * <p>
     * S_n = Σ a(k), k=0..n
     * </p>
     * 
     * @param n the number of terms to sum (n ≥ 0)
     * @return the partial sum S_n
     */
    T partialSum(int n);

    /**
     * Determines if this series is convergent.
     * <p>
     * A series converges if lim(n→∞) S_n exists and is finite.
     * </p>
     * 
     * @return true if convergent, false if divergent
     */
    boolean isConvergent();

    /**
     * Returns the limit of the series if convergent.
     * <p>
     * L = lim(n→∞) S_n
     * </p>
     * 
     * @return the limit L
     * @throws ArithmeticException if the series is divergent
     */
    T limit();

    /**
     * Returns the k-th term of this series.
     * 
     * @param k the term index (k ≥ 0)
     * @return the term a(k)
     */
    T term(int k);
}
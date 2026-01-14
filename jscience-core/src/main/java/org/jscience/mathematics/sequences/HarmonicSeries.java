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

package org.jscience.mathematics.sequences;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents the Harmonic series: 1 + 1/2 + 1/3 + 1/4 + ...
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class HarmonicSeries implements InfiniteSequence<Real> {

    @Override
    public Real get(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Index must be non-negative");
        }
        // H_n = sum_{k=1}^{n+1} 1/k (using 0-based index for sequence terms)
        // Let's define get(n) as the n-th harmonic number H_n where H_0 = 0, H_1 = 1,
        // H_2 = 1 + 1/2
        // Actually, usually H_n is the sum of first n reciprocals.
        // Let's align with 0-based index: get(n) = sum_{k=1}^{n} 1/k.
        // If index=0, sum is empty -> 0? Or index=1 -> 1?
        // Let's say get(n) returns the sum of the first n terms?
        // Sequence usually means a_0, a_1, a_2...
        // If this is a "Series", it usually represents the partial sums.
        // Let's define get(n) as the n-th partial sum.

        Real sum = Real.ZERO;
        for (int k = 1; k <= index; k++) {
            sum = sum.add(Real.ONE.divide(Real.of(k)));
        }
        return sum;
    }

    @Override
    public boolean isConvergent() {
        return false;
    }

    @Override
    public Real limit() {
        throw new ArithmeticException("Harmonic series diverges to infinity");
    }
}



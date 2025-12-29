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

import org.jscience.mathematics.numbers.integers.Integer;
import org.jscience.mathematics.numbers.integers.Natural;

/**
 * A sequence of integers (ℤ).
 * <p>
 * Most OEIS sequences are integer sequences. This interface uses JScience
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface IntegerSequence extends Sequence<Integer> {

    /**
     * Convenience method for small indices that fit in a long.
     * <p>
     * Use this when you know the sequence value will fit in a {@code long}.
     * For arbitrary-precision results, use {@link #get(Natural)} or
     * {@link #get(int)}.
     * </p>
     * 
     * @param n the index
     * @return a(n) as long
     * @throws ArithmeticException if the value doesn't fit in a long
     */
    default long getLong(int n) {
        return get(n).longValue();
    }

    /**
     * Convenience method to get value as long using Natural index.
     * 
     * @param n the index
     * @return a(n) as long
     * @throws ArithmeticException if the value doesn't fit in a long
     */
    default long getLong(Natural n) {
        return get(n).longValue();
    }
}
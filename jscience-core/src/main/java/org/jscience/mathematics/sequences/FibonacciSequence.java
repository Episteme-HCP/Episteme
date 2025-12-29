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

package org.jscience.mathematics.sequences;

import org.jscience.mathematics.numbers.integers.Integer;

/**
 * Represents the Fibonacci sequence: 0, 1, 1, 2, 3, 5, 8, ...
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FibonacciSequence implements InfiniteSequence<Integer> {

    @Override
    public Integer get(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Index must be non-negative");
        }
        if (index == 0)
            return Integer.ZERO;
        if (index == 1)
            return Integer.ONE;

        Integer a = Integer.ZERO;
        Integer b = Integer.ONE;
        for (int i = 2; i <= index; i++) {
            Integer temp = a.add(b);
            a = b;
            b = temp;
        }
        return b;
    }

    @Override
    public boolean isConvergent() {
        return false;
    }

    @Override
    public Integer limit() {
        throw new ArithmeticException("Fibonacci sequence diverges to infinity");
    }
}

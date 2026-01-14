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

package org.jscience.mathematics.analysis.series;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a sequence defined by a recurrence relation.
 * <p>
 * Supports memoization to efficiently calculate terms.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public abstract class RecursiveSequence<T> implements Sequence<T> {

    private final Map<Integer, T> cache = new HashMap<>();

    /**
     * Calculates the n-th term using the recurrence relation.
     * Implementations should call {@link #get(int)} for previous terms
     * to utilize caching.
     * 
     * @param n the index
     * @return the n-th term
     */
    protected abstract T calculate(int n);

    @Override
    public T get(int n) {
        if (n < 0)
            throw new IllegalArgumentException("Index must be non-negative");

        if (cache.containsKey(n)) {
            return cache.get(n);
        }

        // Check for base cases defined by subclass?
        // Usually calculate() handles base cases or calls get() which handles cache.
        // To prevent infinite recursion if calculate calls get(n), we assume calculate
        // handles logic.

        // Stack overflow protection could be added here for deep recursions,
        // or iterative computation if possible.

        T value = calculate(n);
        cache.put(n, value);
        return value;
    }

    /**
     * Clears the memoization cache.
     */
    public void clearCache() {
        cache.clear();
    }
}


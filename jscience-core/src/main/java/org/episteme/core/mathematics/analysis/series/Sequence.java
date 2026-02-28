/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.core.mathematics.analysis.series;

import org.episteme.core.mathematics.analysis.Function;
import org.episteme.core.mathematics.numbers.integers.Natural;

/**
 * A mathematical sequence a(n) for n Ã¢â€°Â¥ 0.
 * <p>
 * A sequence is a function from natural numbers to type T: Ã¢â€žâ€¢ Ã¢â€ â€™ T.
 * Compatible with OEIS (Online Encyclopedia of Integer Sequences) format.
 * </p>
 * <p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Sequence<T> extends Function<Natural, T> {

    /**
     * Returns the n-th term of the sequence (0-indexed).
     * <p>
     * This is the canonical method using Episteme number types.
     * </p>
     * 
     * @param n the index (n Ã¢â€°Â¥ 0)
     * @return a(n)
     * @throws IllegalArgumentException if n is negative
     */
    T get(Natural n);

    /**
     * Function interface implementation - delegates to get(Natural).
     * 
     * @param n the index
     * @return a(n)
     */
    @Override
    default T evaluate(Natural n) {
        return get(n);
    }

    /**
     * Convenience method for primitive int indices.
     * <p>
     * Delegates to {@link #get(Natural)} after converting to Natural.
     * </p>
     * 
     * @param n the index (must be Ã¢â€°Â¥ 0)
     * @return a(n)
     * @throws IllegalArgumentException if n < 0
     */
    default T get(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Sequence index must be non-negative: " + n);
        }
        return get(Natural.of(n));
    }

    /**
     * Function interface implementation - delegates to get(Natural).
     * 
     * @param n the index
     * @return a(n)
     */
    @Override
    default T apply(Natural n) {
        return get(n);
    }

    @Override
    default String getDomain() {
        return "Ã¢â€žâ€¢ (Natural numbers)";
    }

    /**
     * Returns the OEIS (Online Encyclopedia of Integer Sequences) identifier.
     * For example, "A000045" for Fibonacci numbers.
     * 
     * @return OEIS ID or null if not catalogued
     */
    default String getOEISId() {
        return null;
    }

    /**
     * Returns a human-readable name for this sequence.
     * 
     * @return sequence name
     */
    default String getName() {
        return this.getClass().getSimpleName();
    }

    /**
     * Returns a formula or description of how terms are computed.
     * 
     * @return mathematical formula or description
     */
    default String getFormula() {
        return "Not specified";
    }
}



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

/**
 * Interface for mathematical series that support composition.
 * <p>
 * Enables functional composition f(g(x)) of series, as well as
 * standard arithmetic operations.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface ComposableSeries<T> {

    /**
     * Composes this series with another: f(g(x)).
     * 
     * @param inner the inner series g(x)
     * @return the composed series f(g(x))
     */
    ComposableSeries<T> compose(ComposableSeries<T> inner);

    /**
     * Adds two series: (f + g)(x) = f(x) + g(x).
     * 
     * @param other the series to add
     * @return the sum series
     */
    ComposableSeries<T> add(ComposableSeries<T> other);

    /**
     * Multiplies two series: (f * g)(x) = f(x) * g(x).
     * 
     * @param other the series to multiply
     * @return the product series
     */
    ComposableSeries<T> multiply(ComposableSeries<T> other);

    /**
     * Scales the series by a constant: (c * f)(x) = c * f(x).
     * 
     * @param scalar the scaling factor
     * @return the scaled series
     */
    ComposableSeries<T> scale(T scalar);

    /**
     * Evaluates the series at a given point.
     * 
     * @param x the evaluation point
     * @return the series value at x
     */
    T evaluate(T x);

    /**
     * Returns the degree or order of the series.
     * 
     * @return the series degree
     */
    int degree();
}


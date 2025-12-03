/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.mathematics.function;

/**
 * Base interface for all mathematical functions.
 * <p>
 * A function maps elements from a domain D to a range R.
 * </p>
 * 
 * @param <D> the domain type
 * @param <R> the range type
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@FunctionalInterface
public interface Function<D, R> {

    /**
     * Evaluates the function at the given point.
     * 
     * @param x the input value
     * @return the output value
     */
    R evaluate(D x);

    /**
     * Composes this function with another function.
     * 
     * @param <T>   the new range type
     * @param after the function to apply after this one
     * @return the composed function
     */
    default <T> Function<D, T> andThen(Function<? super R, ? extends T> after) {
        return x -> after.evaluate(evaluate(x));
    }

    /**
     * Composes this function with a function before it.
     * 
     * @param <T>    the new domain type
     * @param before the function to apply before this one
     * @return the composed function
     */
    default <T> Function<T, R> compose(Function<? super T, ? extends D> before) {
        return x -> evaluate(before.evaluate(x));
    }
}

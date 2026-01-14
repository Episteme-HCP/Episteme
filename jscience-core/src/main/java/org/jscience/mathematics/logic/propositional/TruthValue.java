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

package org.jscience.mathematics.logic.propositional;

/**
 * Represents a truth value in a logical system.
 * <p>
 * In classical logic, this is just True or False.
 * In fuzzy logic, this is a value between 0 and 1.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface TruthValue<T> extends Comparable<TruthValue<T>> {

    /**
     * Returns the underlying value.
     * 
     * @return the value
     */
    T getValue();

    /**
     * Returns the negation of this truth value.
     * 
     * @return NOT this
     */
    TruthValue<T> not();

    /**
     * Returns the conjunction of this truth value and another.
     * 
     * @param other the other truth value
     * @return this AND other
     */
    TruthValue<T> and(TruthValue<T> other);

    /**
     * Returns the disjunction of this truth value and another.
     * 
     * @param other the other truth value
     * @return this OR other
     */
    TruthValue<T> or(TruthValue<T> other);

    /**
     * Checks if this value represents absolute truth (tautology).
     * 
     * @return true if this is the "True" value
     */
    boolean isTrue();

    /**
     * Checks if this value represents absolute falsity (contradiction).
     * 
     * @return true if this is the "False" value
     */
    boolean isFalse();
}



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

package org.jscience.mathematics.logic;

/**
 * Represents a logical system.
 * <p>
 * A logical system defines the set of truth values and the operations on them.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Logic<T> {

    /**
     * Returns the truth value representing "True" (tautology).
     * 
     * @return the true value
     */
    T trueValue();

    /**
     * Returns the truth value representing "False" (contradiction).
     * 
     * @return the false value
     */
    T falseValue();

    /**
     * Logical AND (Conjunction).
     * 
     * @param a first operand
     * @param b second operand
     * @return a AND b
     */
    T and(T a, T b);

    /**
     * Logical OR (Disjunction).
     * 
     * @param a first operand
     * @param b second operand
     * @return a OR b
     */
    T or(T a, T b);

    /**
     * Logical NOT (Negation).
     * 
     * @param a operand
     * @return NOT a
     */
    T not(T a);

    /**
     * Logical Implication (a -> b).
     * 
     * @param a antecedent
     * @param b consequent
     * @return a IMPLIES b
     */
    default T implies(T a, T b) {
        return or(not(a), b);
    }

    /**
     * Logical Equivalence (a <-> b).
     * 
     * @param a first operand
     * @param b second operand
     * @return a IFF b
     */
    default T equivalent(T a, T b) {
        return and(implies(a, b), implies(b, a));
    }
}



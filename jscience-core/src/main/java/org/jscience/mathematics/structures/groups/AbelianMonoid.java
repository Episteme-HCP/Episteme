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

package org.jscience.mathematics.structures.groups;

/**
 * An abelian monoid is a commutative monoid.
 * <p>
 * It satisfies all monoid axioms plus commutativity: a Ã¢Ë†â€” b = b Ã¢Ë†â€” a.
 * </p>
 * <p>
 * <strong>Convention</strong>: Abelian monoids often use additive notation (+,
 * 0).
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface AbelianMonoid<E> extends Monoid<E> {

    /**
     * Returns the sum of two elements (additive notation).
     * <p>
     * Delegates to {@link #operate(Object, Object)}.
     * </p>
     * 
     * @param a the first addend
     * @param b the second addend
     * @return a + b
     */
    default E add(E a, E b) {
        return operate(a, b);
    }

    /**
     * Returns the additive identity (zero element).
     * <p>
     * Delegates to {@link #identity()}.
     * </p>
     * 
     * @return the zero element
     */
    default E zero() {
        return identity();
    }

    /**
     * Abelian monoids are commutative by definition.
     * 
     * @return always {@code true}
     */
    @Override
    default boolean isCommutative() {
        return true;
    }

    /**
     * Default implementation of identity that delegates to zero().
     * Implementations can override zero() to provide the identity element.
     * 
     * @return the identity element (same as zero())
     */
    @Override
    default E identity() {
        return zero();
    }
}


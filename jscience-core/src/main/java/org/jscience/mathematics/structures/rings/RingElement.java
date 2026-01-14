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

package org.jscience.mathematics.structures.rings;

/**
 * Marker interface for elements of a ring structure.
 * <p>
 * A ring element supports addition, multiplication, and negation operations
 * as instance methods. This interface uses F-bounded polymorphism (self-typing)
 * to ensure type safety: operations return the same type as the implementing
 * class.
 * </p>
 *
 * <h2>Ring Axioms</h2>
 * <p>
 * Elements must satisfy:
 * </p>
 * <ul>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface RingElement<E extends RingElement<E>> {

    /**
     * Returns the sum of this element and another.
     * 
     * @param other the element to add
     * @return this + other
     */
    E add(E other);

    /**
     * Returns the product of this element and another.
     * 
     * @param other the element to multiply
     * @return this * other
     */
    E multiply(E other);

    /**
     * Returns the additive inverse of this element.
     * 
     * @return -this
     */
    E negate();

    /**
     * Returns the difference of this element and another.
     * <p>
     * Default implementation: {@code this.add(other.negate())}
     * </p>
     * 
     * @param other the element to subtract
     * @return this - other
     */
    default E subtract(E other) {
        return add(other.negate());
    }

    /**
     * Returns the additive identity element (zero).
     * <p>
     * For any element e: e.add(e.zero()) = e
     * </p>
     * 
     * @return the zero element
     */
    E zero();

    /**
     * Returns the multiplicative identity element (one).
     * <p>
     * For any element e: e.multiply(e.one()) = e
     * </p>
     * 
     * @return the one element
     */
    E one();

    /**
     * Checks if this element is the additive identity (zero).
     * 
     * @return true if this equals zero()
     */
    default boolean isZero() {
        return this.equals(zero());
    }

    /**
     * Checks if this element is the multiplicative identity (one).
     * 
     * @return true if this equals one()
     */
    default boolean isOne() {
        return this.equals(one());
    }
}


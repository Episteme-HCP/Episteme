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

package org.jscience.mathematics.structures.rings;

/**
 * Marker interface for elements of a field structure.
 * <p>
 * A field element extends ring element with division (multiplicative inverse).
 * Every non-zero field element has a multiplicative inverse.
 * </p>
 *
 * <h2>Field Axioms</h2>
 * <p>
 * In addition to ring axioms:
 * </p>
 * <ul>
 * <li>Every non-zero element has a multiplicative inverse</li>
 * <li>(F \ {0}, *) is an abelian group</li>
 * </ul>
 *
 * <h2>Example Usage</h2>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface FieldElement<E extends FieldElement<E>> extends RingElement<E> {

    /**
     * Returns the multiplicative inverse of this element.
     * <p>
     * For any non-zero element e: e.multiply(e.inverse()) = e.one()
     * </p>
     * 
     * @return 1/this
     * @throws ArithmeticException if this element is zero
     */
    E inverse();

    /**
     * Returns this element divided by another.
     * <p>
     * Default implementation: {@code this.multiply(other.inverse())}
     * </p>
     * 
     * @param other the divisor
     * @return this / other
     * @throws ArithmeticException if other is zero
     */
    default E divide(E other) {
        return multiply(other.inverse());
    }
}
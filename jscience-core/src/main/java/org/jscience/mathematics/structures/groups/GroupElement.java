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
 * Marker interface for elements of a group structure.
 * <p>
 * A group element supports a binary operation and inverse.
 * </p>
 *
 * <h2>Group Axioms</h2>
 * <ul>
 * <li>Closure: a Ã¢Ë†Ëœ b is in G</li>
 * <li>Associativity: (a Ã¢Ë†Ëœ b) Ã¢Ë†Ëœ c = a Ã¢Ë†Ëœ (b Ã¢Ë†Ëœ c)</li>
 * <li>Identity: e Ã¢Ë†Ëœ a = a Ã¢Ë†Ëœ e = a</li>
 * <li>Inverse: a Ã¢Ë†Ëœ aÃ¢ÂÂ»Ã‚Â¹ = aÃ¢ÂÂ»Ã‚Â¹ Ã¢Ë†Ëœ a = e</li>
 * </ul>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface GroupElement<E extends GroupElement<E>> {

    /**
     * Applies the group operation with another element.
     * 
     * @param other the other element
     * @return this Ã¢Ë†Ëœ other
     */
    E operate(E other);

    /**
     * Returns the inverse of this element.
     * 
     * @return thisÃ¢ÂÂ»Ã‚Â¹
     */
    E inverse();

    /**
     * Returns the identity element of this group.
     * 
     * @return the identity element e
     */
    E identity();
}


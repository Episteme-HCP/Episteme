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

package org.jscience.mathematics.algebra.groups;

import org.jscience.mathematics.structures.groups.Group;

/**
 * Represents an Ordered Group.
 * <p>
 * An ordered group is a group (G, +) equipped with a total order &le; such that
 * for all a, b, c in G, if a &le; b then a + c &le; b + c and c + a &le; c + b.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface OrderedGroup<E> extends Group<E>, Comparable<E> {

    /**
     * Checks if the first element is strictly less than the second.
     * 
     * @param a the first element
     * @param b the second element
     * @return true if a < b
     */
    boolean isStrictlyLessThan(E a, E b);

    /**
     * Checks if the first element is less than or equal to the second.
     * 
     * @param a the first element
     * @param b the second element
     * @return true if a <= b
     */
    boolean isLessThanOrEqualTo(E a, E b);
}



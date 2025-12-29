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

package org.jscience.mathematics.structures.sets;

/**
 * Represents a mathematical set - a collection of distinct elements.
 * <p>
 * This is the foundational concept in mathematics. A set has no additional
 * structure
 * beyond membership. Higher-level structures (Magma, Group, Ring, Field) add
 * operations
 * and properties to sets.
 * </p>
 *
 * <h2>Mathematical Definition</h2>
 * <p>
 * A set S is a well-defined collection of distinct objects, considered as a
 * whole.
 * The objects are called elements or members of the set.
 * </p>
 *
 * <h2>Examples</h2>
 * <ul>
 * <li>ℕ = {0, 1, 2, 3, ...} - Natural numbers</li>
 * <li>ℤ = {..., -2, -1, 0, 1, 2, ...} - Integers</li>
 * <li>ℝ - Real numbers</li>
 * <li>ℂ - Complex numbers</li>
 * </ul>
 *
 * <h2>Usage in JScience</h2>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Set<E> {

    /**
     * Tests whether this set contains the specified element.
     * <p>
     * This is the fundamental operation of a set - membership testing.
     * </p>
     * 
     * @param element the element to test for membership
     * @return {@code true} if this set contains the element, {@code false}
     *         otherwise
     * @throws NullPointerException if the element is null and this set does not
     *                              permit null elements
     * 
     * @see #isEmpty()
     * @see #size()
     */
    boolean contains(E element);

    /**
     * Returns {@code true} if this set contains no elements.
     * <p>
     * The empty set (∅) is a fundamental concept in set theory.
     * It is the unique set containing no elements.
     * </p>
     * 
     * @return {@code true} if this set is empty
     */
    boolean isEmpty();

    /**
     * Returns a human-readable description of this set.
     * <p>
     * Examples:
     * <ul>
     * <li>"ℝ (Real Numbers)"</li>
     * <li>"ℤ/12ℤ (Integers modulo 12)"</li>
     * <li>"{1, 2, 3, 4, 5}"</li>
     * </ul>
     * </p>
     * 
     * @return a description of this set
     */
    String description();
}
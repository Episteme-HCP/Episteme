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

package org.jscience.mathematics.sets;

import org.jscience.mathematics.structures.rings.Ring;
import org.jscience.mathematics.structures.sets.InfiniteSet;
import org.jscience.mathematics.numbers.integers.Integer;

/**
 * The structure of integers (ℤ = {..., -2, -1, 0, 1, 2, ...}).
 * <p>
 * This class represents the <strong>structure</strong> of integers,
 * not individual elements. It implements {@link Ring} because ℤ forms
 * a commutative ring with unity under addition and multiplication.
 * </p>
 * <p>
 * Integers are an infinite, countable set.
 * </p>
 * 
 * <h2>Usage</h2>
 * 
 * <pre>{@code
 * Integers structure = Integers.getInstance();
 * Integer five = Integer.of(5);
 * Integer negThree = Integer.of(-3);
 * 
 * // Use structure for operations
 * Integer two = structure.add(five, negThree);
 * Integer negFifteen = structure.multiply(five, negThree);
 * Integer negFive = structure.negate(five);
 * }</pre>
 * 
 * @see Integer * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class Integers implements Ring<Integer>, InfiniteSet<Integer> {

    /** Singleton instance */
    private static final Integers INSTANCE = new Integers();

    /**
     * Returns the singleton instance.
     * 
     * @return the Integers structure
     */
    public static Integers getInstance() {
        return INSTANCE;
    }

    /** Private constructor for singleton */
    private Integers() {
    }

    // --- Ring Implementation ---

    @Override
    public Integer operate(Integer a, Integer b) {
        return add(a, b); // Ring operation is addition
    }

    @Override
    public Integer add(Integer a, Integer b) {
        return a.add(b);
    }

    @Override
    public Integer zero() {
        return Integer.ZERO;
    }

    @Override
    public Integer negate(Integer element) {
        return element.negate();
    }

    @Override
    public Integer multiply(Integer a, Integer b) {
        return a.multiply(b);
    }

    @Override
    public Integer one() {
        return Integer.ONE;
    }

    @Override
    public boolean isMultiplicationCommutative() {
        return true;
    }

    @Override
    public Integer inverse(Integer element) {
        // Integers don't have multiplicative inverses in general
        if (element.isOne() || element.equals(Integer.MINUS_ONE)) {
            return element; // Only ±1 are self-inverse
        }
        throw new UnsupportedOperationException(
                "Multiplicative inverse not defined for integers (use Rationals instead)");
    }

    // --- InfiniteSet Implementation ---

    @Override
    public boolean isCountable() {
        return true; // ℤ is countably infinite
    }

    @Override
    public boolean contains(Integer element) {
        return element != null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public String description() {
        return "ℤ (Integers)";
    }

    @Override
    public String toString() {
        return "Integers(ℤ)";
    }
}
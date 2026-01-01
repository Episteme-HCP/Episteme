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

package org.jscience.mathematics.algebra.groups;

import org.jscience.mathematics.structures.groups.Group;
import org.jscience.mathematics.numbers.integers.Integer;

/**
 * Represents a Cyclic Group of order n (Z_n).
 * <p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CyclicGroup implements Group<Integer> {

    private final Integer order;

    /**
     * Creates a cyclic group of order n.
     * 
     * @param n the order of the group (must be > 0)
     */
    public CyclicGroup(long n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Order must be positive");
        }
        this.order = Integer.of(n);
    }

    /**
     * Returns the order of this group.
     * 
     * @return n
     */
    public Integer getOrder() {
        return order;
    }

    @Override
    public Integer operate(Integer a, Integer b) {
        // (a + b) % n
        return a.add(b).remainder(order);
    }

    @Override
    public Integer identity() {
        return Integer.ZERO;
    }

    @Override
    public Integer inverse(Integer element) {
        // -a % n = (n - a) % n
        if (element.equals(Integer.ZERO)) {
            return Integer.ZERO;
        }
        return order.subtract(element).remainder(order);
    }

    @Override
    public boolean isCommutative() {
        return true;
    }

    @Override
    public String description() {
        return "Cyclic Group of order " + order;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Integer element) {
        return element.compareTo(Integer.ZERO) >= 0 && element.compareTo(order) < 0;
    }

    @Override
    public String toString() {
        return "Z_" + order;
    }
}



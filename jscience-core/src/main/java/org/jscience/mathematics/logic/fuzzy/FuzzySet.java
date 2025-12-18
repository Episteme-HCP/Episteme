/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.mathematics.logic.fuzzy;

/**
 * Represents a fuzzy set.
 * 
 * @param <T> the type of elements in the universe of discourse
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class FuzzySet<T> {

    private final MembershipFunction<T> function;

    /**
     * Creates a new fuzzy set with the given membership function.
     * 
     * @param function the membership function
     */
    public FuzzySet(MembershipFunction<T> function) {
        this.function = function;
    }

    /**
     * Returns the degree of membership of an element in this set.
     * 
     * @param element the element
     * @return membership degree [0, 1]
     */
    public double membership(T element) {
        double val = function.apply(element);
        if (val < 0.0)
            return 0.0;
        if (val > 1.0)
            return 1.0;
        return val;
    }

    /**
     * Returns the complement of this fuzzy set.
     * 
     * @return the complement set
     */
    public FuzzySet<T> complement() {
        return new FuzzySet<>(e -> 1.0 - this.membership(e));
    }

    /**
     * Returns the union of this set and another (max).
     * 
     * @param other the other set
     * @return the union set
     */
    public FuzzySet<T> union(FuzzySet<T> other) {
        return new FuzzySet<>(e -> Math.max(this.membership(e), other.membership(e)));
    }

    /**
     * Returns the intersection of this set and another (min).
     * 
     * @param other the other set
     * @return the intersection set
     */
    public FuzzySet<T> intersection(FuzzySet<T> other) {
        return new FuzzySet<>(e -> Math.min(this.membership(e), other.membership(e)));
    }
}


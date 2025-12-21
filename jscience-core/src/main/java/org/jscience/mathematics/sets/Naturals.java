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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.jscience.mathematics.sets;

import org.jscience.mathematics.structures.rings.Semiring;
import org.jscience.mathematics.structures.sets.InfiniteSet;
import org.jscience.mathematics.numbers.integers.Natural;
import java.util.stream.Stream;

/**
 * The structure of natural numbers (ℕ = {0, 1, 2, 3, ...}).
 * <p>
 * This class represents the <strong>structure</strong> of natural numbers,
 * not individual elements. It implements {@link Semiring} because ℕ forms
 * a commutative semiring with addition and multiplication, but lacks
 * additive inverses (so it's not a Ring).
 * </p>
 * <p>
 * Natural numbers are an infinite, countable set.
 * </p>
 * 
 * <h2>Usage</h2>
 * 
 * <pre>{@code
 * Naturals structure = Naturals.getInstance();
 * Natural five = Natural.of(5);
 * Natural three = Natural.of(3);
 * 
 * // Use structure for operations
 * Natural eight = structure.add(five, three);
 * Natural fifteen = structure.multiply(five, three);
 * 
 * // Query structure properties
 * Natural zero = structure.zero(); // 0
 * Natural one = structure.one(); // 1
 * boolean contains = structure.contains(five); // true
 * }</pre>
 * 
 * @see Natural * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class Naturals implements Semiring<Natural>, InfiniteSet<Natural> {

    /** Singleton instance */
    private static final Naturals INSTANCE = new Naturals();

    /**
     * Returns the singleton instance.
     * 
     * @return the Naturals structure
     */
    public static Naturals getInstance() {
        return INSTANCE;
    }

    /** Private constructor for singleton */
    private Naturals() {
    }

    // --- Semiring Implementation ---

    @Override
    public Natural operate(Natural a, Natural b) {
        return add(a, b); // Monoid operation is addition
    }

    @Override
    public Natural add(Natural a, Natural b) {
        return Natural.of(a.bigIntegerValue().add(b.bigIntegerValue()));
    }

    @Override
    public Natural zero() {
        return Natural.ZERO;
    }

    @Override
    public Natural multiply(Natural a, Natural b) {
        return Natural.of(a.bigIntegerValue().multiply(b.bigIntegerValue()));
    }

    @Override
    public Natural one() {
        return Natural.ONE;
    }

    @Override
    public boolean isMultiplicationCommutative() {
        return true;
    }

    // --- InfiniteSet Implementation ---

    @Override
    public boolean isCountable() {
        return true; // ℕ is countably infinite
    }

    @Override
    public boolean contains(Natural element) {
        return element != null; // All Natural instances are valid
    }

    @Override
    public boolean isEmpty() {
        return false; // ℕ is infinite
    }

    @Override
    public String description() {
        return "ℕ (Natural Numbers)";
    }

    // --- Additional Methods ---

    /**
     * Returns a stream of the first n natural numbers.
     * <p>
     * Since ℕ is infinite, we provide a way to get a finite stream.
     * </p>
     * 
     * @param count number of elements
     * @return stream of 0, 1, 2, ..., count-1
     */
    public Stream<Natural> stream(int count) {
        return Stream.iterate(Natural.ZERO, n -> add(n, Natural.ONE))
                .limit(count);
    }

    @Override
    public String toString() {
        return "Naturals(ℕ)";
    }
}
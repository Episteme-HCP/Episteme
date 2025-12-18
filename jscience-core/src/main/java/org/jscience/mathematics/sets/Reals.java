/*
 * JScience Reimagined - Unified Scientific Computing Framework
 * Copyright (c) 2025 Silvere Martin-Michiellot
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
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

import org.jscience.mathematics.structures.rings.Field;
import org.jscience.mathematics.structures.sets.InfiniteSet;
import org.jscience.mathematics.numbers.real.Real;

/**
 * The structure of real numbers (ℝ).
 * <p>
 * This class represents the <strong>structure</strong> of real numbers,
 * not individual elements. It implements {@link Field} because ℝ forms
 * a field under addition and multiplication.
 * </p>
 * <p>
 * Real numbers are an infinite, uncountable set.
 * </p>
 * 
 * <h2>Usage</h2>
 * 
 * <pre>{@code
 * Reals structure = Reals.getInstance();
 * Real pi = Real.of(3.14159);
 * Real two = Real.of(2.0);
 * 
 * // Use structure for operations
 * Real twoPi = structure.multiply(pi, two);
 * Real halfPi = structure.divide(pi, two);
 * }</pre>
 * 
 * @see Real
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class Reals implements Field<Real>, InfiniteSet<Real> {

    /** Singleton instance */
    public static final Reals INSTANCE = new Reals();

    /**
     * Returns the singleton instance.
     * 
     * @return the Reals structure
     */
    public static Reals getInstance() {
        return INSTANCE;
    }

    /** Private constructor for singleton */
    private Reals() {
    }

    // --- Field Implementation ---

    @Override
    public Real operate(Real a, Real b) {
        return add(a, b); // Field operation is addition
    }

    @Override
    public Real add(Real a, Real b) {
        return a.add(b);
    }

    @Override
    public int characteristic() {
        return 0;
    }

    @Override
    public Real zero() {
        return Real.ZERO;
    }

    @Override
    public Real negate(Real element) {
        return element.negate();
    }

    @Override
    public Real multiply(Real a, Real b) {
        return a.multiply(b);
    }

    @Override
    public Real one() {
        return Real.ONE;
    }

    @Override
    public boolean isMultiplicationCommutative() {
        return true;
    }

    @Override
    public Real inverse(Real element) {
        return element.inverse();
    }

    // --- InfiniteSet Implementation ---

    @Override
    public boolean isCountable() {
        return false; // ℝ is uncountable
    }

    @Override
    public boolean contains(Real element) {
        return element != null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public String description() {
        return "ℝ (Real Numbers)";
    }

    @Override
    public String toString() {
        return "Reals(ℝ)";
    }
}

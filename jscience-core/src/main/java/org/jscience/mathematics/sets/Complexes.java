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

import org.jscience.mathematics.structures.rings.Field;
import org.jscience.mathematics.structures.sets.InfiniteSet;
import org.jscience.mathematics.numbers.complex.Complex;

/**
 * The structure of complex numbers (ℂ).
 * <p>
 * This class represents the <strong>structure</strong> of complex numbers,
 * implementing {@link Field} because ℂ forms a field under addition and
 * multiplication.
 * </p>
 * <p>
 * Complex numbers are an infinite, uncountable set.
 * </p>
 * 
 * @see Complex * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class Complexes implements Field<Complex>, InfiniteSet<Complex> {

    /** Singleton instance */
    private static final Complexes INSTANCE = new Complexes();

    /**
     * Returns the singleton instance.
     * 
     * @return the Complexes structure
     */
    public static Complexes getInstance() {
        return INSTANCE;
    }

    /** Private constructor for singleton */
    private Complexes() {
    }

    // --- Field Implementation ---

    @Override
    public Complex operate(Complex a, Complex b) {
        return add(a, b);
    }

    @Override
    public Complex add(Complex a, Complex b) {
        return a.add(b);
    }

    @Override
    public Complex zero() {
        return Complex.ZERO;
    }

    @Override
    public Complex negate(Complex element) {
        return element.negate();
    }

    @Override
    public Complex multiply(Complex a, Complex b) {
        return a.multiply(b);
    }

    @Override
    public Complex one() {
        return Complex.ONE;
    }

    @Override
    public boolean isMultiplicationCommutative() {
        return true;
    }

    @Override
    public Complex inverse(Complex element) {
        return element.inverse();
    }

    @Override
    public Complex divide(Complex a, Complex b) {
        return a.divide(b);
    }

    @Override
    public int characteristic() {
        return 0;
    }

    // --- InfiniteSet Implementation ---

    @Override
    public boolean isCountable() {
        return false; // ℂ is uncountable
    }

    @Override
    public boolean contains(Complex element) {
        return element != null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public String description() {
        return "ℂ (Complex Numbers)";
    }

    @Override
    public String toString() {
        return "Complexes(ℂ)";
    }
}

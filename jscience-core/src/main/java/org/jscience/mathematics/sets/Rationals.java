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
import org.jscience.mathematics.numbers.rationals.Rational;

/**
 * The structure of rational numbers (Ã¢â€žÅ¡).
 * <p>
 * This class represents the <strong>structure</strong> of rational numbers,
 * implementing {@link Field} because Ã¢â€žÅ¡ forms a field under addition and
 * multiplication.
 * </p>
 * <p>
 * Rational numbers are an infinite, countable set.
 * </p>
 * 
 * @see Rational * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class Rationals implements Field<Rational>, InfiniteSet<Rational> {

    /** Singleton instance */
    private static final Rationals INSTANCE = new Rationals();

    /**
     * Returns the singleton instance.
     * 
     * @return the Rationals structure
     */
    public static Rationals getInstance() {
        return INSTANCE;
    }

    /** Private constructor for singleton */
    private Rationals() {
    }

    // --- Field Implementation ---

    @Override
    public Rational operate(Rational a, Rational b) {
        return add(a, b);
    }

    @Override
    public Rational add(Rational a, Rational b) {
        return a.add(b);
    }

    @Override
    public Rational zero() {
        return Rational.ZERO;
    }

    @Override
    public Rational negate(Rational element) {
        return element.negate();
    }

    @Override
    public Rational multiply(Rational a, Rational b) {
        return a.multiply(b);
    }

    @Override
    public Rational one() {
        return Rational.ONE;
    }

    @Override
    public boolean isMultiplicationCommutative() {
        return true;
    }

    @Override
    public Rational inverse(Rational element) {
        return element.inverse();
    }

    @Override
    public Rational divide(Rational a, Rational b) {
        return a.divide(b);
    }

    @Override
    public int characteristic() {
        return 0;
    }

    // --- InfiniteSet Implementation ---

    @Override
    public boolean isCountable() {
        return true; // Ã¢â€žÅ¡ is countable
    }

    @Override
    public boolean contains(Rational element) {
        return element != null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public String description() {
        return "Ã¢â€žÅ¡ (Rational Numbers)";
    }

    @Override
    public String toString() {
        return "Rationals(Ã¢â€žÅ¡)";
    }
}



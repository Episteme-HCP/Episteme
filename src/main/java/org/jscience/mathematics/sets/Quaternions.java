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
package org.jscience.mathematics.sets;

import org.jscience.mathematics.algebra.DivisionRing;
import org.jscience.mathematics.algebra.InfiniteSet;
import org.jscience.mathematics.number.Quaternion;

/**
 * The structure of quaternions (ℍ).
 * <p>
 * This class represents the <strong>structure</strong> of quaternions.
 * It implements {@link DivisionRing} because ℍ forms a non-commutative
 * division ring (skew field).
 * </p>
 * 
 * @see Quaternion
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class Quaternions implements DivisionRing<Quaternion>, InfiniteSet<Quaternion> {

    /** Singleton instance */
    private static final Quaternions INSTANCE = new Quaternions();

    /**
     * Returns the singleton instance.
     * 
     * @return the Quaternions structure
     */
    public static Quaternions getInstance() {
        return INSTANCE;
    }

    /** Private constructor for singleton */
    private Quaternions() {
    }

    // --- DivisionRing Implementation ---

    @Override
    public Quaternion operate(Quaternion a, Quaternion b) {
        return a.multiply(b);
    }

    @Override
    public Quaternion add(Quaternion a, Quaternion b) {
        return a.add(b);
    }

    @Override
    public Quaternion zero() {
        return Quaternion.ZERO;
    }

    @Override
    public Quaternion negate(Quaternion element) {
        return element.negate();
    }

    @Override
    public Quaternion multiply(Quaternion a, Quaternion b) {
        return a.multiply(b);
    }

    @Override
    public Quaternion one() {
        return Quaternion.ONE;
    }

    @Override
    public boolean isMultiplicationCommutative() {
        return false; // Quaternions are non-commutative
    }

    @Override
    public Quaternion inverse(Quaternion element) {
        return element.inverse();
    }

    @Override
    public Quaternion divide(Quaternion dividend, Quaternion divisor) {
        // Note: This is right division (dividend * divisor^-1)
        return dividend.multiply(divisor.inverse());
    }

    // --- InfiniteSet Implementation ---

    @Override
    public boolean isCountable() {
        return false; // ℍ is uncountable (like ℝ^4)
    }

    @Override
    public boolean contains(Quaternion element) {
        return element != null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public String description() {
        return "ℍ (Quaternions)";
    }

    @Override
    public String toString() {
        return "Quaternions(ℍ)";
    }
}



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
import org.jscience.mathematics.numbers.complex.Complex;

/**
 * Represents the Unitary Group U(1).
 * <p>
 * The group of all complex numbers with absolute value 1 under multiplication.
 * Isomorphic to the circle group (rotations in 2D).
 * Important in electromagnetism (gauge group).
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class U1Group implements Group<Complex> {

    private static final U1Group INSTANCE = new U1Group();

    public static U1Group getInstance() {
        return INSTANCE;
    }

    private U1Group() {
    }

    @Override
    public Complex operate(Complex left, Complex right) {
        return left.multiply(right);
    }

    @Override
    public Complex identity() {
        return Complex.ONE;
    }

    @Override
    public Complex inverse(Complex element) {
        // For U(1), inverse is conjugate (since |z|=1, z^-1 = z_bar)
        return element.conjugate();
    }

    @Override
    public boolean isCommutative() {
        return true;
    }

    @Override
    public String description() {
        return "U(1) - Circle Group";
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Complex element) {
        // Check if |z| = 1
        // Allow small epsilon for floating point arithmetic
        return Math.abs(element.abs().doubleValue() - 1.0) < 1e-10;
    }

    /**
     * Creates a U(1) element from an angle (phase).
     * 
     * @param theta the angle in radians
     * @return e^(i*theta)
     */
    public Complex fromPhase(double theta) {
        return Complex.of(Math.cos(theta), Math.sin(theta));
    }
}



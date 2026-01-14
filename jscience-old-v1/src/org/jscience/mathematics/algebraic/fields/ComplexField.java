/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.mathematics.algebraic.fields;

import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.numbers.Complex;


/**
 * The ComplexField class encapsulates the field of complex numbers.
 *
 * @author Mark Hale
 * @version 1.0
 */
public final class ComplexField extends Object implements Field {
    /** DOCUMENT ME! */
    public static final Complex ZERO = new Complex(0.0, 0.0);

    /** DOCUMENT ME! */
    public static final Complex I = new Complex(0.0, 1.0);

    /** DOCUMENT ME! */
    public static final Complex ONE = new Complex(1.0, 0.0);

    /** DOCUMENT ME! */
    public static final Complex MINUS_ONE = new Complex(-1.0, 0.0);

    /** DOCUMENT ME! */
    public static final Complex MINUS_I = new Complex(0.0, -1.0);

    /** DOCUMENT ME! */
    public static final Complex HALF = new Complex(0.5, 0.0);

    /** DOCUMENT ME! */
    public static final Complex MINUS_HALF = new Complex(-0.5, 0.0);

    /** DOCUMENT ME! */
    public static final Complex HALF_I = new Complex(0.0, 0.5);

    /** DOCUMENT ME! */
    public static final Complex MINUS_HALF_I = new Complex(0.0, -0.5);

    /** DOCUMENT ME! */
    public static final Complex TWO = new Complex(2.0, 0.0);

    /** DOCUMENT ME! */
    public static final Complex MINUS_TWO = new Complex(-2.0, 0.0);

    /** DOCUMENT ME! */
    public static final Complex SQRT_HALF = new Complex(Math.sqrt(0.5), 0.0);

    /** DOCUMENT ME! */
    public static final Complex SQRT_HALF_I = new Complex(0.0, Math.sqrt(0.5));

    /** DOCUMENT ME! */
    public static final Complex MINUS_SQRT_HALF_I = new Complex(0.0,
            -Math.sqrt(0.5));

    /**
     * This is the value of PI rounded to that same number of digits as
     * given by Math.PI
     */
    public static final Complex PI = new Complex(Math.PI, 0.0);

    /**
     * This is the value of PI as imaginary rounded to that same number
     * of digits as given by Math.PI
     */
    public static final Complex PI_I = new Complex(0.0, Math.PI);

    /**
     * This is the value of PI/2 rounded to that same number of digits
     * as given by Math.PI/2
     */
    public static final Complex PI_2 = new Complex(Math.PI / 2.0, 0.0);

    /**
     * This is the value of -PI/2 rounded to that same number of digits
     * as given by -Math.PI/2
     */
    public static final Complex MINUS_PI_2 = new Complex(-Math.PI / 2.0, 0.0);

    /**
     * This is the value of PI/2 as imaginary rounded to that same
     * number of digits as given by Math.PI/2
     */
    public static final Complex PI_2_I = new Complex(0.0, Math.PI / 2.0);

    /**
     * This is the value of -PI/2 as imaginary rounded to that same
     * number of digits as given by -Math.PI/2
     */
    public static final Complex MINUS_PI_2_I = new Complex(0.0, -Math.PI / 2.0);

    //we could also provide INFINITY and NaN
    /** DOCUMENT ME! */
    private static ComplexField _instance;

/**
     * Constructs a field of complex numbers.
     */
    private ComplexField() {
    }

    /**
     * Constructs a field of complex numbers. Singleton.
     *
     * @return DOCUMENT ME!
     */
    public static final ComplexField getInstance() {
        if (_instance == null) {
            synchronized (ComplexField.class) {
                if (_instance == null) {
                    _instance = new ComplexField();
                }
            }
        }

        return _instance;
    }

    /**
     * Returns the complex number zero.
     *
     * @return DOCUMENT ME!
     */
    public AbelianGroup.Member zero() {
        return ZERO;
    }

    /**
     * Returns true if the complex number is equal to zero.
     *
     * @param g DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isZero(AbelianGroup.Member g) {
        return ZERO.equals(g);
    }

    /**
     * Returns true if one complex number is the negative of the other.
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isNegative(AbelianGroup.Member a, AbelianGroup.Member b) {
        return ZERO.equals(a.add(b));
    }

    /**
     * Returns the complex number one.
     *
     * @return DOCUMENT ME!
     */
    public Ring.Member one() {
        return ONE;
    }

    /**
     * Returns true if the complex number is equal to one.
     *
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isOne(Ring.Member r) {
        return ONE.equals(r);
    }

    /**
     * Returns true if one complex number is the inverse of the other.
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isInverse(Field.Member a, Field.Member b) {
        return ONE.equals(a.multiply(b));
    }
}

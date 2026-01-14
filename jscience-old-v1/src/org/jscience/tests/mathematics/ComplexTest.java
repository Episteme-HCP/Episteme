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

package org.jscience.tests.mathematics;

import junitx.extensions.*;


/**
 * Testcase for complex numbers.
 *
 * @author Mark Hale
 */
public class ComplexTest extends junitx.extensions.EqualsHashCodeTestCase {
    /** DOCUMENT ME! */
    private double x;

    /** DOCUMENT ME! */
    private double y;

    /** DOCUMENT ME! */
    private double u;

    /** DOCUMENT ME! */
    private double v;

/**
     * Creates a new ComplexTest object.
     *
     * @param name DOCUMENT ME!
     */
    public ComplexTest(String name) {
        super(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg DOCUMENT ME!
     */
    public static void main(String[] arg) {
        junit.textui.TestRunner.run(ComplexTest.class);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    protected void setUp() throws Exception {
        org.jscience.GlobalSettings.ZERO_TOL = 1.0e-10;
        x = Math.random();
        y = Math.random();
        u = Math.random();
        v = Math.random();
        super.setUp();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Object createInstance() {
        return new Complex(x, y);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Object createNotEqualInstance() {
        return new Complex(u, v);
    }

    /**
     * DOCUMENT ME!
     */
    public void testParseString() {
        Complex expected = (Complex) createInstance();
        Complex actual = new Complex(expected.toString());
        assertEquals(expected, actual);
        expected = new Complex(0.0, Double.MAX_VALUE);
        actual = new Complex(expected.toString());
        assertEquals(expected, actual);
        expected = new Complex(Double.MIN_VALUE, 0.0);
        actual = new Complex(expected.toString());
        assertEquals(expected, actual);
    }

    /**
     * DOCUMENT ME!
     */
    public void testSin() {
        Complex expected = (Complex) createInstance();
        Complex actual = Complex.sin(Complex.asin(expected));
        assertEquals(expected, actual);
    }

    /**
     * DOCUMENT ME!
     */
    public void testCos() {
        Complex expected = (Complex) createInstance();
        Complex actual = Complex.cos(Complex.acos(expected));
        assertEquals(expected, actual);
    }

    /**
     * DOCUMENT ME!
     */
    public void testTan() {
        Complex expected = (Complex) createInstance();
        Complex actual = Complex.tan(Complex.atan(expected));
        assertEquals(expected, actual);
    }

    /**
     * DOCUMENT ME!
     */
    public void testSinh() {
        Complex expected = (Complex) createInstance();
        Complex actual = Complex.sinh(Complex.asinh(expected));
        assertEquals(expected, actual);
    }

    /**
     * DOCUMENT ME!
     */
    public void testCosh() {
        Complex expected = (Complex) createInstance();
        Complex actual = Complex.cosh(Complex.acosh(expected));
        assertEquals(expected, actual);
    }

    /**
     * DOCUMENT ME!
     */
    public void testTanh() {
        Complex expected = (Complex) createInstance();
        Complex actual = Complex.tanh(Complex.atanh(expected));
        assertEquals(expected, actual);
    }
}

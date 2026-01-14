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

import org.jscience.mathematics.SpecialMath;


/**
 * Testcase for special function methods.
 *
 * @author Mark Hale
 */
public class SpecialTest extends junit.framework.TestCase {
/**
     * Creates a new SpecialTest object.
     *
     * @param name DOCUMENT ME!
     */
    public SpecialTest(String name) {
        super(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg DOCUMENT ME!
     */
    public static void main(String[] arg) {
        junit.textui.TestRunner.run(SpecialTest.class);
    }

    /**
     * DOCUMENT ME!
     *
     * @param expected DOCUMENT ME!
     * @param actual DOCUMENT ME!
     * @param fraction DOCUMENT ME!
     */
    public static void assertEquals(double expected, double actual,
        double fraction) {
        assertTrue("expected:<" + expected + "> but was:<" + actual + ">",
            Math.abs(1.0 - (actual / expected)) < fraction);
    }

    /**
     * Tests the gamma function against its functional equation.
     */
    public void testGammaFunctional() {
        for (int i = 0; i < 10; i++) {
            double x = SpecialMath.GAMMA_X_MAX_VALUE * Math.random();
            double expected = x * SpecialMath.gamma(x);
            double ans = SpecialMath.gamma(x + 1.0);
            assertEquals(expected, ans, 1.0e-10);
        }
    }

    /**
     * Tests the gamma function against the Legendre duplication
     * formula.
     */
    public void testGammaDuplication() {
        for (int i = 0; i < 10; i++) {
            double x = Math.sqrt(SpecialMath.GAMMA_X_MAX_VALUE) * Math.random();
            double expected = Math.pow(4.0, x) / (2.0 * Math.sqrt(Math.PI)) * SpecialMath.gamma(x) * SpecialMath.gamma(x +
                    0.5);
            double ans = SpecialMath.gamma(2.0 * x);
            assertEquals(expected, ans, 1.0e-10);
        }
    }
}

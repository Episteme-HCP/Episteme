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

import java.util.Random;


/**
 * Testcase for array methods.
 *
 * @author Mark Hale
 */
public class ArrayTest extends junit.framework.TestCase {
    /** DOCUMENT ME! */
    private final Random random = new Random();

/**
     * Creates a new ArrayTest object.
     *
     * @param name DOCUMENT ME!
     */
    public ArrayTest(String name) {
        super(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg DOCUMENT ME!
     */
    public static void main(String[] arg) {
        junit.textui.TestRunner.run(ArrayTest.class);
    }

    /**
     * DOCUMENT ME!
     */
    protected void setUp() {
        JSci.GlobalSettings.ZERO_TOL = 1.0e-6;
    }

    /**
     * DOCUMENT ME!
     */
    public void testMedian() {
        final int N = random.nextInt(31) + 1;
        double[] array = new double[N];
        array[0] = random.nextDouble();

        for (int i = 1; i < N; i++) {
            array[i] = random.nextDouble();

            if (array[i] < array[i - 1]) {
                array[i] += array[i - 1];
            }
        }

        double expected;

        if (isEven(N)) {
            expected = (array[(N / 2) - 1] + array[N / 2]) / 2.0;
        } else {
            expected = array[N / 2];
        }

        randomize(array);

        double ans = ArrayMath.median(array);
        assertEquals("[" + ArrayMath.toString(array) + "]", expected, ans, 0.0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static boolean isEven(int n) {
        return ((n % 2) == 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param array DOCUMENT ME!
     */
    private void randomize(double[] array) {
        for (int i = 0; i < array.length; i++) {
            int p = random.nextInt(array.length);
            int q = random.nextInt(array.length);
            double tmp = array[q];
            array[q] = array[p];
            array[p] = tmp;
        }
    }
}

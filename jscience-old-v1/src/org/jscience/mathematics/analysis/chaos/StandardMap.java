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

package org.jscience.mathematics.analysis.chaos;

import org.jscience.mathematics.MathConstants;
import org.jscience.mathematics.analysis.PrimitiveMappingND;


/**
 * The StandardMap class provides an object that encapsulates the Standard
 * map. x<sub>n+1</sub> = (x<sub>n</sub> + y<sub>n+1</sub>) mod 2<img border=0
 * alt="pi" src="../doc-files/pi.gif"> y<sub>n+1</sub> = (y<sub>n</sub> + k
 * sin(x<sub>n</sub>)) mod 2<img border=0 alt="pi" src="../doc-files/pi.gif">
 * (Chirikov, Taylor).
 *
 * @author Mark Hale
 * @version 1.0
 */
public final class StandardMap extends Object implements PrimitiveMappingND {
    /** DOCUMENT ME! */
    private final double k;

/**
     * Constructs a Standard map.
     *
     * @param kval the value of the k parameter
     */
    public StandardMap(double kval) {
        k = kval;
    }

    /**
     * Performs the mapping.
     *
     * @param x a 2-D double array
     *
     * @return a 2-D double array
     */
    public double[] map(double[] x) {
        double[] ans = new double[2];
        ans[1] = (x[1] + (k * Math.sin(x[0]))) % MathConstants.TWO_PI;
        ans[0] = (x[0] + ans[1]) % MathConstants.TWO_PI;

        return ans;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] map(float[] x) {
        double[] result;

        result = new double[x.length];

        for (int i = 0; i < x.length; i++) {
            result[i] = (double) x[i];
        }

        return map(result);
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] map(long[] x) {
        long[] result;

        result = new long[x.length];

        for (int i = 0; i < x.length; i++) {
            result[i] = (long) x[i];
        }

        return map(result);
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] map(int[] x) {
        int[] result;

        result = new int[x.length];

        for (int i = 0; i < x.length; i++) {
            result[i] = (int) x[i];
        }

        return map(result);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int numInputDimensions() {
        return 2;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int numOutputDimensions() {
        return 2;
    }

    /**
     * Iterates the map.
     *
     * @param n the number of iterations
     * @param x the initial values (2-D)
     *
     * @return a 2-D double array
     */
    public double[] iterate(int n, double[] x) {
        double[] xn = map(x);

        for (int i = 1; i < n; i++)
            xn = map(xn);

        return xn;
    }
}

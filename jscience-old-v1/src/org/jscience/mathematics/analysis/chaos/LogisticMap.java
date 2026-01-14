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

import org.jscience.mathematics.analysis.PrimitiveMapping;


/**
 * The LogisticMap class provides an object that encapsulates the logistic
 * map. x<sub>n+1</sub> = r x<sub>n</sub> (1-x<sub>n</sub>)
 *
 * @author Mark Hale
 * @version 1.0
 */
public final class LogisticMap extends Object implements PrimitiveMapping {
    /** 2-cycle bifurcation point. */
    public final static double R_2CYCLE = 3.0;

    /** 4-cycle bifurcation point. */
    public final static double R_4CYCLE = 1.0 + Math.sqrt(6.0);

    /** 8-cycle bifurcation point. */
    public final static double R_8CYCLE = 3.544090;

    /** 16-cycle bifurcation point. */
    public final static double R_16CYCLE = 3.564407;

    /** Accumulation point. */
    public final static double R_ACCUMULATION = 3.569945672;

    /** DOCUMENT ME! */
    private final double r;

/**
     * Constructs a logistic map.
     *
     * @param rval the value of the r parameter
     */
    public LogisticMap(double rval) {
        r = rval;
    }

    /**
     * Performs the mapping.
     *
     * @param x a double
     *
     * @return DOCUMENT ME!
     */
    public double map(double x) {
        return r * x * (1.0 - x);
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double map(float x) {
        return map((double) x);
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double map(long x) {
        return map((long) x);
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double map(int x) {
        return map((int) x);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double hausdorffDimension() {
        return 0.538;
    }

    /**
     * Iterates the map.
     *
     * @param n the number of iterations
     * @param x the initial value
     *
     * @return DOCUMENT ME!
     */
    public double iterate(int n, double x) {
        for (int i = 0; i < n; i++)
            x = map(x);

        return x;
    }
}

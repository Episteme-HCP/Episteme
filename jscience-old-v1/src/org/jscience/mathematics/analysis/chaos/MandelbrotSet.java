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

import org.jscience.mathematics.algebraic.numbers.Complex;


/**
 * The MandelbrotSet class provides an object that encapsulates the
 * Mandelbrot set.
 *
 * @author Mark Hale
 * @version 1.0
 */
public final class MandelbrotSet extends Object {
    /** DOCUMENT ME! */
    private final MandelbrotMap mbrot = new MandelbrotMap(Complex.ZERO);

/**
     * Constructs a Mandelbrot set.
     */
    public MandelbrotSet() {
    }

    /**
     * Returns 0 if z is a member of this set, else the number of
     * iterations it took for z to diverge to infinity.
     *
     * @param z DOCUMENT ME!
     * @param maxIter DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int isMember(final Complex z, final int maxIter) {
        mbrot.setConstant(z);

        Complex w = Complex.ZERO;

        for (int i = 0; i < maxIter; i++) {
            w = mbrot.map(w);

            if (w.mod() > MandelbrotMap.CONVERGENT_BOUND) {
                return i + 1;
            }
        }

        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param zRe DOCUMENT ME!
     * @param zIm DOCUMENT ME!
     * @param maxIter DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int isMember(final double zRe, final double zIm, final int maxIter) {
        double re = 0.0;
        double im = 0.0;
        double tmp;

        for (int i = 0; i < maxIter; i++) {
            tmp = (2.0 * re * im) + zIm;
            re = (re * re) - (im * im) + zRe;
            im = tmp;

            if (((re * re) + (im * im)) > (MandelbrotMap.CONVERGENT_BOUND * MandelbrotMap.CONVERGENT_BOUND)) {
                return i + 1;
            }
        }

        return 0;
    }
}

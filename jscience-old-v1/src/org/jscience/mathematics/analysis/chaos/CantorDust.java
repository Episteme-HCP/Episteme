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

/**
 * The CantorDust class provides an object that encapsulates the Cantor
 * dust fractal.
 *
 * @author Mark Hale
 * @version 0.1
 */
public abstract class CantorDust extends Object {
/**
     * Creates a new CantorDust object.
     */
    public CantorDust() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double hausdorffDimension() {
        return Math.log(2.0) / Math.log(3.0);
    }

    /**
     * The recursive algorithm for Cantor dust.
     *
     * @param start the start of the line.
     * @param end the end of the line.
     * @param n the number of recursions.
     */
    public void recurse(double start, double end, int n) {
        if (n == 0) {
            return;
        }

        final double l_3 = (end - start) / 3.0;
        eraseLine(start + l_3, end - l_3);
        recurse(start, start + l_3, n - 1);
        recurse(end - l_3, end, n - 1);
    }

    /**
     * Erases a line segment. This need not be an actual graphical
     * operation, but some corresponding abstract operation.
     *
     * @param start DOCUMENT ME!
     * @param end DOCUMENT ME!
     */
    protected abstract void eraseLine(double start, double end);
}

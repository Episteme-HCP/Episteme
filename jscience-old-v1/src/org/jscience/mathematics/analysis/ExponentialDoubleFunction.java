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

package org.jscience.mathematics.analysis;

/**
 * The exponential function.
 *
 * @author Mark Hale, Silvere Martin-Michiellot
 * @version 1.0
 */
public class ExponentialDoubleFunction extends DoubleFunction
    implements C2Function {
    /** DOCUMENT ME! */
    private final double A;

    /** DOCUMENT ME! */
    private final double w;

    /** DOCUMENT ME! */
    private final double k;

/**
     * Constructs an exponential function of the form <code>A exp(wx+k)</code>.
     *
     * @param A DOCUMENT ME!
     * @param w DOCUMENT ME!
     * @param k DOCUMENT ME!
     */
    public ExponentialDoubleFunction(double A, double w, double k) {
        this.A = A;
        this.w = w;
        this.k = k;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double map(double x) {
        return A * Math.exp((w * x) + k);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DoubleFunction differentiate() {
        return new ExponentialDoubleFunction(A * w, w, k);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DoubleFunction secondDerivative() {
        return differentiate().differentiate();
    }
}

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
import org.jscience.mathematics.analysis.ComplexMapping;


/**
 * The MandelbrotMap class provides an object that encapsulates the
 * Mandelbrot map. z<sub>n+1</sub> = z<sub>n</sub><sup>2</sup> + c.
 *
 * @author Mark Hale
 * @version 1.0
 */
public final class MandelbrotMap extends Object implements ComplexMapping {
    /**
     * A complex number z such that |z| &gt; CONVERGENT_BOUND will
     * diverge under this map.
     */
    public final static double CONVERGENT_BOUND = 2.0;

    /** DOCUMENT ME! */
    private Complex a;

/**
     * Constructs a Mandelbrot map.
     *
     * @param aval the value of the constant.
     */
    public MandelbrotMap(double aval) {
        a = new Complex(aval, 0.0);
    }

/**
     * Constructs a Mandelbrot map.
     *
     * @param aval the value of the constant.
     */
    public MandelbrotMap(Complex aval) {
        a = aval;
    }

    /**
     * Returns the constant.
     *
     * @return DOCUMENT ME!
     */
    public Complex getConstant() {
        return a;
    }

    /**
     * Sets the constant.
     *
     * @param aval DOCUMENT ME!
     */
    public void setConstant(Complex aval) {
        a = aval;
    }

    /**
     * Performs the mapping.
     *
     * @param x a double
     *
     * @return DOCUMENT ME!
     */
    public double map(double x) {
        return (x * x) + a.real();
    }

    /**
     * Performs the mapping.
     *
     * @param z a complex number.
     *
     * @return DOCUMENT ME!
     */
    public Complex map(Complex z) {
        return map(z.real(), z.imag());
    }

    /**
     * Performs the mapping.
     *
     * @param real DOCUMENT ME!
     * @param imag DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Complex map(double real, double imag) {
        return new Complex((real * real) - (imag * imag) + a.real(),
            (2.0 * real * imag) + a.imag());
    }

    //We support Double and Complex
    /**
     * DOCUMENT ME!
     *
     * @param z DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Number map(Number z) {
        if (z instanceof Complex) {
            return map((Complex) z);
        } else if (z instanceof Double) {
            return map(((Double) z).doubleValue());
        } else {
            throw new IllegalArgumentException(
                "Unsupported Number class for mapping.");
        }
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

    /**
     * Iterates the map.
     *
     * @param n the number of iterations
     * @param z the initial value
     *
     * @return DOCUMENT ME!
     */
    public Complex iterate(int n, Complex z) {
        for (int i = 0; i < n; i++)
            z = map(z);

        return z;
    }
}

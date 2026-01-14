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

package org.jscience.measure.random;

import org.jscience.mathematics.algebraic.numbers.Complex;
import org.jscience.mathematics.algebraic.numbers.Double;
import org.jscience.mathematics.analysis.ComplexMapping;
import org.jscience.mathematics.analysis.PrimitiveMapping;


/**
 * This class defines a random map.
 */
public final class RandomMap implements PrimitiveMapping, ComplexMapping {
    /** A random map that generates numbers between 0.0 and 1.0. */
    public final static RandomMap MAP = new RandomMap();

    /** DOCUMENT ME! */
    private final double min;

    /** DOCUMENT ME! */
    private final double max;

/**
     * Constructs a random map with the range [0.0,1.0].
     */
    public RandomMap() {
        this(0.0, 1.0);
    }

/**
     * Constructs a random map with a specified range.
     *
     * @param minimum smallest random number to generate
     * @param maximum largest random number to generate
     */
    public RandomMap(double minimum, double maximum) {
        min = minimum;
        max = maximum;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double map(int x) {
        return map(new Double(x).doubleValue());
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double map(long x) {
        return map(new Double(x).doubleValue());
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double map(float x) {
        return map(new Double(x).doubleValue());
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double map(double x) {
        return ((max - min) * Math.random()) + min;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Number map(Number x) {
        if (x instanceof Complex) {
            return map((Complex) x);
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param z DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Complex map(Complex z) {
        return new Complex(map(z.real()), map(z.imag()));
    }

    /**
     * DOCUMENT ME!
     *
     * @param real DOCUMENT ME!
     * @param imag DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Complex map(double real, double imag) {
        return new Complex(map(real), map(imag));
    }
}

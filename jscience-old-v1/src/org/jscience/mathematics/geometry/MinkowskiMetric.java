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

package org.jscience.mathematics.geometry;

import org.jscience.mathematics.algebraic.Vector;
import org.jscience.mathematics.algebraic.numbers.ComparableNumber;
import org.jscience.mathematics.algebraic.numbers.Double;


/**
 * This class provides an implementation for Metric, using the Minkowski
 * metric. Minkowski Metric, also known as the L_p norm. Special cases include
 * the Manhatten city-block distance with q=1 and the Euclidean distance with
 * q=2. The special case with q equal to positive infinity is supported.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//adapted from Mallet, under CreativeCommonsLicense
public final class MinkowskiMetric extends Object implements Metric {
    /** DOCUMENT ME! */
    private double q;

/**
     * Constructor for Minkowski metric.
     *
     * @param q Power of component wise absolute difference; must be at least 1
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public MinkowskiMetric(double q) {
        if (q < 1) {
            throw new IllegalArgumentException("Argument q must be at least 1.");
        }

        //assert( q>= 1 );
        this.q = q;
    }

    /*
    * Returns the parameter used in the constructor.
    * @return the q parameter used in the constructor
         */
    public double getQParameter() {
        return q;
    }

    /*  Gives the Minkowski distance between two vectors.
     *
     *  distance(x,y) := \left( \Sum_i=0^d-1 \left| x_i - y_i \right|^q \right)^\frac{1}{q}
     *
     *  for 1<=q<infinity. For q=infinity
     *
     *  distance(x,y) := max_i \left| x_i - y_i \right|
     */
    public ComparableNumber getDistance(Vector a, Vector b) {
        double dist = 0;
        double diff;

        if ((a == null) || (b == null)) {
            throw new IllegalArgumentException(
                "Distance from a null vector is undefined.");
        }

        //assert (a != null);
        //assert (b != null);
        if (a.getDimension() != b.getDimension()) {
            throw new IllegalArgumentException(
                "Vectors must be of the same dimension.");
        }

        //assert (a.getDimension() == b.getDimension() );
        for (int i = 0; i < a.getDimension(); i++) {
            diff = Math.abs(a.getElement(i).doubleValue() -
                    b.getElement(i).doubleValue());

            if (q == 1) {
                dist += diff;
            } else if (q == 2) {
                dist += (diff * diff);
            } else if (q == Double.POSITIVE_INFINITY) {
                if (diff > dist) {
                    dist = diff;
                } else {
                    dist += Math.pow(diff, q);
                }
            }
        }

        if ((q == 1) || (q == Double.POSITIVE_INFINITY)) {
            return new Double(dist);
        } else if (q == 2) {
            return new Double(Math.sqrt(dist));
        } else {
            return new Double(Math.pow(dist, 1 / q));
        }
    }
}

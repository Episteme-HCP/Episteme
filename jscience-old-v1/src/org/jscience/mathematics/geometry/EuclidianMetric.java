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
 * This class provides an implementation for Metric, with the traditional
 * meaning
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//We could treat this as a special case of Minkowski metric, thus removing this class
public final class EuclidianMetric extends Object implements Metric {
/**
     * Creates a new EuclidianMetric object.
     */
    private EuclidianMetric() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param v1 DOCUMENT ME!
     * @param v2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public ComparableNumber getDistance(Vector v1, Vector v2) {
        double dist = 0;
        double diff;

        if ((v1 == null) || (v2 == null)) {
            throw new IllegalArgumentException(
                "Distance from a null vector is undefined.");
        }

        //assert (a != null);
        //assert (b != null);
        if (v1.getDimension() != v2.getDimension()) {
            throw new IllegalArgumentException(
                "Vectors must be of the same dimension.");
        }

        //assert (a.getDimension() == b.getDimension() );
        for (int i = 0; i < v1.getDimension(); i++) {
            diff = Math.abs(v1.getElement(i).doubleValue() -
                    v2.getElement(i).doubleValue());
            dist += (diff * diff);
        }

        return new Double(Math.sqrt(dist));
    }
}

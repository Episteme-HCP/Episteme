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

package org.jscience.physics.nuclear.kinematics.math.statistics;

import org.jscience.physics.nuclear.kinematics.math.UncertainNumber;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class LinearFunction implements UncertainFunction {
    /** y=a(x-delx)+b */
    UncertainNumber a;

    /** y=a(x-delx)+b */
    UncertainNumber b;

    /**
     * delx is an offset introduced in x which diagonalizes the
     * covariance matrix of a and b.  It has no error of its own, by
     * assumption.
     */
    double delx;

    /**
     * Creates a new LinearFunction object.
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     * @param delx DOCUMENT ME!
     */
    public LinearFunction(UncertainNumber a, UncertainNumber b, double delx) {
        this.a = a;
        this.b = b;
        this.delx = delx;
    }

    /**
     * Creates a new LinearFunction object.
     *
     * @param a DOCUMENT ME!
     * @param siga DOCUMENT ME!
     * @param b DOCUMENT ME!
     * @param sigb DOCUMENT ME!
     * @param delx DOCUMENT ME!
     */
    public LinearFunction(double a, double siga, double b, double sigb,
        double delx) {
        this(new UncertainNumber(a, siga), new UncertainNumber(b, sigb), delx);
    }

    /**
     * Creates a new LinearFunction object.
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     * @param delx DOCUMENT ME!
     */
    public LinearFunction(double a, double b, double delx) {
        this(a, 0.0, b, 0.0, delx);
    }

    /**
     * Given an array of uncertain numbers, return back a function
     * value.
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws StatisticsException DOCUMENT ME!
     */
    public UncertainNumber evaluate(UncertainNumber[] x)
        throws StatisticsException {
        UncertainNumber xp;

        if (x.length > 1) {
            throw new StatisticsException("LinearFit: too many" +
                " arguments: " + x.length);
        }

        //Gives the appropriate number to use in the function.
        xp = new UncertainNumber(x[0].value - delx, x[0].error);

        return a.plus(b.times(xp));
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double valueAt(double x) {
        return a.value + (b.value * x);
    }
}

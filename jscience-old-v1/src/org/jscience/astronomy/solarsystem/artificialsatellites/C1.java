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

package org.jscience.astronomy.solarsystem.artificialsatellites;

/**
 * 
 */
class C1 {
    /**
     * DOCUMENT ME!
     */
    final static double E6A = 1.E-6;

    /**
     * DOCUMENT ME!
     */
    final static double TOTHRD = .66666667;

    /**
     * DOCUMENT ME!
     */
    final static double XJ2 = 1.082616E-3;

    /**
     * DOCUMENT ME!
     */
    final static double XJ4 = -1.65597E-6;

    /**
     * DOCUMENT ME!
     */
    final static double XJ3 = -.253881E-5;

    /**
     * DOCUMENT ME!
     */
    final static double XKE = .743669161E-1;

    /**
     * DOCUMENT ME!
     */
    final static double XKMPER = 6378.135;

    /**
     * DOCUMENT ME!
     */
    final static double XMNPDA = 1440.;

    /**
     * DOCUMENT ME!
     */
    final static double AE = 1.;

    /**
     * DOCUMENT ME!
     */
    final static double QO = 120.0;

    /**
     * DOCUMENT ME!
     */
    final static double SO = 78.0;

    /**
     * DOCUMENT ME!
     */
    final static double CK2 = .5 * XJ2 * Math.pow(AE, 2);

    /**
     * DOCUMENT ME!
     */
    final static double CK4 = -.375 * XJ4 * Math.pow(AE, 4);

    /**
     * DOCUMENT ME!
     */
    final static double QOMS2T = Math.pow((((QO - SO) * AE) / XKMPER), 4);

    /**
     * DOCUMENT ME!
     */
    final static double S = AE * (1. + (SO / XKMPER));

/**
     * Deny the ability to construct an instance of this class.
     */
    private C1() {
    }
}

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

package org.jscience.tests.astronomy.milkyway.hipparcos;

//this package is rebundled from Hipparcos Java package from
//William O'Mullane
//http://astro.estec.esa.nl/Hipparcos/hipparcos_java.html
//mailto:hipparcos@astro.estec.esa.nl

import org.jscience.astronomy.catalogs.hipparcos.HIPProperties;


/* Constants used in the tools */
public class Constants {
    /**
     * DOCUMENT ME!
     */
    public static final double PI = Math.PI;

    /**
     * DOCUMENT ME!
     */
    public static final double t0 = 1991.25;

    /**
     * DOCUMENT ME!
     */
    public static final double cPi = 2.0 * Math.atan(1);

    /**
     * DOCUMENT ME!
     */
    public static final double cPr = cPi / 180;

    /**
     * DOCUMENT ME!
     */
    public static final double degc = 57.29578;

    /**
     * DOCUMENT ME!
     */
    public static final int vlev = 2;

    /**
     * DOCUMENT ME!
     */
    public static final double EPS = 0.0000001;

    /**
     * DOCUMENT ME!
     */
    public static final double c = 0.105; // for Mag graphics

    /**
     * DOCUMENT ME!
     */
    public static int verbose = Integer.parseInt(HIPProperties.getProperty(
            "verbose", "1"));

    /**
     * DOCUMENT ME!
     */
    public static final float[] colBands = {0.3f, 0.4f, 0.7f, 1.1f}; // b-v bands
    /* this array holds the r,b,g values for the color bands
Note there is one more than the bands for greater than the last band */

    /**
     * DOCUMENT ME!
     */
    public static final float[][] colours = {
            {0.1f, 0.5f, 0.1f},
            {0.13f, 0.36f, 0.37f},
            {0.5f, 0.15f, 0.40f},
            {0.55f, 0.1f, 0.3f},
            {0.6f, 0.1f, 0.1f}
    };

    /**
     * DOCUMENT ME!
     */
    public static float maxBV = 5.46f;

    /**
     * DOCUMENT ME!
     */
    public static float minBV = -0.4f;
}

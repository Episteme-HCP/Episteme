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

package org.jscience.physics.waves;

import java.awt.*;


/**
 * The Star class provides a Color object given the wavelength of the
 * colour in nanometers.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
/**
 * instead of: Color c = new Color(255, 0, 0); use the freqency in
 * nanometers, and gamma 0.0. .. 1.0. Color c = ColorConverter.getColor(
 * 400.0f, 0.80f );
 */

//based on the code from Roedy Green
//http://mindprod.com
//mailto:roedyg@mindprod.com
//itself based on the code from Dan Bruton
//http://www.isc.tamu.edu/~astro/color.html
//mailto:astro@tamu.edu
public final class ColorConverter extends Object {
    /**
     * Creates a Color object given the wavelength instead of the usual
     * RGB or HSB values.
     *
     * @param wl wavelength of the light in NANOMETERS. Will show up black
     *        outside the range 380..780 nanometers.
     * @param gamma 0.0 .. 1.0 intensity.
     *
     * @return DOCUMENT ME!
     */
    public static Color getColor(double wl, double gamma) {
        /** red, green, blue component in range 0.0 .. 1.0. */
        double r = 0;
        double g = 0;
        double b = 0;

        /**
         * intensity 0.0 .. 1.0 based on drop off in vision at
         * low/high wavelengths
         */
        double s = 1;

        /**
         * We use different linear interpolations on different
         * bands. These numbers mark the upper bound of each band.
         */
        final float[] bands = {
                380, 420, 440, 490, 510, 580, 645, 700, 780, Float.MAX_VALUE
            };

        /**
         * Figure out which band we fall in.  A point on the edge
         * is considered part of the lower band.
         */
        int band = bands.length - 1;

        for (int i = 0; i < bands.length; i++) {
            if (wl <= bands[i]) {
                band = i;

                break;
            }
        }

        switch (band) {
        case 0:
            /* invisible below 380 */

            // The code is a little redundant for clarity.
            // A smart optimiser can remove any r=0, g=0, b=0.
            r = 0;
            g = 0;
            b = 0;
            s = 0;

            break;

        case 1:
            /* 380 .. 420, intensity drop off. */
            r = (440 - wl) / (440 - 380);
            g = 0;
            b = 1;
            s = .3f + ((.7f * (wl - 380)) / (420 - 380));

            break;

        case 2:
            /* 420 .. 440 */
            r = (440 - wl) / (440 - 380);
            g = 0;
            b = 1;

            break;

        case 3:
            /* 440 .. 490 */
            r = 0;
            g = (wl - 440) / (490 - 440);
            b = 1;

            break;

        case 4:
            /* 490 .. 510 */
            r = 0;
            g = 1;
            b = (510 - wl) / (510 - 490);

            break;

        case 5:
            /* 510 .. 580 */
            r = (wl - 510) / (580 - 510);
            g = 1;
            b = 0;

            break;

        case 6:
            /* 580 .. 645 */
            r = 1;
            g = (645 - wl) / (645 - 580);
            b = 0;

            break;

        case 7:
            /* 645 .. 700 */
            r = 1;
            g = 0;
            b = 0;

            break;

        case 8:
            /* 700 .. 780, intensity drop off */
            r = 1;
            g = 0;
            b = 0;
            s = .3f + ((.7f * (780 - wl)) / (780 - 700));

            break;

        case 9:
            /* invisible above 780 */
            r = 0;
            g = 0;
            b = 0;
            s = 0;

            break;
        } // end switch

        // apply intensity and gamma corrections.
        s *= gamma;
        r *= s;
        g *= s;
        b *= s;

        return new Color((float) r, (float) g, (float) b);
    }
}

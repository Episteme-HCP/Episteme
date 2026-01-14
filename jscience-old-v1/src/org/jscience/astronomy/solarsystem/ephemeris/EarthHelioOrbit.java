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

package org.jscience.astronomy.solarsystem.ephemeris;


//this code is rebundled after the code from
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class EarthHelioOrbit extends HelioOrbit {
/**
     * Creates a new EarthHelioOrbit object.
     *
     * @param d  DOCUMENT ME!
     * @param d1 DOCUMENT ME!
     * @param d2 DOCUMENT ME!
     * @param d3 DOCUMENT ME!
     * @param d4 DOCUMENT ME!
     * @param d5 DOCUMENT ME!
     * @param d6 DOCUMENT ME!
     * @param d7 DOCUMENT ME!
     * @param d8 DOCUMENT ME!
     */
    public EarthHelioOrbit(double d, double d1, double d2, double d3,
        double d4, double d5, double d6, double d7, double d8) {
        super(d, d1, d2, d3, d4, d5, d6, d7, d8);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector3 findHelioRectEquatCoords(double d) {
        double d1 = (d - 2451545D) / 365.25D;
        double d2 = Angle.reduce(3.804817769D + (8399.7111839999998D * d1));
        Vector3 vector3 = super.findHelioRectEquatCoords(d);
        vector3.set(vector3.x - (3.1199999999999999E-005D * Math.cos(d2)),
            vector3.y - (3.1199999999999999E-005D * Math.sin(d2)), vector3.z);

        return vector3;
    }
}

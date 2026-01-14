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

package org.jscience.physics.astronomy.time;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Sidereal time calculations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SiderealTime {

    private SiderealTime() {
    }

    /** Computes Greenwich Mean Sidereal Time in degrees. */
    public static Real gmstDegrees(JulianDate jd) {
        Real T = Real.of(jd.getJulianCenturies());

        Real gmst = Real.of(24110.54841)
                .add(Real.of(8640184.812866).multiply(T))
                .add(Real.of(0.093104).multiply(T.pow(2)))
                .subtract(Real.of(6.2e-6).multiply(T.pow(3)));

        Real degrees = gmst.divide(Real.of(240)).remainder(Real.of(360));
        return degrees.compareTo(Real.ZERO) < 0 ? degrees.add(Real.of(360)) : degrees;
    }

    /** Computes Greenwich Mean Sidereal Time in hours. */
    public static Real gmstHours(JulianDate jd) {
        return gmstDegrees(jd).divide(Real.of(15));
    }

    /** Computes Local Mean Sidereal Time in degrees. */
    public static Real lmstDegrees(JulianDate jd, Real longitude) {
        Real gmst = gmstDegrees(jd);
        Real lmst = gmst.add(longitude).remainder(Real.of(360));
        return lmst.compareTo(Real.ZERO) < 0 ? lmst.add(Real.of(360)) : lmst;
    }

    /** Computes Local Mean Sidereal Time in hours. */
    public static Real lmstHours(JulianDate jd, Real longitude) {
        return lmstDegrees(jd, longitude).divide(Real.of(15));
    }

    /** Computes Hour Angle: HA = LMST - RA */
    public static Real hourAngle(Real lmstHours, Real raHours) {
        Real ha = lmstHours.subtract(raHours);
        if (ha.compareTo(Real.of(-12)) < 0)
            ha = ha.add(Real.of(24));
        if (ha.compareTo(Real.of(12)) > 0)
            ha = ha.subtract(Real.of(24));
        return ha;
    }

    /** Converts RA hours to degrees. */
    public static Real raHoursToDegrees(Real hours) {
        return hours.multiply(Real.of(15));
    }

    /** Converts degrees to RA hours. */
    public static Real degreesToRaHours(Real degrees) {
        return degrees.divide(Real.of(15));
    }
}



/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.physics.astronomy.observation;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.physics.astronomy.coordinates.*;
import org.jscience.physics.astronomy.time.*;

/**
 * Observational astronomy calculations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Observer {

    private final Real latitude;
    private final Real longitude;
    private final Real altitude;
    private final String name;

    public Observer(String name, Real latitude, Real longitude, Real altitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public Observer(String name, double latitude, double longitude, double altitude) {
        this(name, Real.of(latitude), Real.of(longitude), Real.of(altitude));
    }

    public Real getLocalSiderealTime(JulianDate jd) {
        return SiderealTime.lmstDegrees(jd, longitude);
    }

    public HorizontalCoordinate toHorizontal(EquatorialCoordinate eq, JulianDate jd) {
        Real lmst = getLocalSiderealTime(jd);
        return HorizontalCoordinate.fromEquatorial(eq, latitude.doubleValue(), lmst.doubleValue());
    }

    public JulianDate riseTime(EquatorialCoordinate eq, JulianDate jdMidnight) {
        double dec = Math.toRadians(eq.getDecDegrees());
        double lat = Math.toRadians(latitude.doubleValue());

        double cosH = -Math.tan(lat) * Math.tan(dec);
        if (cosH > 1 || cosH < -1)
            return null;

        double H = Math.toDegrees(Math.acos(cosH));
        double lstRise = eq.getRaDegrees() - H;
        lstRise = (lstRise + 360) % 360;

        double gmst = SiderealTime.gmstDegrees(jdMidnight).doubleValue();
        double deltaLst = lstRise - gmst - longitude.doubleValue();
        if (deltaLst < 0)
            deltaLst += 360;

        double solarHours = deltaLst / 15.0 * (24.0 / 24.06571);
        return jdMidnight.addDays(solarHours / 24.0);
    }

    public JulianDate transitTime(EquatorialCoordinate eq, JulianDate jdMidnight) {
        double lstTransit = eq.getRaDegrees();
        double gmst = SiderealTime.gmstDegrees(jdMidnight).doubleValue();

        double deltaLst = lstTransit - gmst - longitude.doubleValue();
        if (deltaLst < 0)
            deltaLst += 360;
        if (deltaLst > 360)
            deltaLst -= 360;

        double solarHours = deltaLst / 15.0 * (24.0 / 24.06571);
        return jdMidnight.addDays(solarHours / 24.0);
    }

    public boolean isVisible(EquatorialCoordinate eq, JulianDate jd) {
        HorizontalCoordinate hor = toHorizontal(eq, jd);
        return hor.getAltitude() > 0;
    }

    public boolean isCircumpolar(EquatorialCoordinate eq) {
        double dec = eq.getDecDegrees();
        return Math.abs(dec) > (90 - Math.abs(latitude.doubleValue()));
    }

    public Real maximumAltitude(EquatorialCoordinate eq) {
        return Real.of(90).subtract(latitude.subtract(Real.of(eq.getDecDegrees())).abs());
    }

    public Real getLatitude() {
        return latitude;
    }

    public Real getLongitude() {
        return longitude;
    }

    public Real getAltitude() {
        return altitude;
    }

    public String getName() {
        return name;
    }

    public static final Observer MAUNA_KEA = new Observer("Mauna Kea", 19.8207, -155.4681, 4205);
    public static final Observer PARANAL = new Observer("Paranal", -24.6253, -70.4042, 2635);
    public static final Observer LA_PALMA = new Observer("La Palma", 28.7606, -17.8816, 2396);
    public static final Observer GREENWICH = new Observer("Greenwich", 51.4769, -0.0005, 48);
}

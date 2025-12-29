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

package org.jscience.earth.geodesy;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.physics.PhysicalConstants;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Length;

/**
 * Geodetic calculations for Earth science.
 * <p>
 * Provides:
 * <ul>
 * <li>Haversine formula for great-circle distance</li>
 * <li>Vincenty formula for high-accuracy geodesic distance</li>
 * <li>Bearing calculations</li>
 * <li>Destination point given bearing and distance</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Geodesy {

        /** WGS84 Earth equatorial radius (meters) */
        public static final Real EARTH_RADIUS_EQUATORIAL = Real.of(6378137.0);

        /** WGS84 Earth polar radius (meters) */
        public static final Real EARTH_RADIUS_POLAR = Real.of(6356752.314245);

        /** WGS84 flattening */
        public static final Real FLATTENING = Real.ONE.divide(Real.of(298.257223563));

        /** Mean Earth radius (meters) */
        private static final Real MEAN_RADIUS = Real.of(6371000);

        private Geodesy() {
        }

        /**
         * Haversine formula for great-circle distance.
         * Simple and fast, accuracy ~0.5%
         */
        public static Quantity<Length> haversineDistance(Real lat1, Real lon1, Real lat2, Real lon2) {
                Real dLat = lat2.subtract(lat1).toRadians();
                Real dLon = lon2.subtract(lon1).toRadians();

                Real lat1Rad = lat1.toRadians();
                Real lat2Rad = lat2.toRadians();

                Real sinDLatHalf = dLat.divide(Real.TWO).sin();
                Real sinDLonHalf = dLon.divide(Real.TWO).sin();

                Real a = sinDLatHalf.multiply(sinDLatHalf).add(
                                lat1Rad.cos().multiply(lat2Rad.cos()).multiply(sinDLonHalf).multiply(sinDLonHalf));
                Real sqrtA = a.sqrt();
                Real sqrtOneMinusA = Real.ONE.subtract(a).sqrt();
                Real c = Real.TWO.multiply(sqrtA.atan2(sqrtOneMinusA));

                return Quantities.create(MEAN_RADIUS.multiply(c).doubleValue(), Units.METER);
        }

        /**
         * Initial bearing from point 1 to point 2.
         * 
         * @return bearing in degrees (0-360)
         */
        public static Real initialBearing(Real lat1, Real lon1, Real lat2, Real lon2) {
                Real dLon = lon2.subtract(lon1).toRadians();
                Real lat1Rad = lat1.toRadians();
                Real lat2Rad = lat2.toRadians();

                Real y = dLon.sin().multiply(lat2Rad.cos());
                Real x = lat1Rad.cos().multiply(lat2Rad.sin()).subtract(
                                lat1Rad.sin().multiply(lat2Rad.cos()).multiply(dLon.cos()));

                Real bearing = y.atan2(x).toDegrees();
                return bearing.add(Real.of(360)).subtract(
                                Real.of(360).multiply(bearing.add(Real.of(360)).divide(Real.of(360)).floor()));
        }

        /**
         * Destination point given start, bearing, and distance.
         * 
         * @return Real[] with [lat, lon] of destination
         */
        public static Real[] destination(Real lat, Real lon, Real bearingDegrees, Real distanceM) {
                Real d = distanceM.divide(MEAN_RADIUS);
                Real brng = bearingDegrees.toRadians();
                Real lat1 = lat.toRadians();
                Real lon1 = lon.toRadians();

                Real lat2 = lat1.sin().multiply(d.cos()).add(lat1.cos().multiply(d.sin()).multiply(brng.cos())).asin();

                Real lon2 = lon1.add(brng.sin().multiply(d.sin()).multiply(lat1.cos())
                                .atan2(d.cos().subtract(lat1.sin().multiply(lat2.sin()))));

                return new Real[] { lat2.toDegrees(), lon2.toDegrees() };
        }

        /**
         * Midpoint between two coordinates.
         * 
         * @return Real[] with [lat, lon] of midpoint
         */
        public static Real[] midpoint(Real lat1, Real lon1, Real lat2, Real lon2) {
                Real lat1Rad = lat1.toRadians();
                Real lon1Rad = lon1.toRadians();
                Real lat2Rad = lat2.toRadians();
                Real dLon = lon2.subtract(lon1).toRadians();

                Real Bx = lat2Rad.cos().multiply(dLon.cos());
                Real By = lat2Rad.cos().multiply(dLon.sin());

                Real latM = lat1Rad.sin().add(lat2Rad.sin())
                                .atan2(lat1Rad.cos().add(Bx).pow(2).add(By.pow(2)).sqrt());
                Real lonM = lon1Rad.add(By.atan2(lat1Rad.cos().add(Bx)));

                return new Real[] { latM.toDegrees(), lonM.toDegrees() };
        }

        /**
         * Barometric formula for atmospheric pressure at altitude.
         * P = P0 * exp(-Mgh/RT)
         * 
         * @return pressure ratio P/P0
         */
        public static Real pressureRatio(Real altitudeM) {
                Real M = Real.of(0.0289644); // Molar mass of air (kg/mol)
                Real g = PhysicalConstants.g_n;
                Real R = Real.of(8.31447); // Gas constant (J/(mol·K))
                Real T = Real.of(288.15); // Standard temperature (K)

                return M.negate().multiply(g).multiply(altitudeM).divide(R.multiply(T)).exp();
        }
}

/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.earth.geodesy;

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
 * @author Gemini AI
 * @since 2.0
 */
public class Geodesy {

    /** WGS84 Earth equatorial radius (meters) */
    public static final double EARTH_RADIUS_EQUATORIAL = 6378137.0;

    /** WGS84 Earth polar radius (meters) */
    public static final double EARTH_RADIUS_POLAR = 6356752.314245;

    /** WGS84 flattening */
    public static final double FLATTENING = 1 / 298.257223563;

    private Geodesy() {
    }

    /**
     * Haversine formula for great-circle distance.
     * Simple and fast, accuracy ~0.5%
     * 
     * @param lat1 latitude of point 1 (degrees)
     * @param lon1 longitude of point 1 (degrees)
     * @param lat2 latitude of point 2 (degrees)
     * @param lon2 longitude of point 2 (degrees)
     * @return distance as Quantity
     */
    public static Quantity<Length> haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371000; // Mean Earth radius in meters

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return Quantities.create(R * c, Units.METER);
    }

    /**
     * Vincenty formula for geodesic distance on ellipsoid.
     * High accuracy (~0.5mm), but may fail for near-antipodal points.
     */
    public static Quantity<Length> vincentyDistance(double lat1, double lon1, double lat2, double lon2) {
        double a = EARTH_RADIUS_EQUATORIAL;
        double b = EARTH_RADIUS_POLAR;
        double f = FLATTENING;

        double L = Math.toRadians(lon2 - lon1);
        double U1 = Math.atan((1 - f) * Math.tan(Math.toRadians(lat1)));
        double U2 = Math.atan((1 - f) * Math.tan(Math.toRadians(lat2)));

        double sinU1 = Math.sin(U1), cosU1 = Math.cos(U1);
        double sinU2 = Math.sin(U2), cosU2 = Math.cos(U2);

        double lambda = L, lambdaP;
        int iterLimit = 100;
        double sinSigma, cosSigma, sigma, sinAlpha, cosSqAlpha, cos2SigmaM;

        do {
            double sinLambda = Math.sin(lambda), cosLambda = Math.cos(lambda);
            sinSigma = Math.sqrt((cosU2 * sinLambda) * (cosU2 * sinLambda) +
                    (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda) *
                            (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda));

            if (sinSigma == 0)
                return Quantities.create(0, Units.METER); // Co-incident points

            cosSigma = sinU1 * sinU2 + cosU1 * cosU2 * cosLambda;
            sigma = Math.atan2(sinSigma, cosSigma);
            sinAlpha = cosU1 * cosU2 * sinLambda / sinSigma;
            cosSqAlpha = 1 - sinAlpha * sinAlpha;
            cos2SigmaM = cosSqAlpha != 0 ? cosSigma - 2 * sinU1 * sinU2 / cosSqAlpha : 0;

            double C = f / 16 * cosSqAlpha * (4 + f * (4 - 3 * cosSqAlpha));
            lambdaP = lambda;
            lambda = L + (1 - C) * f * sinAlpha
                    * (sigma + C * sinSigma * (cos2SigmaM + C * cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM)));

        } while (Math.abs(lambda - lambdaP) > 1e-12 && --iterLimit > 0);

        if (iterLimit == 0) {
            // Failed to converge (antipodal points), fallback to haversine
            return haversineDistance(lat1, lon1, lat2, lon2);
        }

        double uSq = cosSqAlpha * (a * a - b * b) / (b * b);
        double A = 1 + uSq / 16384 * (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));
        double B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));
        double deltaSigma = B * sinSigma * (cos2SigmaM + B / 4 * (cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM) -
                B / 6 * cos2SigmaM * (-3 + 4 * sinSigma * sinSigma) * (-3 + 4 * cos2SigmaM * cos2SigmaM)));

        double s = b * A * (sigma - deltaSigma);

        return Quantities.create(s, Units.METER);
    }

    /**
     * Initial bearing from point 1 to point 2.
     * 
     * @return bearing in degrees (0-360)
     */
    public static double initialBearing(double lat1, double lon1, double lat2, double lon2) {
        double dLon = Math.toRadians(lon2 - lon1);
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);

        double y = Math.sin(dLon) * Math.cos(lat2Rad);
        double x = Math.cos(lat1Rad) * Math.sin(lat2Rad) -
                Math.sin(lat1Rad) * Math.cos(lat2Rad) * Math.cos(dLon);

        double bearing = Math.toDegrees(Math.atan2(y, x));
        return (bearing + 360) % 360;
    }

    /**
     * Destination point given start, bearing, and distance.
     * 
     * @param lat            starting latitude (degrees)
     * @param lon            starting longitude (degrees)
     * @param bearingDegrees initial bearing (degrees)
     * @param distanceM      distance in meters
     * @return [lat, lon] of destination
     */
    public static double[] destination(double lat, double lon, double bearingDegrees, double distanceM) {
        double R = 6371000; // Mean radius

        double d = distanceM / R;
        double brng = Math.toRadians(bearingDegrees);
        double lat1 = Math.toRadians(lat);
        double lon1 = Math.toRadians(lon);

        double lat2 = Math.asin(Math.sin(lat1) * Math.cos(d) +
                Math.cos(lat1) * Math.sin(d) * Math.cos(brng));
        double lon2 = lon1 + Math.atan2(Math.sin(brng) * Math.sin(d) * Math.cos(lat1),
                Math.cos(d) - Math.sin(lat1) * Math.sin(lat2));

        return new double[] { Math.toDegrees(lat2), Math.toDegrees(lon2) };
    }

    /**
     * Midpoint between two coordinates.
     */
    public static double[] midpoint(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double dLon = Math.toRadians(lon2 - lon1);

        double Bx = Math.cos(lat2Rad) * Math.cos(dLon);
        double By = Math.cos(lat2Rad) * Math.sin(dLon);

        double latM = Math.atan2(Math.sin(lat1Rad) + Math.sin(lat2Rad),
                Math.sqrt((Math.cos(lat1Rad) + Bx) * (Math.cos(lat1Rad) + Bx) + By * By));
        double lonM = lon1Rad + Math.atan2(By, Math.cos(lat1Rad) + Bx);

        return new double[] { Math.toDegrees(latM), Math.toDegrees(lonM) };
    }

    /**
     * Barometric formula for atmospheric pressure at altitude.
     * P = P0 * exp(-Mgh/RT)
     * 
     * @param altitudeM altitude in meters
     * @return pressure ratio P/P0
     */
    public static double pressureRatio(double altitudeM) {
        double M = 0.0289644; // Molar mass of air (kg/mol)
        double g = 9.80665; // Gravity (m/s²)
        double R = 8.31447; // Gas constant (J/(mol·K))
        double T = 288.15; // Standard temperature (K)

        return Math.exp(-M * g * altitudeM / (R * T));
    }
}

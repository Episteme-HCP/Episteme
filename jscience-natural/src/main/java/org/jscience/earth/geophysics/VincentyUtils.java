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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.jscience.earth.geophysics;

import org.jscience.measure.Units;

/**
 * High-precision geodesy calculations on the WGS-84 ellipsoid using Vincenty's
 * formulae.
 * <p>
 * Based on: T. Vincenty, "Direct and Inverse Solutions of Geodesics on the
 * Ellipsoid with Application of Nested Equations", Survey Review, Vol. 23,
 * No. 176, pp. 88-93, April 1975.
 * DOI: 10.1179/sre.1975.23.176.88
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class VincentyUtils {

        // WGS-84 ellipsoid parameters
        private static final double a = 6378137.0; // semi-major axis (meters)
        private static final double f = 1 / 298.257223563; // flattening
        private static final double b = 6356752.314245; // semi-minor axis

        private VincentyUtils() {
        }

        /**
         * Calculates the distance between two points on the ellipsoid.
         * 
         * @param lat1 Latitude 1
         * @param lon1 Longitude 1
         * @param lat2 Latitude 2
         * @param lon2 Longitude 2
         * @return Distance in meters
         */
        @SuppressWarnings("unchecked")
        public static double distance(org.jscience.measure.Quantity<org.jscience.measure.quantity.Angle> lat1,
                        org.jscience.measure.Quantity<org.jscience.measure.quantity.Angle> lon1,
                        org.jscience.measure.Quantity<org.jscience.measure.quantity.Angle> lat2,
                        org.jscience.measure.Quantity<org.jscience.measure.quantity.Angle> lon2) {

                double phi1 = lat1.to(
                                (org.jscience.measure.Unit<org.jscience.measure.quantity.Angle>) (org.jscience.measure.Unit<?>) Units.RADIAN)
                                .getValue()
                                .doubleValue();
                double lambda1 = lon1.to(
                                (org.jscience.measure.Unit<org.jscience.measure.quantity.Angle>) (org.jscience.measure.Unit<?>) Units.RADIAN)
                                .getValue().doubleValue();
                double phi2 = lat2.to(
                                (org.jscience.measure.Unit<org.jscience.measure.quantity.Angle>) (org.jscience.measure.Unit<?>) Units.RADIAN)
                                .getValue()
                                .doubleValue();
                double lambda2 = lon2.to(
                                (org.jscience.measure.Unit<org.jscience.measure.quantity.Angle>) (org.jscience.measure.Unit<?>) Units.RADIAN)
                                .getValue().doubleValue();

                double L = lambda2 - lambda1;
                double U1 = Math.atan((1 - f) * Math.tan(phi1));
                double U2 = Math.atan((1 - f) * Math.tan(phi2));
                double sinU1 = Math.sin(U1), cosU1 = Math.cos(U1);
                double sinU2 = Math.sin(U2), cosU2 = Math.cos(U2);

                double lambda = L;
                double lambdaP;
                double iterLimit = 100;

                double cosSqAlpha = 0;
                double sinSigma = 0;
                double cos2SigmaM = 0;
                double cosSigma = 0;
                double sigma = 0;

                do {
                        double sinLambda = Math.sin(lambda);
                        double cosLambda = Math.cos(lambda);
                        sinSigma = Math.sqrt((cosU2 * sinLambda) * (cosU2 * sinLambda)
                                        + (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda)
                                                        * (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda));

                        if (sinSigma == 0)
                                return 0; // Co-incident points

                        cosSigma = sinU1 * sinU2 + cosU1 * cosU2 * cosLambda;
                        sigma = Math.atan2(sinSigma, cosSigma);

                        double sinAlpha = cosU1 * cosU2 * sinLambda / sinSigma;
                        cosSqAlpha = 1 - sinAlpha * sinAlpha;

                        cos2SigmaM = cosSigma - 2 * sinU1 * sinU2 / cosSqAlpha;
                        if (Double.isNaN(cos2SigmaM))
                                cos2SigmaM = 0; // Equatorial line

                        double C = f / 16 * cosSqAlpha * (4 + f * (4 - 3 * cosSqAlpha));
                        lambdaP = lambda;
                        lambda = L + (1 - C) * f * sinAlpha
                                        * (sigma + C * sinSigma * (cos2SigmaM
                                                        + C * cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM)));

                } while (Math.abs(lambda - lambdaP) > 1e-12 && --iterLimit > 0);

                if (iterLimit == 0)
                        return Double.NaN; // Failure to converge

                double uSq = cosSqAlpha * (a * a - b * b) / (b * b);
                double A = 1 + uSq / 16384 * (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));
                double B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));
                double deltaSigma = B * sinSigma
                                * (cos2SigmaM + B / 4 * (cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM)
                                                - B / 6 * cos2SigmaM * (-3 + 4 * sinSigma * sinSigma)
                                                                * (-3 + 4 * cos2SigmaM * cos2SigmaM)));

                return b * A * (sigma - deltaSigma);
        }

        /**
         * Calculates the destination point given start point, initial bearing and
         * distance.
         * 
         * @return double[] {lat, lon} in Radians
         */
        public static double[] destination(double lat1, double lon1, double brng, double dist) {
                double s = dist;
                double alpha1 = brng;
                double sinAlpha1 = Math.sin(alpha1);
                double cosAlpha1 = Math.cos(alpha1);

                double tanU1 = (1 - f) * Math.tan(lat1);
                double cosU1 = 1 / Math.sqrt((1 + tanU1 * tanU1));
                double sinU1 = tanU1 * cosU1;

                double sigma1 = Math.atan2(tanU1, cosAlpha1);
                double sinAlpha = cosU1 * sinAlpha1;
                double cosSqAlpha = 1 - sinAlpha * sinAlpha;
                double uSq = cosSqAlpha * (a * a - b * b) / (b * b);

                double A = 1 + uSq / 16384 * (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));
                double B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));

                double sigma = s / (b * A);
                double sigmaP = 2 * Math.PI;

                double cos2SigmaM = 0;
                double sinSigma = 0;
                double cosSigma = 0;

                while (Math.abs(sigma - sigmaP) > 1e-12) {
                        cos2SigmaM = Math.cos(2 * sigma1 + sigma);
                        sinSigma = Math.sin(sigma);
                        cosSigma = Math.cos(sigma);
                        double deltaSigma = B * sinSigma
                                        * (cos2SigmaM + B / 4 * (cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM) -
                                                        B / 6 * cos2SigmaM * (-3 + 4 * sinSigma * sinSigma)
                                                                        * (-3 + 4 * cos2SigmaM * cos2SigmaM)));
                        sigmaP = sigma;
                        sigma = s / (b * A) + deltaSigma;
                }

                double tmp = sinU1 * sinSigma - cosU1 * cosSigma * cosAlpha1;
                double lat2 = Math.atan2(sinU1 * cosSigma + cosU1 * sinSigma * cosAlpha1,
                                (1 - f) * Math.sqrt(sinAlpha * sinAlpha + tmp * tmp));

                double lambda = Math.atan2(sinSigma * sinAlpha1, cosU1 * cosSigma - sinU1 * sinSigma * cosAlpha1);
                double C = f / 16 * cosSqAlpha * (4 + f * (4 - 3 * cosSqAlpha));
                double L = lambda - (1 - C) * f * sinAlpha *
                                (sigma + C * sinSigma
                                                * (cos2SigmaM + C * cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM)));

                double lon2 = lon1 + L;

                return new double[] { lat2, lon2 };
        }
}

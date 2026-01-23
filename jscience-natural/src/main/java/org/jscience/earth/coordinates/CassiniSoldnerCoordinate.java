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

package org.jscience.earth.coordinates;

import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Angle;
import org.jscience.measure.quantity.Length;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a coordinate in the Cassini-Soldner projection.
 * Historically used for topographic mapping in the UK, Germany and other countries.
 * It is a transverse cylindrical projection that is neither conformal nor equal-area.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CassiniSoldnerCoordinate {

    private final Real easting;
    private final Real northing;
    private final ReferenceEllipsoid ellipsoid;
    private final CassiniParameters parameters;

    public record CassiniParameters(
            double latitudeOfOrigin,
            double centralMeridian,
            double falseEasting,
            double falseNorthing) {
    }

    public CassiniSoldnerCoordinate(Real easting, Real northing, ReferenceEllipsoid ellipsoid, CassiniParameters params) {
        this.easting = easting;
        this.northing = northing;
        this.ellipsoid = ellipsoid;
        this.parameters = params;
    }

    public static CassiniSoldnerCoordinate fromGeodetic(GeodeticCoordinate geodetic, CassiniParameters params) {
        ReferenceEllipsoid el = geodetic.getEllipsoid();
        double a = el.getSemiMajorAxis().getValue().doubleValue();
        double e2 = el.getEccentricitySquared().doubleValue();

        double phi = geodetic.getLatitude().to(Units.RADIAN).getValue().doubleValue();
        double phi0 = Math.toRadians(params.latitudeOfOrigin);
        double lambda = geodetic.getLongitude().to(Units.RADIAN).getValue().doubleValue();
        double lambda0 = Math.toRadians(params.centralMeridian);

        double A = (lambda - lambda0) * Math.cos(phi);
        double T = Math.tan(phi) * Math.tan(phi);
        double C = (e2 / (1 - e2)) * Math.pow(Math.cos(phi), 2);
        double N = a / Math.sqrt(1 - e2 * Math.pow(Math.sin(phi), 2));
        double M = calculateM(phi, a, e2);
        double M0 = calculateM(phi0, a, e2);

        double x = params.falseEasting + N * (A - T * Math.pow(A, 3) / 6.0 - (8 - T + 8 * C) * T * Math.pow(A, 5) / 120.0);
        double y = params.falseNorthing + M - M0 + N * Math.tan(phi) * (Math.pow(A, 2) / 2.0 + (5 - T + 6 * C) * Math.pow(A, 4) / 24.0);

        return new CassiniSoldnerCoordinate(Real.of(x), Real.of(y), el, params);
    }

    private static double calculateM(double phi, double a, double e2) {
        double e4 = e2 * e2;
        double e6 = e4 * e2;
        return a * ((1 - e2 / 4.0 - 3 * e4 / 64.0 - 5 * e6 / 256.0) * phi
                - (3 * e2 / 8.0 + 3 * e4 / 32.0 + 45 * e6 / 1024.0) * Math.sin(2 * phi)
                + (15 * e4 / 256.0 + 45 * e6 / 1024.0) * Math.sin(4 * phi)
                - (35 * e6 / 3072.0) * Math.sin(6 * phi));
    }

    public GeodeticCoordinate toGeodetic() {
        ReferenceEllipsoid el = ellipsoid;
        double a = el.getSemiMajorAxis().getValue().doubleValue();
        double e2 = el.getEccentricitySquared().doubleValue();

        double phi0 = Math.toRadians(parameters.latitudeOfOrigin);
        double lambda0 = Math.toRadians(parameters.centralMeridian);
        double M0 = calculateM(phi0, a, e2);

        double M1 = M0 + (northing.doubleValue() - parameters.falseNorthing);
        
        // Iterative solution for phi1
        double mu1 = M1 / (a * (1 - e2 / 4.0 - 3 * e2 * e2 / 64.0 - 5 * Math.pow(e2, 3) / 256.0));
        double e1 = (1 - Math.sqrt(1 - e2)) / (1 + Math.sqrt(1 - e2));
        double phi1 = mu1 + (3 * e1 / 2.0 - 27 * Math.pow(e1, 3) / 32.0) * Math.sin(2 * mu1)
                + (21 * e1 * e1 / 16.0 - 55 * Math.pow(e1, 4) / 32.0) * Math.sin(4 * mu1)
                + (151 * Math.pow(e1, 3) / 96.0) * Math.sin(6 * mu1);

        double T1 = Math.tan(phi1) * Math.tan(phi1);
        double N1 = a / Math.sqrt(1 - e2 * Math.pow(Math.sin(phi1), 2));
        double R1 = a * (1 - e2) / Math.pow(1 - e2 * Math.pow(Math.sin(phi1), 2), 1.5);
        double D = (easting.doubleValue() - parameters.falseEasting) / N1;

        double phi = phi1 - (N1 * Math.tan(phi1) / R1) * (D * D / 2.0 - (1 + 3 * T1) * Math.pow(D, 4) / 24.0);
        double lambda = lambda0 + (D - T1 * Math.pow(D, 3) / 3.0 + (1 + 3 * T1) * T1 * Math.pow(D, 5) / 15.0) / Math.cos(phi1);

        return new GeodeticCoordinate(
                Quantities.create(Math.toDegrees(phi), Units.DEGREE_ANGLE),
                Quantities.create(Math.toDegrees(lambda), Units.DEGREE_ANGLE),
                Quantities.create(0, Units.METER),
                el);
    }
}

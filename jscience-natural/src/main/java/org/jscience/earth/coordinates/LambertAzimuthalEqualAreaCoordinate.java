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
 * Represents a coordinate in the Lambert Azimuthal Equal-Area (LAEA) projection.
 * Frequently used for atlas maps of continents and hemispheric maps.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class LambertAzimuthalEqualAreaCoordinate {

    private final Real easting;
    private final Real northing;
    private final ReferenceEllipsoid ellipsoid;
    private final LAEAParameters parameters;

    public record LAEAParameters(
            double latitudeOfTrueScale, // Usually center
            double centralMeridian,
            double falseEasting,
            double falseNorthing) {
    }

    public LambertAzimuthalEqualAreaCoordinate(Real easting, Real northing, ReferenceEllipsoid ellipsoid, LAEAParameters params) {
        this.easting = easting;
        this.northing = northing;
        this.ellipsoid = ellipsoid;
        this.parameters = params;
    }

    public static LambertAzimuthalEqualAreaCoordinate fromGeodetic(GeodeticCoordinate geodetic, LAEAParameters params) {
        ReferenceEllipsoid el = geodetic.getEllipsoid();
        double a = el.getSemiMajorAxis().getValue().doubleValue();
        double e2 = el.getEccentricitySquared().doubleValue();
        double e = Math.sqrt(e2);

        double phi = geodetic.getLatitude().to(Units.RADIAN).getValue().doubleValue();
        double phi0 = Math.toRadians(params.latitudeOfTrueScale);
        double lambda = geodetic.getLongitude().to(Units.RADIAN).getValue().doubleValue();
        double lambda0 = Math.toRadians(params.centralMeridian);

        double q = calculateQ(phi, e);
        double q0 = calculateQ(phi0, e);
        double qp = calculateQ(Math.PI / 2.0, e);

        double beta = Math.asin(q / qp);
        double beta0 = Math.asin(q0 / qp);

        double m0 = Math.cos(phi0) / Math.sqrt(1 - e2 * Math.pow(Math.sin(phi0), 2));
        double D = a * m0 / (qp * Math.cos(beta0));
        
        double Rq = a * Math.sqrt(qp / 2.0);
        
        double cosBeta = Math.cos(beta);
        double sinBeta = Math.sin(beta);
        double cosBeta0 = Math.cos(beta0);
        double sinBeta0 = Math.sin(beta0);
        double cosDeltaLambda = Math.cos(lambda - lambda0);
        double sinDeltaLambda = Math.sin(lambda - lambda0);

        double B = 1 + sinBeta0 * sinBeta + cosBeta0 * cosBeta * cosDeltaLambda;
        if (B <= 0) {
            // Point is at the antipodes of the origin, not projectable in a single hemisphere
            throw new IllegalArgumentException("Coordinate out of projection range");
        }
        
        double kPrime = Math.sqrt(2.0 / B);
        
        double x = params.falseEasting + (Rq * D) * kPrime * cosBeta * sinDeltaLambda;
        double y = params.falseNorthing + (Rq / D) * kPrime * (cosBeta0 * sinBeta - sinBeta0 * cosBeta * cosDeltaLambda);

        return new LambertAzimuthalEqualAreaCoordinate(Real.of(x), Real.of(y), el, params);
    }

    private static double calculateQ(double phi, double e) {
        double sinPhi = Math.sin(phi);
        if (Math.abs(e) < 1e-10) return 2 * sinPhi;
        return (1 - e * e) * (sinPhi / (1 - e * e * sinPhi * sinPhi)
                - (1 / (2 * e)) * Math.log((1 - e * sinPhi) / (1 + e * sinPhi)));
    }

    public GeodeticCoordinate toGeodetic() {
        ReferenceEllipsoid el = ellipsoid;
        double a = el.getSemiMajorAxis().getValue().doubleValue();
        double e2 = el.getEccentricitySquared().doubleValue();
        double e = Math.sqrt(e2);

        double phi0 = Math.toRadians(parameters.latitudeOfTrueScale);
        double lambda0 = Math.toRadians(parameters.centralMeridian);

        double q0 = calculateQ(phi0, e);
        double qp = calculateQ(Math.PI / 2.0, e);
        double beta0 = Math.asin(q0 / qp);
        double m0 = Math.cos(phi0) / Math.sqrt(1 - e2 * Math.pow(Math.sin(phi0), 2));
        double D = a * m0 / (qp * Math.cos(beta0));
        double Rq = a * Math.sqrt(qp / 2.0);

        double dx = easting.doubleValue() - parameters.falseEasting;
        double dy = northing.doubleValue() - parameters.falseNorthing;

        double rho = Math.sqrt(Math.pow(dx / D, 2) + Math.pow(dy * D, 2));
        if (rho < 1e-10) {
            return new GeodeticCoordinate(
                    Quantities.create(Math.toDegrees(phi0), Units.DEGREE_ANGLE),
                    Quantities.create(Math.toDegrees(lambda0), Units.DEGREE_ANGLE),
                    Quantities.create(0, Units.METER),
                    el);
        }

        double C = 2 * Math.asin(rho / (2 * Rq));
        double sinC = Math.sin(C);
        double cosC = Math.cos(C);
        
        double sinBeta = cosC * Math.sin(beta0) + (dy * D * sinC * Math.cos(beta0) / rho);
        double q = qp * sinBeta;

        // Iterative solution for phi
        double phi = Math.asin(sinBeta); // Approximation
        for (int i = 0; i < 5; i++) {
            double sinPhi = Math.sin(phi);
            double deltaPhi = (1 - e2 * sinPhi * sinPhi) * (1 - e2 * sinPhi * sinPhi) / (2 * Math.cos(phi))
                    * (q / (1 - e2) - sinPhi / (1 - e2 * sinPhi * sinPhi)
                    + (1 / (2 * e)) * Math.log((1 - e * sinPhi) / (1 + e * sinPhi)));
            phi += deltaPhi;
        }

        double lambda = lambda0 + Math.atan2(dx * sinC * Math.cos(beta0), D * rho * Math.cos(beta0) * cosC - D * dy * Math.sin(beta0) * sinC);

        return new GeodeticCoordinate(
                Quantities.create(Math.toDegrees(phi), Units.DEGREE_ANGLE),
                Quantities.create(Math.toDegrees(lambda), Units.DEGREE_ANGLE),
                Quantities.create(0, Units.METER),
                el);
    }
}

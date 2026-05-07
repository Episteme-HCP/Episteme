/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.natural.earth.coordinates;

import org.episteme.core.measure.Quantity;
import org.episteme.core.measure.Quantities;
import org.episteme.core.measure.Units;
import org.episteme.core.measure.quantity.Length;
import org.episteme.core.mathematics.numbers.real.Real;
import java.io.Serializable;

/**
 * Represents a coordinate in the Albers Equal-Area Conic projection.
 * This projection is frequently used for thematic maps of mid-latitude regions.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class AlbersEqualAreaCoordinate implements EarthCoordinate, Serializable {

    private static final long serialVersionUID = 1L;

    private final Real easting;
    private final Real northing;
    private final ReferenceEllipsoid ellipsoid;
    private final AlbersParameters parameters;

    public record AlbersParameters(
            double standardParallel1,
            double standardParallel2,
            double latitudeOfOrigin,
            double centralMeridian,
            double falseEasting,
            double falseNorthing) {
    }

    public AlbersEqualAreaCoordinate(Real easting, Real northing, ReferenceEllipsoid ellipsoid, AlbersParameters params) {
        this.easting = easting;
        this.northing = northing;
        this.ellipsoid = ellipsoid;
        this.parameters = params;
    }

    public Quantity<Length> getEasting() {
        return Quantities.create(easting.doubleValue(), Units.METER);
    }

    public Quantity<Length> getNorthing() {
        return Quantities.create(northing.doubleValue(), Units.METER);
    }

    public static AlbersEqualAreaCoordinate fromGeodetic(GeodeticCoordinate geodetic, AlbersParameters params) {
        ReferenceEllipsoid el = geodetic.getEllipsoid();
        double a = el.getSemiMajorAxis().getValue().doubleValue();
        double e2 = el.getEccentricitySquared().doubleValue();
        double e = Math.sqrt(e2);

        double phi = geodetic.getLatitude().to(Units.RADIAN).getValue().doubleValue();
        double phi1 = Math.toRadians(params.standardParallel1);
        double phi2 = Math.toRadians(params.standardParallel2);
        double phi0 = Math.toRadians(params.latitudeOfOrigin);
        double lambda = geodetic.getLongitude().to(Units.RADIAN).getValue().doubleValue();
        double lambda0 = Math.toRadians(params.centralMeridian);

        double m1 = Math.cos(phi1) / Math.sqrt(1 - e2 * Math.pow(Math.sin(phi1), 2));
        double m2 = Math.cos(phi2) / Math.sqrt(1 - e2 * Math.pow(Math.sin(phi2), 2));

        double q = calculateQ(phi, e);
        double q1 = calculateQ(phi1, e);
        double q2 = calculateQ(phi2, e);
        double q0 = calculateQ(phi0, e);

        double n = (Math.pow(m1, 2) - Math.pow(m2, 2)) / (q2 - q1);
        double C = Math.pow(m1, 2) + n * q1;
        double rho0 = a * Math.sqrt(C - n * q0) / n;

        double theta = n * (lambda - lambda0);
        double rho = a * Math.sqrt(C - n * q) / n;

        double x = params.falseEasting + rho * Math.sin(theta);
        double y = params.falseNorthing + rho0 - rho * Math.cos(theta);

        return new AlbersEqualAreaCoordinate(Real.of(x), Real.of(y), el, params);
    }

    private static double calculateQ(double phi, double e) {
        double sinPhi = Math.sin(phi);
        return (1 - Math.pow(e, 2)) * (sinPhi / (1 - Math.pow(e, 2) * Math.pow(sinPhi, 2))
                - (1 / (2 * e)) * Math.log((1 - e * sinPhi) / (1 + e * sinPhi)));
    }

    @Override
    public String getCoordinateSystem() { return "Albers"; }

    @Override
    public ReferenceEllipsoid getEllipsoid() { return ellipsoid; }

    @Override
    public ECEFCoordinate toECEF() { return toGeodetic().toECEF(); }

    @Override
    public GeodeticCoordinate toGeodetic() {
        double a = ellipsoid.getSemiMajorAxis().getValue().doubleValue();
        double e2 = ellipsoid.getEccentricitySquared().doubleValue();
        double e = Math.sqrt(e2);

        double phi1 = Math.toRadians(parameters.standardParallel1);
        double phi2 = Math.toRadians(parameters.standardParallel2);
        double phi0 = Math.toRadians(parameters.latitudeOfOrigin);
        double lambda0 = Math.toRadians(parameters.centralMeridian);

        double m1 = Math.cos(phi1) / Math.sqrt(1 - e2 * Math.pow(Math.sin(phi1), 2));
        double m2 = Math.cos(phi2) / Math.sqrt(1 - e2 * Math.pow(Math.sin(phi2), 2));

        double q1 = calculateQ(phi1, e);
        double q2 = calculateQ(phi2, e);
        double q0 = calculateQ(phi0, e);

        double n = (Math.pow(m1, 2) - Math.pow(m2, 2)) / (q2 - q1);
        double C = Math.pow(m1, 2) + n * q1;
        double rho0 = a * Math.sqrt(C - n * q0) / n;

        double dx = easting.doubleValue() - parameters.falseEasting;
        double dy = rho0 - northing.doubleValue() + parameters.falseNorthing;

        double rho = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        double theta = Math.atan2(dx, dy);

        double q = (C - Math.pow(rho * n / a, 2)) / n;
        
        // Iterative solution for phi
        double phi = Math.asin(q / 2.0); // Simple initial guess
        for (int i = 0; i < 5; i++) {
            double sinPhi = Math.sin(phi);
            double deltaPhi = (1 - e2 * sinPhi * sinPhi) * (1 - e2 * sinPhi * sinPhi) / (2 * Math.cos(phi))
                    * (q / (1 - e2) - sinPhi / (1 - e2 * sinPhi * sinPhi)
                    + (1 / (2 * e)) * Math.log((1 - e * sinPhi) / (1 + e * sinPhi)));
            phi += deltaPhi;
        }

        double lambda = lambda0 + theta / n;

        return new GeodeticCoordinate(
                Quantities.create(Math.toDegrees(phi), Units.DEGREE_ANGLE),
                Quantities.create(Math.toDegrees(lambda), Units.DEGREE_ANGLE),
                Quantities.create(0, Units.METER),
                ellipsoid);
    }
    @Override
    public int dimension() {
        return 0;
    }

    @Override
    public int ambientDimension() {
        return 3;
    }

    @Override
    public String description() {
        return "Albers Equal Area Coordinate: " + toString();
    }

    @Override
    public String toString() {
        return String.format("Albers[E=%.2f, N=%.2f]", easting.doubleValue(), northing.doubleValue());
    }
}


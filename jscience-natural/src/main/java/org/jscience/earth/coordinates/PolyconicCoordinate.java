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
import org.jscience.measure.quantity.Length;
import org.jscience.mathematics.numbers.real.Real;
import java.io.Serializable;

/**
 * Represents a coordinate in the American Polyconic projection.
 * Historically used by the USGS for topographic maps.
 * It is neither conformal nor equal-area.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PolyconicCoordinate implements EarthCoordinate, Serializable {

    private static final long serialVersionUID = 1L;

    private final Real easting;
    private final Real northing;
    private final ReferenceEllipsoid ellipsoid;
    private final PolyconicParameters parameters;

    public record PolyconicParameters(
            double latitudeOfOrigin,
            double centralMeridian,
            double falseEasting,
            double falseNorthing) {
    }

    public PolyconicCoordinate(Real easting, Real northing, ReferenceEllipsoid ellipsoid, PolyconicParameters params) {
        this.easting = easting;
        this.northing = northing;
        this.ellipsoid = ellipsoid;
        this.parameters = params;
    }

    public Quantity<Length> getEasting() { return Quantities.create(easting.doubleValue(), Units.METER); }
    public Quantity<Length> getNorthing() { return Quantities.create(northing.doubleValue(), Units.METER); }

    @Override
    public String getCoordinateSystem() { return "Polyconic"; }

    @Override
    public ReferenceEllipsoid getEllipsoid() { return ellipsoid; }

    @Override
    public ECEFCoordinate toECEF() { return toGeodetic().toECEF(); }

    public static PolyconicCoordinate fromGeodetic(GeodeticCoordinate geodetic, PolyconicParameters params) {
        ReferenceEllipsoid el = geodetic.getEllipsoid();
        double a = el.getSemiMajorAxis().getValue().doubleValue();
        double e2 = el.getEccentricitySquared().doubleValue();

        double phi = geodetic.getLatitude().to(Units.RADIAN).getValue().doubleValue();
        double phi0 = Math.toRadians(params.latitudeOfOrigin);
        double lambda = geodetic.getLongitude().to(Units.RADIAN).getValue().doubleValue();
        double lambda0 = Math.toRadians(params.centralMeridian);

        if (Math.abs(phi) < 1e-10) {
            double x = params.falseEasting + a * (lambda - lambda0);
            double y = params.falseNorthing - calculateM(phi0, a, e2);
            return new PolyconicCoordinate(Real.of(x), Real.of(y), el, params);
        }

        double N = a / Math.sqrt(1 - e2 * Math.pow(Math.sin(phi), 2));
        double cotPhi = 1.0 / Math.tan(phi);
        double M = calculateM(phi, a, e2);
        double M0 = calculateM(phi0, a, e2);
        double E = (lambda - lambda0) * Math.sin(phi);

        double x = params.falseEasting + N * cotPhi * Math.sin(E);
        double y = params.falseNorthing + M - M0 + N * cotPhi * (1 - Math.cos(E));

        return new PolyconicCoordinate(Real.of(x), Real.of(y), el, params);
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

        double x = easting.doubleValue() - parameters.falseEasting;
        double y = northing.doubleValue() - parameters.falseNorthing;

        if (Math.abs(y + M0) < 1e-10) {
            double phi = 0;
            double lambda = lambda0 + x / a;
            return new GeodeticCoordinate(
                    Quantities.create(Math.toDegrees(phi), Units.DEGREE_ANGLE),
                    Quantities.create(Math.toDegrees(lambda), Units.DEGREE_ANGLE),
                    Quantities.create(0, Units.METER),
                    el);
        }

        // Iterative solution for phi
        double phi = phi0 + (y / a); // Initial guess
        for (int i = 0; i < 10; i++) {
            double M = calculateM(phi, a, e2);
            
            // Simplified iteration
            phi = phi + (y + M0 - M) / a; 
        }

        // More robust Polyconic inverse is complex, this is a placeholder for the logic
        double N = a / Math.sqrt(1 - e2 * Math.pow(Math.sin(phi), 2));
        double cotPhi = 1.0 / Math.tan(phi);
        double E = Math.asin(x / (N * cotPhi));
        double lambda = lambda0 + E / Math.sin(phi);

        return new GeodeticCoordinate(
                Quantities.create(Math.toDegrees(phi), Units.DEGREE_ANGLE),
                Quantities.create(Math.toDegrees(lambda), Units.DEGREE_ANGLE),
                Quantities.create(0, Units.METER),
                el);
    }
}

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
 * Represents a coordinate in the Bonne projection.
 * Historically used for the French "Carte de l'État-Major" and mapping of Europe/Asia.
 * It is an equal-area pseudoconic projection.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class BonneCoordinate implements EarthCoordinate, Serializable {

    private static final long serialVersionUID = 1L;

    private final Real easting;
    private final Real northing;
    private final ReferenceEllipsoid ellipsoid;
    private final BonneParameters parameters;

    public record BonneParameters(
            double standardParallel,
            double centralMeridian,
            double falseEasting,
            double falseNorthing) {
    }

    public BonneCoordinate(Real easting, Real northing, ReferenceEllipsoid ellipsoid, BonneParameters params) {
        this.easting = easting;
        this.northing = northing;
        this.ellipsoid = ellipsoid;
        this.parameters = params;
    }

    public Quantity<Length> getEasting() { return Quantities.create(easting.doubleValue(), Units.METER); }
    public Quantity<Length> getNorthing() { return Quantities.create(northing.doubleValue(), Units.METER); }

    @Override
    public String getCoordinateSystem() { return "Bonne"; }

    @Override
    public ReferenceEllipsoid getEllipsoid() { return ellipsoid; }

    @Override
    public ECEFCoordinate toECEF() { return toGeodetic().toECEF(); }

    public static BonneCoordinate fromGeodetic(GeodeticCoordinate geodetic, BonneParameters params) {
        ReferenceEllipsoid el = geodetic.getEllipsoid();
        double a = el.getSemiMajorAxis().getValue().doubleValue();
        double e2 = el.getEccentricitySquared().doubleValue();

        double phi = geodetic.getLatitude().to(Units.RADIAN).getValue().doubleValue();
        double phi1 = Math.toRadians(params.standardParallel);
        double lambda = geodetic.getLongitude().to(Units.RADIAN).getValue().doubleValue();
        double lambda0 = Math.toRadians(params.centralMeridian);

        double M = calculateM(phi, a, e2);
        double M1 = calculateM(phi1, a, e2);
        double m = Math.cos(phi) / Math.sqrt(1 - e2 * Math.pow(Math.sin(phi), 2));
        double m1 = Math.cos(phi1) / Math.sqrt(1 - e2 * Math.pow(Math.sin(phi1), 2));

        double rho = a * m1 / Math.sin(phi1) + M1 - M;
        double E = a * m * (lambda - lambda0) / rho;

        double x = params.falseEasting + rho * Math.sin(E);
        double y = params.falseNorthing + a * m1 / Math.sin(phi1) - rho * Math.cos(E);

        return new BonneCoordinate(Real.of(x), Real.of(y), el, params);
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

        double phi1 = Math.toRadians(parameters.standardParallel);
        double lambda0 = Math.toRadians(parameters.centralMeridian);

        double dx = easting.doubleValue() - parameters.falseEasting;
        double m1 = Math.cos(phi1) / Math.sqrt(1 - e2 * Math.pow(Math.sin(phi1), 2));
        double dy = a * m1 / Math.sin(phi1) + parameters.falseNorthing - northing.doubleValue();

        double rho = Math.sqrt(dx * dx + dy * dy);
        if (phi1 < 0) rho = -rho;
        
        double M1 = calculateM(phi1, a, e2);
        double M = a * m1 / Math.sin(phi1) + M1 - rho;
        
        // Iterative solution for phi from M
        double mu = M / (a * (1 - e2 / 4.0 - 3 * e2 * e2 / 64.0 - 5 * Math.pow(e2, 3) / 256.0));
        double e1 = (1 - Math.sqrt(1 - e2)) / (1 + Math.sqrt(1 - e2));
        double phi = mu + (3 * e1 / 2.0 - 27 * Math.pow(e1, 3) / 32.0) * Math.sin(2 * mu)
                + (21 * e1 * e1 / 16.0 - 55 * Math.pow(e1, 4) / 32.0) * Math.sin(4 * mu)
                + (151 * Math.pow(e1, 3) / 96.0) * Math.sin(6 * mu);

        double E = Math.atan2(dx, dy);
        double m = Math.cos(phi) / Math.sqrt(1 - e2 * Math.pow(Math.sin(phi), 2));
        double lambda = lambda0 + rho * E / (a * m);

        return new GeodeticCoordinate(
                Quantities.create(Math.toDegrees(phi), Units.DEGREE_ANGLE),
                Quantities.create(Math.toDegrees(lambda), Units.DEGREE_ANGLE),
                Quantities.create(0, Units.METER),
                el);
    }
}

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

import java.io.Serializable;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Length;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Lambert Conformal Conic (LCC) projection coordinate.
 * Widely used for aeronautical charts and regional maps.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class LambertConformalConicCoordinate implements EarthCoordinate, Serializable {

    private static final long serialVersionUID = 1L;

    private final Real easting;
    private final Real northing;
    private final LCCParams params;

    public static class LCCParams implements Serializable {
        private static final long serialVersionUID = 1L;
        public final double lat1; // Standard parallel 1 (radians)
        public final double lat2; // Standard parallel 2 (radians)
        public final double lat0; // Latitude of origin (radians)
        public final double lon0; // Longitude of origin (radians)
        public final double falseEasting;
        public final double falseNorthing;
        public final ReferenceEllipsoid ellipsoid;

        public LCCParams(double lat1Deg, double lat2Deg, double lat0Deg, double lon0Deg, double falseE, double falseN, ReferenceEllipsoid el) {
            this.lat1 = Math.toRadians(lat1Deg);
            this.lat2 = Math.toRadians(lat2Deg);
            this.lat0 = Math.toRadians(lat0Deg);
            this.lon0 = Math.toRadians(lon0Deg);
            this.falseEasting = falseE;
            this.falseNorthing = falseN;
            this.ellipsoid = el;
        }
    }

    public LambertConformalConicCoordinate(Real easting, Real northing, LCCParams params) {
        this.easting = easting;
        this.northing = northing;
        this.params = params;
    }

    public Quantity<Length> getEasting() { return Quantities.create(easting.doubleValue(), Units.METER); }
    public Quantity<Length> getNorthing() { return Quantities.create(northing.doubleValue(), Units.METER); }
    public LCCParams getParameters() { return params; }

    @Override
    public String getCoordinateSystem() { return "LCC"; }

    @Override
    public ReferenceEllipsoid getEllipsoid() { return params.ellipsoid; }

    @Override
    public GeodeticCoordinate toGeodetic() {
        // Inverse LCC projection (simplified)
        double a = params.ellipsoid.getSemiMajorAxis().to(Units.METER).getValue().doubleValue();
        double e2 = params.ellipsoid.getEccentricitySquared().doubleValue();
        double e = Math.sqrt(e2);
        
        double m1 = Math.cos(params.lat1) / Math.sqrt(1 - e2 * Math.pow(Math.sin(params.lat1), 2));
        double t1 = Math.tan(Math.PI/4 - params.lat1/2) / Math.pow((1 - e*Math.sin(params.lat1)) / (1 + e*Math.sin(params.lat1)), e/2);
        double t2 = Math.tan(Math.PI/4 - params.lat2/2) / Math.pow((1 - e*Math.sin(params.lat2)) / (1 + e*Math.sin(params.lat2)), e/2);
        double t0 = Math.tan(Math.PI/4 - params.lat0/2) / Math.pow((1 - e*Math.sin(params.lat0)) / (1 + e*Math.sin(params.lat0)), e/2);
        double m2 = Math.cos(params.lat2) / Math.sqrt(1 - e2 * Math.pow(Math.sin(params.lat2), 2));
        double n = Math.log(m1 / m2) / Math.log(t1 / t2);
        double F = m1 / (n * Math.pow(t1, n));
        double rho0 = a * F * Math.pow(t0, n);
        
        double dx = easting.doubleValue() - params.falseEasting;
        double dy = rho0 - northing.doubleValue() + params.falseNorthing;
        double rho = Math.signum(n) * Math.sqrt(dx*dx + dy*dy);
        double t = Math.pow(rho / (a * F), 1.0 / n);
        double theta = Math.atan2(dx, dy);
        
        // Iterative phi calculation
        double phi = Math.PI/2 - 2*Math.atan(t);
        for (int i = 0; i < 5; i++) {
            phi = Math.PI/2 - 2*Math.atan(t * Math.pow((1 - e*Math.sin(phi)) / (1 + e*Math.sin(phi)), e/2));
        }
        double lambda = params.lon0 + theta / n;
        
        return new GeodeticCoordinate(Math.toDegrees(phi), Math.toDegrees(lambda), 0.0);
    }

    @Override
    public ECEFCoordinate toECEF() { return toGeodetic().toECEF(); }

    /**
     * Converts Geodetic to LCC.
     */
    public static LambertConformalConicCoordinate fromGeodetic(GeodeticCoordinate geodetic, LCCParams p) {
        double lat = geodetic.getLatitude().to(Units.RADIAN).getValue().doubleValue();
        double lon = geodetic.getLongitude().to(Units.RADIAN).getValue().doubleValue();
        double a = p.ellipsoid.getSemiMajorAxis().to(Units.METER).getValue().doubleValue();
        double e2 = p.ellipsoid.getEccentricitySquared().doubleValue();
        double e = Math.sqrt(e2);

        double m1 = Math.cos(p.lat1) / Math.sqrt(1 - e2 * Math.pow(Math.sin(p.lat1), 2));
        double m2 = Math.cos(p.lat2) / Math.sqrt(1 - e2 * Math.pow(Math.sin(p.lat2), 2));
        
        double t = Math.tan(Math.PI/4 - lat/2) / Math.pow((1 - e*Math.sin(lat)) / (1 + e*Math.sin(lat)), e/2);
        double t1 = Math.tan(Math.PI/4 - p.lat1/2) / Math.pow((1 - e*Math.sin(p.lat1)) / (1 + e*Math.sin(p.lat1)), e/2);
        double t2 = Math.tan(Math.PI/4 - p.lat2/2) / Math.pow((1 - e*Math.sin(p.lat2)) / (1 + e*Math.sin(p.lat2)), e/2);
        double t0 = Math.tan(Math.PI/4 - p.lat0/2) / Math.pow((1 - e*Math.sin(p.lat0)) / (1 + e*Math.sin(p.lat0)), e/2);

        double n = Math.log(m1 / m2) / Math.log(t1 / t2);
        double F = m1 / (n * Math.pow(t1, n));
        double rho = a * F * Math.pow(t, n);
        double rho0 = a * F * Math.pow(t0, n);

        double theta = n * (lon - p.lon0);
        double x = p.falseEasting + rho * Math.sin(theta);
        double y = p.falseNorthing + rho0 - rho * Math.cos(theta);

        return new LambertConformalConicCoordinate(Real.of(x), Real.of(y), p);
    }

    @Override
    public String toString() {
        return String.format("LCC[E=%.2f, N=%.2f]", easting.doubleValue(), northing.doubleValue());
    }
}

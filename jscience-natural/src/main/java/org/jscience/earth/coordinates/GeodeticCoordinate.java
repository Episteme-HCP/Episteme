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
import org.jscience.measure.Unit;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Angle;
import org.jscience.measure.quantity.Length;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.VectorFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a point on the Earth defined by latitude, longitude, and height.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GeodeticCoordinate {

    private final Quantity<Angle> latitude;
    private final Quantity<Angle> longitude;
    private final Quantity<Length> height;
    private final ReferenceEllipsoid ellipsoid;

    public GeodeticCoordinate(Quantity<Angle> latitude, Quantity<Angle> longitude, Quantity<Length> height,
            ReferenceEllipsoid ellipsoid) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.height = height;
        this.ellipsoid = ellipsoid;
    }

    public GeodeticCoordinate(double latDeg, double lonDeg, double hMeters) {
        this(Quantities.create(latDeg, Units.DEGREE_ANGLE),
                Quantities.create(lonDeg, Units.DEGREE_ANGLE),
                Quantities.create(hMeters, Units.METER),
                ReferenceEllipsoid.WGS84);
    }

    public Quantity<Angle> getLatitude() {
        return latitude;
    }

    public Quantity<Angle> getLongitude() {
        return longitude;
    }

    public Quantity<Length> getHeight() {
        return height;
    }

    public ReferenceEllipsoid getEllipsoid() {
        return ellipsoid;
    }

    /**
     * Calculates the internal radius of curvature in the prime vertical (N).
     * N = a / sqrt(1 - e^2 * sin^2(phi))
     * 
     * @return N in meters
     */
    public Real getPrimeVerticalRadius() {
        double a = ellipsoid.getSemiMajorAxis().to(Units.METER).getValue().doubleValue();
        double e2 = ellipsoid.getEccentricitySquared().doubleValue();

        double phi = latitude.to(Units.RADIAN).getValue().doubleValue();
        double sinPhi = Math.sin(phi);
        double denom = Math.sqrt(1.0 - e2 * sinPhi * sinPhi);

        return Real.of(a / denom);
    }

    /**
     * Converts to Earth-Centered, Earth-Fixed (ECEF) Cartesian coordinates.
     * 
     * @return ECEFCoordinate [X, Y, Z]
     */
    public ECEFCoordinate toECEF() {
        double a = ellipsoid.getSemiMajorAxis().to(Units.METER).getValue().doubleValue();
        double e2 = ellipsoid.getEccentricitySquared().doubleValue();

        double phi = latitude.to(Units.RADIAN).getValue().doubleValue();
        double lambda = longitude.to(Units.RADIAN).getValue().doubleValue();
        double h = height.to(Units.METER).getValue().doubleValue();

        double sinPhi = Math.sin(phi);
        double cosPhi = Math.cos(phi);
        double sinLambda = Math.sin(lambda);
        double cosLambda = Math.cos(lambda);

        double N = a / Math.sqrt(1.0 - e2 * sinPhi * sinPhi);

        double x = (N + h) * cosPhi * cosLambda;
        double y = (N + h) * cosPhi * sinLambda;
        double z = (N * (1.0 - e2) + h) * sinPhi;

        return new ECEFCoordinate(Real.of(x), Real.of(y), Real.of(z), ellipsoid);
    }

    @Override
    public String toString() {
        return String.format("Geodetic[Lat=%.6f°, Lon=%.6f°, H=%.2fm, %s]", 
            latitude.to(Units.DEGREE_ANGLE).getValue().doubleValue(),
            longitude.to(Units.DEGREE_ANGLE).getValue().doubleValue(),
            height.to(Units.METER).getValue().doubleValue(),
            ellipsoid.getName());
    }
}



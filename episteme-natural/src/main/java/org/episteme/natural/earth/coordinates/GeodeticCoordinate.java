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
import org.episteme.core.measure.quantity.Angle;
import org.episteme.core.measure.quantity.Length;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Persistent;

/**
 * Represents a point on the Earth defined by latitude, longitude, and height.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class GeodeticCoordinate implements EarthCoordinate {

    @Attribute
    private final Quantity<Angle> latitude;
    @Attribute
    private final Quantity<Angle> longitude;
    @Attribute
    private final Quantity<Length> height;
    @Attribute
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

    /**
     * Creates a coordinate from latitude and longitude in degrees (height = 0).
     */
    public GeodeticCoordinate(double latDeg, double lonDeg) {
        this(latDeg, lonDeg, 0.0);
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
     * @return N as Real
     */
    public Real getPrimeVerticalRadius() {
        Real a = ellipsoid.getSemiMajorAxisValue();
        Real e2 = ellipsoid.getEccentricitySquared();

        double phi = latitude.to(Units.RADIAN).getValue().doubleValue();
        Real sinPhi = Real.of(Math.sin(phi));
        Real denom = Real.of(1.0).subtract(e2.multiply(sinPhi.pow(2))).sqrt();

        return a.divide(denom);
    }

    /**
     * Converts to Earth-Centered, Earth-Fixed (ECEF) Cartesian coordinates.
     * Uses Real for all intermediate products to prevent rounding drift.
     * 
     * @return ECEFCoordinate [X, Y, Z]
     */
    public ECEFCoordinate toECEF() {
        Real a = ellipsoid.getSemiMajorAxisValue();
        Real e2 = ellipsoid.getEccentricitySquared();

        double phi = latitude.to(Units.RADIAN).getValue().doubleValue();
        double lambda = longitude.to(Units.RADIAN).getValue().doubleValue();
        Real h = height.to(Units.METER).getValue();

        Real sinPhi = Real.of(Math.sin(phi));
        Real cosPhi = Real.of(Math.cos(phi));
        Real sinLambda = Real.of(Math.sin(lambda));
        Real cosLambda = Real.of(Math.cos(lambda));

        Real N = a.divide(Real.of(1.0).subtract(e2.multiply(sinPhi.pow(2))).sqrt());

        Real x = N.add(h).multiply(cosPhi).multiply(cosLambda);
        Real y = N.add(h).multiply(cosPhi).multiply(sinLambda);
        Real z = N.multiply(Real.of(1.0).subtract(e2)).add(h).multiply(sinPhi);

        return new ECEFCoordinate(x, y, z, ellipsoid);
    }

    @Override
    public String getCoordinateSystem() {
        return ellipsoid.getName();
    }

    @Override
    public GeodeticCoordinate toGeodetic() {
        return this;
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
        return "Geodetic Coordinate: " + toString();
    }

    @Override
    public String toString() {
        return String.format("Geodetic[Lat=%.6fÂ°, Lon=%.6fÂ°, H=%.2f, %s]", 
            latitude.to(Units.DEGREE_ANGLE).getValue().doubleValue(),
            longitude.to(Units.DEGREE_ANGLE).getValue().doubleValue(),
            height,
            ellipsoid.getName());
    }
}


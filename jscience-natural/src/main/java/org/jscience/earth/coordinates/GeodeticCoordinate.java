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
     */
    public Real getPrimeVerticalRadius() {
        Real a = Real.of(ellipsoid.getSemiMajorAxis().to(Units.METER).getValue().doubleValue());
        Real e2 = ellipsoid.getEccentricitySquared();

        // Need math functions for Real or generic Math interface.
        // Assuming Real.of(double) and standard Math for now, but ideally Real has its
        // own trigonometry.
        // If Real doesn't have sin(), we might need to cast to double, calculate, and
        // back.
        // Let's check Real methods later, but for now assuming standard interactions or
        // utilizing JDK Math for the double value.

        @SuppressWarnings("unchecked")
        Unit<Angle> rad = (Unit<Angle>) (Unit<?>) Units.RADIAN;

        double phi = latitude.to(rad).getValue().doubleValue();
        double sinPhi = Math.sin(phi);
        double denom = Math.sqrt(1.0 - e2.doubleValue() * sinPhi * sinPhi);

        return a.divide(Real.of(denom));
    }

    /**
     * Converts to Earth-Centered, Earth-Fixed (ECEF) Cartesian coordinates.
     */
    /**
     * Converts to Earth-Centered, Earth-Fixed (ECEF) Cartesian coordinates.
     * 
     * @return 3D Vector [X, Y, Z] in Meters
     */
    /**
     * Converts to Earth-Centered, Earth-Fixed (ECEF) Cartesian coordinates.
     * 
     * @return 3D Vector [X, Y, Z] in Meters
     */
    public Vector<Real> toECEF() {
        Real a = Real.of(ellipsoid.getSemiMajorAxis().to(Units.METER).getValue().doubleValue());
        Real e2 = ellipsoid.getEccentricitySquared();

        @SuppressWarnings("unchecked")
        Unit<Angle> rad = (Unit<Angle>) (Unit<?>) Units.RADIAN;

        double lat = latitude.to(rad).getValue().doubleValue();
        double lon = longitude.to(rad).getValue().doubleValue();
        double h = height.to(Units.METER).getValue().doubleValue();

        double sinLat = Math.sin(lat);
        double cosLat = Math.cos(lat);
        double sinLon = Math.sin(lon);
        double cosLon = Math.cos(lon);

        double N = a.doubleValue() / Math.sqrt(1.0 - e2.doubleValue() * sinLat * sinLat);

        double x = (N + h) * cosLat * cosLon;
        double y = (N + h) * cosLat * sinLon;
        double z = (N * (1.0 - e2.doubleValue()) + h) * sinLat;

        // Return Vector<Real> representing values in Meters
        List<Real> elements = new ArrayList<>();
        elements.add(Real.of(x));
        elements.add(Real.of(y));
        elements.add(Real.of(z));

        // We cast to Vector<Length> to satisfy the interface if strictly needed,
        // but physically it's Vector<Real> (numeric vector).
        // The javadoc says "@return 3D Vector [X, Y, Z] in Meters"
        // If the return type is Vector<Length>, we have a problem because
        // Vector<Length>
        // expects Length elements which aren't a Field.
        // I will change return type to Vector<Real> and update Javadoc.

        return VectorFactory.<Real>create(elements, Real.ZERO);
    }
}



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

import java.io.Serializable;
import org.episteme.core.measure.Quantity;
import org.episteme.core.measure.Quantities;
import org.episteme.core.measure.Units;
import org.episteme.core.measure.quantity.Length;
import org.episteme.core.mathematics.numbers.real.Real;

/**
 * Earth-Centered, Earth-Fixed (ECEF) Cartesian coordinate system.
 * This 3D coordinate system (X, Y, Z) has its origin at the center of the Earth.
 * X-axis points towards the intersection of the Prime Meridian and the Equator.
 * Y-axis points towards 90Â°E in the equatorial plane.
 * Z-axis points towards the North Pole.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class ECEFCoordinate implements EarthCoordinate, Serializable {

    private static final long serialVersionUID = 1L;

    private final Real x; // Meters
    private final Real y; // Meters
    private final Real z; // Meters
    private final ReferenceEllipsoid ellipsoid;

    public ECEFCoordinate(Real x, Real y, Real z) {
        this(x, y, z, ReferenceEllipsoid.WGS84);
    }

    public ECEFCoordinate(Real x, Real y, Real z, ReferenceEllipsoid ellipsoid) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.ellipsoid = ellipsoid;
    }

    public Quantity<Length> getX() { return Quantities.create(x.doubleValue(), Units.METER); }
    public Quantity<Length> getY() { return Quantities.create(y.doubleValue(), Units.METER); }
    public Quantity<Length> getZ() { return Quantities.create(z.doubleValue(), Units.METER); }
    
    public Real getXReal() { return x; }
    public Real getYReal() { return y; }
    public Real getZReal() { return z; }

    @Override
    public ReferenceEllipsoid getEllipsoid() { return ellipsoid; }

    @Override
    public String getCoordinateSystem() { return "ECEF"; }

    @Override
    public ECEFCoordinate toECEF() { return this; }

    /**
     * Calculates distance to another ECEF coordinate.
     */
    public Quantity<Length> distanceTo(ECEFCoordinate other) {
        double dx = x.doubleValue() - other.x.doubleValue();
        double dy = y.doubleValue() - other.y.doubleValue();
        double dz = z.doubleValue() - other.z.doubleValue();
        return Quantities.create(Math.sqrt(dx*dx + dy*dy + dz*dz), Units.METER);
    }

    /**
     * Converts ECEF to Geodetic coordinates using Heikkinen's closed-form algorithm.
     * Higher precision than iterative methods.
     */
    @Override
    public GeodeticCoordinate toGeodetic() {
        double a = ellipsoid.getSemiMajorAxis().to(Units.METER).getValue().doubleValue();
        double b = ellipsoid.getSemiMinorAxis().to(Units.METER).getValue().doubleValue();
        double e2 = ellipsoid.getEccentricitySquared().doubleValue();
        double ep2 = (a*a - b*b) / (b*b); // second eccentricity squared
        
        double xd = x.doubleValue();
        double yd = y.doubleValue();
        double zd = z.doubleValue();
        
        double p = Math.sqrt(xd*xd + yd*yd);
        double F = 54 * b*b * zd*zd;
        double G = p*p + (1 - e2)*zd*zd - e2*(a*a - b*b);
        double c = (e2*e2 * F * p*p) / (G*G*G);
        double s = Math.pow(1 + c + Math.sqrt(c*c + 2*c), 1.0/3.0);
        double k = s + 1 + 1/s;
        double P = F / (3 * k*k * G*G);
        double Q = Math.sqrt(1 + 2 * e2*e2 * P);
        double r0 = -(P * e2 * p) / (1 + Q) + Math.sqrt(0.5 * a*a * (1 + 1/Q) - (P * (1 - e2) * zd*zd) / (Q * (1 + Q)) - 0.5 * P * p*p);
        double U = Math.sqrt(Math.pow(p - e2*r0, 2) + zd*zd);
        double V = Math.sqrt(Math.pow(p - e2*r0, 2) + (1 - e2)*zd*zd);
        double z0 = (b*b * zd) / (a * V);
        
        double height = U * (1 - (b*b) / (a * V));
        double latitude = Math.atan((zd + ep2 * z0) / p);
        double longitude = Math.atan2(yd, xd);
        
        return new GeodeticCoordinate(
            Quantities.create(Math.toDegrees(latitude), Units.DEGREE_ANGLE),
            Quantities.create(Math.toDegrees(longitude), Units.DEGREE_ANGLE),
            Quantities.create(height, Units.METER),
            ellipsoid
        );
    }

    /**
     * Converts this ECEF coordinate to a standard 3D point.
     * @return the Point3D representation
     */
    public org.episteme.core.mathematics.geometry.Point3D toPoint3D() {
        return org.episteme.core.mathematics.geometry.Point3D.of(x, y, z);
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
        return "ECEF Coordinate: " + toString();
    }

    @Override
    public String toString() {
        return String.format("ECEF[X=%.2f, Y=%.2f, Z=%.2f]", x.doubleValue(), y.doubleValue(), z.doubleValue());
    }
}


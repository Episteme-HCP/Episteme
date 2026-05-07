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
import org.episteme.core.measure.quantity.Angle;
import org.episteme.core.measure.quantity.Length;

/**
 * Azimuth-Elevation-Range (AER) local spherical coordinate system.
 * Represents a point relative to a local observer's position.
 * Azimuth: Clockwise from North (0-360Â°).
 * Elevation: Angle above horizon (-90 to +90Â°).
 * Range: Slant distance to the target.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class AERCoordinate implements EarthCoordinate, Serializable {

    private static final long serialVersionUID = 1L;

    private final double azimuth; // Degrees
    private final double elevation; // Degrees
    private final double slantRange; // Meters
    private final GeodeticCoordinate observer;

    public AERCoordinate(double azimuth, double elevation, double slantRange, GeodeticCoordinate observer) {
        this.azimuth = azimuth;
        this.elevation = elevation;
        this.slantRange = slantRange;
        this.observer = observer;
    }

    public Quantity<Angle> getAzimuth() { return Quantities.create(azimuth, Units.DEGREE_ANGLE); }
    public Quantity<Angle> getElevation() { return Quantities.create(elevation, Units.DEGREE_ANGLE); }
    public Quantity<Length> getRange() { return Quantities.create(slantRange, Units.METER); }
    public GeodeticCoordinate getObserver() { return observer; }

    @Override
    public String getCoordinateSystem() { return "AER"; }

    @Override
    public ReferenceEllipsoid getEllipsoid() { return observer.getEllipsoid(); }

    @Override
    public GeodeticCoordinate toGeodetic() { return toENU().toGeodetic(); }

    @Override
    public ECEFCoordinate toECEF() { return toENU().toECEF(); }

    /**
     * Converts ENU coordinates to AER.
     */
    public static AERCoordinate fromENU(ENUCoordinate enu) {
        double e = enu.getEast().getValue().doubleValue();
        double n = enu.getNorth().getValue().doubleValue();
        double u = enu.getUp().getValue().doubleValue();

        double slantRange = Math.sqrt(e*e + n*n + u*u);
        double azimuth = Math.toDegrees(Math.atan2(e, n));
        if (azimuth < 0) azimuth += 360;
        
        double elevation = Math.toDegrees(Math.atan2(u, Math.sqrt(e*e + n*n)));

        return new AERCoordinate(azimuth, elevation, slantRange, enu.getReferencePoint());
    }

    /**
     * Converts this AER coordinate to ENU.
     */
    public ENUCoordinate toENU() {
        double azRad = Math.toRadians(azimuth);
        double elRad = Math.toRadians(elevation);
        
        double rCosEl = slantRange * Math.cos(elRad);
        
        double e = rCosEl * Math.sin(azRad);
        double n = rCosEl * Math.cos(azRad);
        double u = slantRange * Math.sin(elRad);

        return new ENUCoordinate(
            org.episteme.core.mathematics.numbers.real.Real.of(e), 
            org.episteme.core.mathematics.numbers.real.Real.of(n), 
            org.episteme.core.mathematics.numbers.real.Real.of(u), 
            observer
        );
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
        return "AER Coordinate: " + toString();
    }

    @Override
    public String toString() {
        return String.format("AER[Az=%.2fÂ°, El=%.2fÂ°, Range=%.2fm]", azimuth, elevation, slantRange);
    }
}


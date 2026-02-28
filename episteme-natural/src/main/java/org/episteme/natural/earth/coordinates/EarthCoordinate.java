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

import org.episteme.core.mathematics.geometry.GeometricObject;
import org.episteme.core.util.Positioned;
import org.episteme.core.measure.Quantity;
import org.episteme.core.measure.quantity.Length;

/**
 * Base interface for all Earth coordinate systems.
 * <p>
 * This interface unifies all coordinate representations (Geodetic, UTM, MGRS, ECEF, etc.)
 * under a common contract while implementing {@link Positioned} and {@link GeometricObject} 
 * for spatial operations and geometry engine integration.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface EarthCoordinate extends Positioned<EarthCoordinate>, GeometricObject<EarthCoordinate> {

    /**
     * Returns the coordinate system name (e.g., "WGS84", "UTM", "MGRS").
     * @return the coordinate system identifier
     */
    String getCoordinateSystem();

    /**
     * Converts this coordinate to geodetic (lat/lon/height) form.
     * @return the geodetic representation
     */
    GeodeticCoordinate toGeodetic();

    /**
     * Converts this coordinate to ECEF Cartesian form.
     * @return the ECEF representation
     */
    ECEFCoordinate toECEF();

    /**
     * Returns the reference ellipsoid used by this coordinate.
     * @return the ellipsoid model
     */
    ReferenceEllipsoid getEllipsoid();

    /**
     * Calculates the distance to another Earth coordinate.
     * @param other the target coordinate
     * @return distance as a Length quantity
     */
    default Quantity<Length> distanceTo(EarthCoordinate other) {
        return toECEF().distanceTo(other.toECEF());
    }

    /**
     * Returns this coordinate as the position (for Positioned interface).
     */
    @Override
    default EarthCoordinate getPosition() {
        return this;
    }

    @Override
    default int dimension() {
        return 0; // Coordinates are points (0D)
    }

    @Override
    default int ambientDimension() {
        return 3; // Earth coordinates exist in 3D space
    }

    @Override
    default String description() {
        return getCoordinateSystem() + " Coordinate: " + toString();
    }
}


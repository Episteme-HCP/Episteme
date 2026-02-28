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

package org.episteme.social.geography;

import org.episteme.natural.earth.coordinates.GeodeticCoordinate;
import org.episteme.core.mathematics.geometry.Vector2D;
import org.episteme.core.measure.Units;

/**
 * Handles Geographic Information System (GIS) projections and spatial offsets.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GISProfile {

    public enum Projection {
        MERCATOR, EQUIRECTANGULAR, ORTHOGRAPHIC
    }

    private final Projection projection;

    public GISProfile(Projection projection) {
        this.projection = projection;
    }

    /**
     * Projects a geodetic coordinate to a 2D plane coordinate system.
     * 
     * @param coord the earth coordinate
     * @return 2D vector [x, y] in projected space
     */
    public Vector2D project(GeodeticCoordinate coord) {
        double latRad = coord.getLatitude().to(Units.RADIAN).getValue().doubleValue();
        double lonRad = coord.getLongitude().to(Units.RADIAN).getValue().doubleValue();

        double x, y;
        switch (projection) {
            case MERCATOR:
                x = lonRad;
                y = Math.log(Math.tan(Math.PI / 4 + latRad / 2));
                break;
            case ORTHOGRAPHIC:
                x = Math.cos(latRad) * Math.sin(lonRad);
                y = Math.sin(latRad);
                break;
            default:
                // Equirectangular
                x = lonRad;
                y = latRad;
        }
        return new Vector2D(x, y);
    }

    public Projection getProjection() {
        return projection;
    }

    /**
     * Calculates the approximate distance in meters between two geodetic coordinates 
     * using the Haversine formula (spherical earth approximation).
     */
    public static double calculateDistanceMeters(GeodeticCoordinate c1, GeodeticCoordinate c2) {
        double lat1 = c1.getLatitude().to(Units.RADIAN).getValue().doubleValue();
        double lon1 = c1.getLongitude().to(Units.RADIAN).getValue().doubleValue();
        double lat2 = c2.getLatitude().to(Units.RADIAN).getValue().doubleValue();
        double lon2 = c2.getLongitude().to(Units.RADIAN).getValue().doubleValue();

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(lat1) * Math.cos(lat2) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return 6371000.0 * c; // Earth radius in meters
    }
}


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

package org.jscience.geography;

import org.jscience.earth.coordinates.GeodeticCoordinate;
import org.jscience.mathematics.geometry.Vector2D;

import org.jscience.measure.Units;

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
}

/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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

import org.jscience.mathematics.numbers.real.Real;

/**
 * Handles Geographic Information System (GIS) profiles and projections.
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
     * Projects a 3D coordinate (lat/lon) to a 2D map coordinate (x/y).
     * 
     * @return Vector<Real> with [x, y] map coordinates
     */
    public org.jscience.mathematics.linearalgebra.Vector<Real> project(Coordinate coord) {
        Real lat = coord.getLatitude().toRadians();
        Real lon = coord.getLongitude().toRadians();

        java.util.List<Real> coords = new java.util.ArrayList<>();

        switch (projection) {
            case MERCATOR:
                Real x = lon;
                Real y = Real.PI.divide(Real.of(4)).add(lat.divide(Real.TWO)).tan().log();
                coords.add(x);
                coords.add(y);
                break;
            case EQUIRECTANGULAR:
                coords.add(lon);
                coords.add(lat);
                break;
            default:
                throw new UnsupportedOperationException("Projection not implemented");
        }
        return org.jscience.mathematics.linearalgebra.vectors.DenseVector.of(coords, Real.ZERO);
    }

    /**
     * Calculates great-circle distance (Haversine) in meters.
     */
    public static Real calculateDistance(Coordinate c1, Coordinate c2) {
        Real R = Real.of(6371000); // Earth radius in meters
        Real lat1 = c1.getLatitude().toRadians();
        Real lat2 = c2.getLatitude().toRadians();
        Real dLat = c2.getLatitude().subtract(c1.getLatitude()).toRadians();
        Real dLon = c2.getLongitude().subtract(c1.getLongitude()).toRadians();

        Real sinDLatHalf = dLat.divide(Real.TWO).sin();
        Real sinDLonHalf = dLon.divide(Real.TWO).sin();

        Real a = sinDLatHalf.pow(2).add(lat1.cos().multiply(lat2.cos()).multiply(sinDLonHalf.pow(2)));
        Real sqrtA = a.sqrt();
        Real sqrtOneMinusA = Real.ONE.subtract(a).sqrt();
        Real c = Real.TWO.multiply(sqrtA.atan2(sqrtOneMinusA));

        return R.multiply(c);
    }
}

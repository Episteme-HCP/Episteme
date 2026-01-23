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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Utility for computing the convex hull of a set of coordinates.
 * Implements the Monotone Chain algorithm.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.0
 * @since 2.0
 */
public final class ConvexHull {

    private ConvexHull() {}

    /**
     * Computes the convex hull of a set of coordinates.
     *
     * @param points the input coordinates
     * @return the coordinates defining the convex hull in counter-clockwise order
     */
    public static Coordinate[] compute(Coordinate[] points) {
        if (points == null || points.length <= 2) {
            return points != null ? Arrays.copyOf(points, points.length) : new Coordinate[0];
        }

        // Sort points by longitude, then latitude
        Coordinate[] sorted = Arrays.copyOf(points, points.length);
        Arrays.sort(sorted, (a, b) -> {
            int cmp = Double.compare(a.getLongitudeDegrees().doubleValue(), 
                                   b.getLongitudeDegrees().doubleValue());
            if (cmp != 0) return cmp;
            return Double.compare(a.getLatitudeDegrees().doubleValue(), 
                                b.getLatitudeDegrees().doubleValue());
        });

        int n = sorted.length;
        Coordinate[] hull = new Coordinate[2 * n];
        int k = 0;

        // Build lower hull
        for (int i = 0; i < n; i++) {
            while (k >= 2 && crossProduct(hull[k - 2], hull[k - 1], sorted[i]) <= 0) {
                k--;
            }
            hull[k++] = sorted[i];
        }

        // Build upper hull
        for (int i = n - 2, t = k + 1; i >= 0; i--) {
            while (k >= t && crossProduct(hull[k - 2], hull[k - 1], sorted[i]) <= 0) {
                k--;
            }
            hull[k++] = sorted[i];
        }

        return Arrays.copyOfRange(hull, 0, k - 1);
    }

    /**
     * Creates a Boundary representing the convex hull of a set of coordinates.
     *
     * @param points the points to enclose
     * @return a new Boundary object
     */
    public static Boundary createBoundary(Coordinate[] points) {
        return new Boundary(compute(points));
    }

    private static double crossProduct(Coordinate a, Coordinate b, Coordinate c) {
        double ax = a.getLongitudeDegrees().doubleValue();
        double ay = a.getLatitudeDegrees().doubleValue();
        double bx = b.getLongitudeDegrees().doubleValue();
        double by = b.getLatitudeDegrees().doubleValue();
        double cx = c.getLongitudeDegrees().doubleValue();
        double cy = c.getLatitudeDegrees().doubleValue();
        return (bx - ax) * (cy - ay) - (by - ay) * (cx - ax);
    }
}

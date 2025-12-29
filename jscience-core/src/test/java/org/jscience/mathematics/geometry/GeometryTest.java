/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.mathematics.geometry;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GeometryTest {

    private static final double TOLERANCE = 1e-9;

    @Test
    public void testVector3DCrossProduct() {
        Vector3D v1 = new Vector3D(1, 0, 0);
        Vector3D v2 = new Vector3D(0, 1, 0);
        Vector3D v3 = v1.cross(v2);

        assertEquals(0.0, v3.getX(), TOLERANCE);
        assertEquals(0.0, v3.getY(), TOLERANCE);
        assertEquals(1.0, v3.getZ(), TOLERANCE);
    }

    @Test
    public void testLinePlaneIntersection() {
        // Plane z=0
        Plane3D plane = new Plane3D(new Vector3D(0, 0, 0), new Vector3D(0, 0, 1));

        // Line along z-axis
        Line3D line = new Line3D(new Vector3D(0, 0, 10), new Vector3D(0, 0, -1));

        Vector3D intersection = plane.intersection(line);
        assertNotNull(intersection);
        assertEquals(0.0, intersection.getX(), TOLERANCE);
        assertEquals(0.0, intersection.getY(), TOLERANCE);
        assertEquals(0.0, intersection.getZ(), TOLERANCE);

        // Parallel line
        Line3D parallel = new Line3D(new Vector3D(0, 0, 1), new Vector3D(1, 0, 0));
        assertNull(plane.intersection(parallel));
    }

    @Test
    public void testPlanePlaneIntersection() {
        // Plane z=0
        Plane3D p1 = new Plane3D(new Vector3D(0, 0, 0), new Vector3D(0, 0, 1));
        // Plane x=0
        Plane3D p2 = new Plane3D(new Vector3D(0, 0, 0), new Vector3D(1, 0, 0));

        Line3D intersection = p1.intersection(p2);
        assertNotNull(intersection);

        // Direction should be y-axis (0, 1, 0) or (0, -1, 0)
        Vector3D dir = intersection.getDirection();
        assertEquals(0.0, dir.getX(), TOLERANCE);
        assertEquals(0.0, dir.getZ(), TOLERANCE);
        assertEquals(1.0, Math.abs(dir.getY()), TOLERANCE);

        // Point should be on both planes (e.g. origin)
        Vector3D pt = intersection.getPoint();
        assertEquals(0.0, pt.getX(), TOLERANCE);
        assertEquals(0.0, pt.getZ(), TOLERANCE);
    }

    @Test
    public void testLineLineIntersection() {
        // Line 1: x-axis
        Line3D l1 = new Line3D(new Vector3D(0, 0, 0), new Vector3D(1, 0, 0));
        // Line 2: y-axis
        Line3D l2 = new Line3D(new Vector3D(0, 0, 0), new Vector3D(0, 1, 0));

        Vector3D intersection = l1.intersection(l2);
        assertNotNull(intersection);
        assertEquals(0.0, intersection.getX(), TOLERANCE);
        assertEquals(0.0, intersection.getY(), TOLERANCE);
        assertEquals(0.0, intersection.getZ(), TOLERANCE);

        // Skew lines
        // L3: z=1, parallel to x
        Line3D l3 = new Line3D(new Vector3D(0, 0, 1), new Vector3D(1, 0, 0));
        assertNull(l1.intersection(l3));

        // Closest points
        @SuppressWarnings("unused")
        Vector3D c1 = l1.closestPoint(l3);
        @SuppressWarnings("unused")
        Vector3D c3 = l3.closestPoint(l1);

        // Closest point on L1 to L3 should be (0,0,0) if we consider projection?
        // Actually any point on L1 has z=0, L3 has z=1. Distance is 1.
        // But they are parallel.

        // Let's try non-parallel skew lines
        // L4: along y at z=1
        Line3D l4 = new Line3D(new Vector3D(0, 0, 1), new Vector3D(0, 1, 0));

        // Closest point on L1 (x-axis) to L4 (y-axis at z=1) should be origin
        Vector3D cp1 = l1.closestPoint(l4);
        assertEquals(0.0, cp1.getX(), TOLERANCE);
        assertEquals(0.0, cp1.getY(), TOLERANCE);
        assertEquals(0.0, cp1.getZ(), TOLERANCE);

        // Closest point on L4 to L1 should be (0,0,1)
        Vector3D cp4 = l4.closestPoint(l1);
        assertEquals(0.0, cp4.getX(), TOLERANCE);
        assertEquals(0.0, cp4.getY(), TOLERANCE);
        assertEquals(1.0, cp4.getZ(), TOLERANCE);
    }

    @Test
    public void testLine2DIntersection() {
        // Line 1: y=x
        Line2D l1 = new Line2D(Point2D.of(0, 0), Vector2D.of(1, 1));
        // Line 2: y=-x + 2
        Line2D l2 = new Line2D(Point2D.of(0, 2), Vector2D.of(1, -1));

        Point2D intersection = l1.intersection(l2);
        assertNotNull(intersection);
        assertEquals(1.0, intersection.getX().doubleValue(), TOLERANCE);
        assertEquals(1.0, intersection.getY().doubleValue(), TOLERANCE);

        // Parallel
        Line2D l3 = new Line2D(Point2D.of(0, 1), Vector2D.of(1, 1));
        assertNull(l1.intersection(l3));
    }

    @Test
    public void testInterval() {
        Vector3D start = new Vector3D(0, 0, 0);
        Vector3D end = new Vector3D(10, 0, 0);
        Segment3D segment = new Segment3D(start, end);

        assertTrue(segment.contains(new Vector3D(5, 0, 0), TOLERANCE));
        assertFalse(segment.contains(new Vector3D(11, 0, 0), TOLERANCE));

        // Clipping
        Vector3D min = new Vector3D(2, -1, -1);
        Vector3D max = new Vector3D(8, 1, 1);

        Segment3D clipped = segment.clip(min, max);
        assertNotNull(clipped);
        assertEquals(2.0, clipped.getStart().getX(), TOLERANCE);
        assertEquals(8.0, clipped.getEnd().getX(), TOLERANCE);
    }
}

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

package org.jscience.chemistry.gui.extended.geometry;

/**
 * A class that defines geometric straight line in 3D space
 *
 * @author Zhidong Xie (zxie
 */
public class Line {
    /** a point on the line */
    Point3D pointA;

    /** the other point on the line */
    Point3D pointB;

/**
     * Default constructor
     */
    public Line() {
        pointA = new Point3D(0.0, 0.0, 0.0);
        pointB = new Point3D(0.0, 0.0, 0.0);
    }

/**
     * Full constructor
     *
     * @param pointA a point on the line
     * @param pointB the other point on the line
     */
    public Line(Point3D pointA, Point3D pointB) {
        this.pointA = new Point3D(pointA);
        this.pointB = new Point3D(pointB);
    }

    /**
     * Return the geometric vector from some point on the line to the
     * given point so that the vector is perpendicular to the line. Assume the
     * given point is not on the line
     *
     * @param pointX the give point
     *
     * @return DOCUMENT ME!
     */
    public GeoVector3D vectorTo(Point3D pointX) {
        GeoVector3D ap = new GeoVector3D(pointA, pointX);
        GeoVector3D ab = new GeoVector3D(pointA, pointB);
        GeoVector3D ap1 = GeometryUtils.project(ap, ab);
        ap.minus(ap1);

        return ap;
    }

    /**
     * Return the distance from a given point to the line
     *
     * @param pointX DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double distanceTo(Point3D pointX) {
        return (vectorTo(pointX)).length();
    }
} //end of Line class

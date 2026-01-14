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
 * A public class that provides mathematical calculation on
 * some geometry entities.
 *
 * @author Zhidong Xie (zxie@tripos.com)
 *         Original Version
 * @date 8/14/97
 * @see Point3D
 * @see GeoVector
 */
public class GeometryUtils {
    /**
     * calculate distance between two points in 3D space
     *
     * @param P1 :  point 1
     * @param P2 :  point 2
     * @return distance  : |P1P2|
     */
    public static double distance(Point3D P1, Point3D P2) {
        return P1.distanceTo(P2);
    }

    /**
     * calculate angle among three points in 3D space
     * note: calling Point3D.distSquare() is more efficient than Point3D.distance().
     *
     * @param x1, y1, z1 : coordinates of point P1
     * @param x2, y2, z2 : coordinates of point O
     * @param x3, y3, z3 : coordinates of point P2
     * @return angle : angle P1-O-P2
     */
    public static Angle angle(Point3D P1, Point3D O, Point3D P2) {
        return O.angleWith(P1, P2);
    }

    /**
     * calculate angle among three points in 3D space
     * note: calling Point3D.distSquare() is more efficient than
     * calling Point3D.distance().
     *
     * @param x1, y1, z1 : coordinates of point P1
     * @param x2, y2, z2 : coordinates of point O
     * @param x3, y3, z3 : coordinates of point P2
     * @return angle : angle P1-O-P2
     */
    public static Angle torsionAngle(Point3D P1, Point3D P2, Point3D P3,
                                     Point3D P4) {
        Angle result = new Angle(0.0);

        // use same local variable names as in $TA_TOOLS/utl/source/utl_geom.c
        GeoVector3D tv1 = new GeoVector3D(P1, P2);
        GeoVector3D tv2 = new GeoVector3D(P2, P3);
        GeoVector3D tv3 = new GeoVector3D(P3, P4);

        // error checking: distance is too small?
        if (tv1.length() <= 0.001) {
            result.setComment("Error: distance between point 1 and point" +
                    "2 is too small to ensure accurate torsion angle calculation");

            return result;
        }

        if (tv2.length() <= 0.001) {
            result.setComment("Error: distance between point 2 and point" +
                    "3 is too small to ensure accurate torsion angle calculation");

            return result;
        }

        if (tv3.length() <= 0.001) {
            result.setComment("Error: distance between point 3 and point" +
                    "4 is too small to ensure accurate torsion angle calculation");

            return result;
        }

        // error checking: co-linear?
        double angleP1P2P3 = P2.angleWith(P1, P3).degreeValue();

        if (((174.9 <= angleP1P2P3) && (angleP1P2P3 <= 180.1)) ||
                ((-180.1 <= angleP1P2P3) && (angleP1P2P3 <= -174.9))) {
            result.setComment("Error: point 1, 2, and 3 are co-linear, hence " +
                    "it is meaningless to calculate torsion angle");

            return result;
        }

        double angleP2P3P4 = P3.angleWith(P2, P4).degreeValue();

        if (((174.9 <= angleP2P3P4) && (angleP2P3P4 <= 180.1)) ||
                ((-180.1 <= angleP2P3P4) && (angleP2P3P4 <= -174.9))) {
            result.setComment("Error: point 2, 3, and 4 are co-linear, hence " +
                    "it is meaningless to calculate torsion angle");

            return result;
        }

        GeoVector3D tva = tv1.cross(tv2);
        GeoVector3D tvb = tv2.cross(tv3);

        tva.normalize();
        tvb.normalize();

        // tva "dot" tvb gives cosine torsionAngle
        double ct = tva.dot(tvb);

        // tva "cross" tvb gives a vector V which is parallel or antiparallel
        // to tv2, and V = sin( torsionAngle ) r, where r is the unit vector
        // of tv2
        GeoVector3D ttvc = tva.cross(tvb);

        // if V is parallel to tv2, sin( torsionAngle) is >= 0 else < 0.
        // In Java, the following value relationship holds:
        //    if sin( anyAngle ) < 0, anyAngle == - acos( cos( anyAngle ) )
        //    else anyAngle == acos( cos( anyAngle ) ).
        if (ttvc.dot(tv2) < 0) { // antiparallel
            result.setValue(-Math.acos(ct));
        } else { // parallel
            result.setValue(Math.acos(ct));
        }

        result.setComment(null);

        return result;
    }

    /**
     * Return a geometric vector that is the result of summation of two
     * input geometric vectors
     *
     * @param va first geometric vector
     * @param vb second geometric vector
     * @return va+vb
     */
    public static GeoVector3D sum(GeoVector3D va, GeoVector3D vb) {
        GeoVector3D result = new GeoVector3D(va);
        result.add(vb);

        return result;
    }

    /**
     * Return a geometric vector that is the result of subtracting second input
     * geometric vector from the first one.
     *
     * @param va first geometric vector
     * @param vb second geometric vector
     * @return va-vb
     */
    public static GeoVector3D subtract(GeoVector3D va, GeoVector3D vb) {
        GeoVector3D result = new GeoVector3D(va);
        result.minus(vb);

        return result;
    }

    /**
     * return the projection of va on vb
     */
    public static GeoVector3D project(GeoVector3D va, GeoVector3D vb) {
        GeoVector3D v2 = new GeoVector3D(vb);
        v2.normalize();
        v2.scale(va.dot(v2));

        return v2;
    }
} // end of Geometry class

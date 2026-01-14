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

package org.jscience.mathematics.geometry;

/**
 * A class representing common perimeters, areas, volumes, center of mass
 * (barycenter or gravity center) for various polygons.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class CenterOfMasses extends Object {
    /**
     * Gets barycenter for a group of masses and their associated
     * weight
     *
     * @param positions DOCUMENT ME!
     * @param masses DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Point2D getBarycenter(Point2D[] positions, double[] masses) {
        if (positions.length == masses.length) {
            double resultx = 0;
            double resulty = 0;

            for (int i = 0; i < positions.length; i++) {
                resultx += (positions[i].x() * masses[i]);
                resulty += (positions[i].y() * masses[i]);
            }

            resultx = resultx / positions.length;
            resulty = resulty / positions.length;

            return new CartesianPoint2D(resultx, resulty);
        } else {
            return null;
        }
    }

    /**
     * Gets barycenter for a group of masses and their associated
     * weight
     *
     * @param positions DOCUMENT ME!
     * @param masses DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Point3D getBarycenter(Point3D[] positions, double[] masses) {
        Point3D result;

        if (positions.length == masses.length) {
            double resultx = 0;
            double resulty = 0;
            double resultz = 0;

            for (int i = 0; i < positions.length; i++) {
                resultx += (positions[i].x() * masses[i]);
                resulty += (positions[i].y() * masses[i]);
                resultz += (positions[i].z() * masses[i]);
            }

            resultx = resultx / positions.length;
            resulty = resulty / positions.length;
            resultz = resultz / positions.length;

            return new CartesianPoint3D(resultx, resulty, resultz);
        } else {
            return null;
        }
    }

    //see http://www.math.com/tables/tables.htm
    /**
     * Gets perimeter for a square
     *
     * @param edge DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getSquarePerimeter(double edge) {
        return 4 * edge;
    }

    /**
     * Gets perimeter for a rectangle
     *
     * @param shortEdge DOCUMENT ME!
     * @param longEdge DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getRectanglePerimeter(double shortEdge, double longEdge) {
        return (2 * shortEdge) + (2 * longEdge);
    }

    /**
     * Gets perimeter for a triangle
     *
     * @param edge1 DOCUMENT ME!
     * @param edge2 DOCUMENT ME!
     * @param edge3 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getTrianglePerimeter(double edge1, double edge2,
        double edge3) {
        return edge1 + edge2 + edge3;
    }

    /**
     * Gets perimeter for a circle
     *
     * @param radius DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getCirclePerimeter(double radius) {
        return 2 * Math.PI * radius;
    }

    /**
     * Gets surface for a square
     *
     * @param edge DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getSquareSurface(double edge) {
        return edge * edge;
    }

    /**
     * Gets surface for a rectangle
     *
     * @param shortEdge DOCUMENT ME!
     * @param longEdge DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getRectangleSurface(double shortEdge, double longEdge) {
        return shortEdge * longEdge;
    }

    /**
     * Gets surface for a parallelogram
     *
     * @param base DOCUMENT ME!
     * @param height DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getParallelogramSurface(double base, double height) {
        return base * height;
    }

    /**
     * Gets surface for a trapezoid
     *
     * @param longEdge DOCUMENT ME!
     * @param shortEdge DOCUMENT ME!
     * @param height DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getTrapezoidSurface(double longEdge, double shortEdge,
        double height) {
        return ((longEdge + shortEdge) * height) / 2;
    }

    /**
     * Gets surface for a triangle
     *
     * @param base DOCUMENT ME!
     * @param height DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getTriangleSurface(double base, double height) {
        return (base * height) / 2;
    }

    /**
     * Gets surface for a circle
     *
     * @param radius DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getCircleSurface(double radius) {
        return Math.PI * radius * radius;
    }

    /**
     * Gets surface for a ellipse
     *
     * @param shortRadius DOCUMENT ME!
     * @param longRadius DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getEllipseSurface(double shortRadius, double longRadius) {
        return Math.PI * shortRadius * longRadius;
    }

    //3D objects
    /**
     * Gets volume for a cube
     *
     * @param edge DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getCubeSurface(double edge) {
        return 6 * edge * edge;
    }

    /**
     * Gets volume for a cube
     *
     * @param edge DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getCubeVolume(double edge) {
        return edge * edge * edge;
    }

    /**
     * Gets surface for a rectangular prism
     *
     * @param edge1 DOCUMENT ME!
     * @param edge2 DOCUMENT ME!
     * @param edge3 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getRectangularPrismSurface(double edge1, double edge2,
        double edge3) {
        return (2 * edge1 * edge2) + (2 * edge1 * edge3) + (2 * edge2 * edge3);
    }

    /**
     * Gets volume for a rectangular prism
     *
     * @param edge1 DOCUMENT ME!
     * @param edge2 DOCUMENT ME!
     * @param edge3 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getRectangularPrismVolume(double edge1, double edge2,
        double edge3) {
        return edge1 * edge2 * edge3;
    }

    /**
     * Gets surface for a cylinder
     *
     * @param radius DOCUMENT ME!
     * @param height DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getCylinderSurface(double radius, double height) {
        return (2 * Math.PI * radius * height) +
        (2 * Math.PI * radius * radius);
    }

    /**
     * Gets volume for a cylinder
     *
     * @param radius DOCUMENT ME!
     * @param height DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getCylinderVolume(double radius, double height) {
        return Math.PI * radius * radius * height;
    }

    /**
     * Gets surface for a cone
     *
     * @param radius DOCUMENT ME!
     * @param height DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getConeSurface(double radius, double height) {
        return (Math.sqrt((radius * radius) + (height * height)) + radius) * Math.PI * radius;
    }

    /**
     * Gets volume for a cone
     *
     * @param radius DOCUMENT ME!
     * @param height DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getConeVolume(double radius, double height) {
        return (Math.PI * radius * radius * height) / 3;
    }

    /**
     * Gets surface for a sphere
     *
     * @param radius DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getSphereSurface(double radius) {
        return 4 * Math.PI * radius * radius;
    }

    /**
     * Gets volume for a sphere
     *
     * @param radius DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getSphereVolume(double radius) {
        return 4 / 3 * Math.PI * radius * radius * radius;
    }

    /**
     * Gets surface for an ellipsoid
     *
     * @param radius1 DOCUMENT ME!
     * @param radius2 DOCUMENT ME!
     * @param radius3 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    //public static double getEllipsoidSurface(double radius1, double radius2, double radius3) {
    //    return I don't even understand the formula;
    //}
    /**
     * Gets volume for an ellipsoid
     *
     * @param radius1 DOCUMENT ME!
     * @param radius2 DOCUMENT ME!
     * @param radius3 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getEllipsoidVolume(double radius1, double radius2,
        double radius3) {
        return 4 / 3 * Math.PI * radius1 * radius2 * radius3;
    }

    /**
     * Gets surface for a pyramid
     *
     * @param base DOCUMENT ME!
     * @param height DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getPyramidSurface(double base, double height) {
        return base * base * (1 + height);
    }

    /**
     * Gets volume for a pyramid
     *
     * @param base DOCUMENT ME!
     * @param height DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getPyramidVolume(double base, double height) {
        return (base * base * height) / 3;
    }

    /**
     * Gets surface for a regular tetrahedron
     *
     * @param edge DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getTetrahedronSurface(double edge) {
        return Math.sqrt(3) * edge * edge;
    }

    /**
     * Gets volume for a regular tetrahedron
     *
     * @param edge DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getTetrahedronVolume(double edge) {
        return (Math.sqrt(2) * edge * edge * edge) / 12;
    }
}

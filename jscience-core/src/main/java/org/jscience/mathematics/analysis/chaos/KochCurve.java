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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.mathematics.analysis.chaos;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.geometry.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Koch Curve (Snowflake) - a fractal curve.
 * <p>
 * The Koch curve is constructed by recursive subdivision:
 * <ol>
 * <li>Start with a line segment</li>
 * <li>Divide it into thirds</li>
 * <li>Replace the middle third with two sides of an equilateral triangle</li>
 * <li>Repeat for each segment</li>
 * </ol>
 * </p>
 * 
 * <h2>Properties</h2>
 * <ul>
 * <li>Hausdorff dimension: log(4)/log(3) ≈ 1.2619</li>
 * <li>Infinite length in finite area</li>
 * <li>Continuous but nowhere differentiable</li>
 * <li>Self-similar at all scales</li>
 * </ul>
 * 
 * <h2>References</h2>
 * <ul>
 * <li>von Koch, Helge (1904). "Sur une courbe continue sans tangente"</li>
 * <li>Mandelbrot, B. (1982). "The Fractal Geometry of Nature"</li>
 * </ul>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class KochCurve {

    /**
     * Generates the Koch curve up to a given depth.
     * 
     * @param start starting point
     * @param end   ending point
     * @param depth recursion depth (0 = line segment)
     * @return list of points forming the curve
     */
    public static List<Point2D> generate(Point2D start, Point2D end, int depth) {
        List<Point2D> points = new ArrayList<>();
        points.add(start);
        generateRecursive(start, end, depth, points);
        points.add(end);
        return points;
    }

    private static void generateRecursive(Point2D p1, Point2D p5, int depth, List<Point2D> points) {
        if (depth == 0) {
            return;
        }

        // Calculate intermediate points
        Real dx = p5.getX().subtract(p1.getX());
        Real dy = p5.getY().subtract(p1.getY());

        // p2 = p1 + (p5 - p1) / 3
        Real p2x = p1.getX().add(dx.divide(Real.of(3)));
        Real p2y = p1.getY().add(dy.divide(Real.of(3)));
        Point2D p2 = Point2D.of(p2x, p2y);

        // p4 = p1 + 2(p5 - p1) / 3
        Real p4x = p1.getX().add(dx.multiply(Real.of(2)).divide(Real.of(3)));
        Real p4y = p1.getY().add(dy.multiply(Real.of(2)).divide(Real.of(3)));
        Point2D p4 = Point2D.of(p4x, p4y);

        // p3 = top of equilateral triangle (60° rotation)
        Real midx = p2x.add(p4x).divide(Real.of(2));
        Real midy = p2y.add(p4y).divide(Real.of(2));
        Real dxSquared = dx.multiply(dx);
        Real dySquared = dy.multiply(dy);
        Real height = dxSquared.add(dySquared).sqrt().divide(Real.of(2).multiply(Real.of(Math.sqrt(3))));

        // Perpendicular vector
        Real perpX = dy.negate();
        Real perpY = dx;
        Real perpXSquared = perpX.multiply(perpX);
        Real perpYSquared = perpY.multiply(perpY);
        Real perpLen = perpXSquared.add(perpYSquared).sqrt();

        Real p3x = midx.add(perpX.multiply(height).divide(perpLen));
        Real p3y = midy.add(perpY.multiply(height).divide(perpLen));
        Point2D p3 = Point2D.of(p3x, p3y);

        // Recursively subdivide
        generateRecursive(p1, p2, depth - 1, points);
        points.add(p2);
        generateRecursive(p2, p3, depth - 1, points);
        points.add(p3);
        generateRecursive(p3, p4, depth - 1, points);
        points.add(p4);
        generateRecursive(p4, p5, depth - 1, points);
    }

    /**
     * Generates a Koch snowflake (3 Koch curves forming a triangle).
     * 
     * @param center center of the snowflake
     * @param radius radius of the enclosing circle
     * @param depth  recursion depth
     * @return list of points forming the snowflake
     */
    public static List<Point2D> generateSnowflake(Point2D center, double radius, int depth) {
        // Create equilateral triangle vertices
        double angle1 = Math.PI / 2; // Top vertex
        double angle2 = angle1 + 2 * Math.PI / 3;
        double angle3 = angle2 + 2 * Math.PI / 3;

        Point2D p1 = Point2D.of(
                center.getX().add(Real.of(radius * Math.cos(angle1))),
                center.getY().add(Real.of(radius * Math.sin(angle1))));
        Point2D p2 = Point2D.of(
                center.getX().add(Real.of(radius * Math.cos(angle2))),
                center.getY().add(Real.of(radius * Math.sin(angle2))));
        Point2D p3 = Point2D.of(
                center.getX().add(Real.of(radius * Math.cos(angle3))),
                center.getY().add(Real.of(radius * Math.sin(angle3))));

        List<Point2D> snowflake = new ArrayList<>();
        snowflake.addAll(generate(p1, p2, depth));
        snowflake.addAll(generate(p2, p3, depth));
        snowflake.addAll(generate(p3, p1, depth));

        return snowflake;
    }

    /**
     * Calculates the Hausdorff dimension of the Koch curve.
     * 
     * @return log(4)/log(3) ≈ 1.2619
     */
    public static double hausdorffDimension() {
        return Math.log(4) / Math.log(3);
    }

    /**
     * Calculates the length of the Koch curve at a given depth.
     * 
     * @param initialLength length of the initial segment
     * @param depth         recursion depth
     * @return total length
     */
    public static double curveLength(double initialLength, int depth) {
        return initialLength * Math.pow(4.0 / 3.0, depth);
    }
}


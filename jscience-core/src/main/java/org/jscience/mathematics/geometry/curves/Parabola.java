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

package org.jscience.mathematics.geometry.curves;

import org.jscience.mathematics.geometry.Point2D;

/**
 * Represents a parabola in 2D space.
 * <p>
 * A parabola is defined by the equation: y = a(x-h)² + k
 * where (h,k) is the vertex and a determines the shape.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Parabola {

    private final double vertexX, vertexY;
    private final double a;
    private final double rotation;

    /**
     * Creates a parabola with vertex at (h, k) and coefficient a.
     */
    public Parabola(double vertexX, double vertexY, double a) {
        this(vertexX, vertexY, a, 0);
    }

    /**
     * Creates a rotated parabola.
     */
    public Parabola(double vertexX, double vertexY, double a, double rotation) {
        if (Math.abs(a) < 1e-15) {
            throw new IllegalArgumentException("Coefficient a must be non-zero");
        }
        this.vertexX = vertexX;
        this.vertexY = vertexY;
        this.a = a;
        this.rotation = rotation;
    }

    public double getVertexX() {
        return vertexX;
    }

    public double getVertexY() {
        return vertexY;
    }

    public double getCoefficient() {
        return a;
    }

    public double getRotation() {
        return rotation;
    }

    /**
     * Returns the vertex as a Point2D.
     */
    public Point2D getVertex() {
        return Point2D.of(vertexX, vertexY);
    }

    /**
     * Returns the focus of the parabola.
     */
    public Point2D getFocus() {
        double p = 1.0 / (4 * a);
        double fx = p * Math.sin(rotation);
        double fy = p * Math.cos(rotation);
        return Point2D.of(vertexX + fx, vertexY + fy);
    }

    /**
     * Returns the focal length p = 1/(4|a|).
     */
    public double getFocalLength() {
        return 1.0 / (4 * Math.abs(a));
    }

    /**
     * Returns the directrix y-coordinate (for non-rotated parabola).
     */
    public double getDirectrixY() {
        return vertexY - 1.0 / (4 * a);
    }

    /**
     * Evaluates y for a given x (non-rotated parabola).
     */
    public double evaluate(double x) {
        double dx = x - vertexX;
        return a * dx * dx + vertexY;
    }

    /**
     * Returns a point on the parabola at parameter t.
     */
    public Point2D pointAt(double t) {
        double localX = t;
        double localY = a * t * t;
        return transformPoint(localX, localY);
    }

    private Point2D transformPoint(double x, double y) {
        double cos = Math.cos(rotation);
        double sin = Math.sin(rotation);
        double rx = x * cos - y * sin;
        double ry = x * sin + y * cos;
        return Point2D.of(vertexX + rx, vertexY + ry);
    }

    /**
     * Returns the derivative dy/dx at point x.
     */
    public double derivative(double x) {
        return 2 * a * (x - vertexX);
    }

    /**
     * Checks if a point is on the parabola (within tolerance).
     */
    public boolean containsPoint(Point2D p, double tolerance) {
        double dx = p.getX().doubleValue() - vertexX;
        double dy = p.getY().doubleValue() - vertexY;

        double cos = Math.cos(-rotation);
        double sin = Math.sin(-rotation);
        double lx = dx * cos - dy * sin;
        double ly = dx * sin + dy * cos;

        double expectedY = a * lx * lx;
        return Math.abs(ly - expectedY) < tolerance;
    }

    /**
     * Creates a parabola from focus and directrix.
     */
    public static Parabola fromFocusAndDirectrix(double focusX, double focusY, double directrixY) {
        double vertexY = (focusY + directrixY) / 2;
        double p = focusY - vertexY;
        double a = 1.0 / (4 * p);
        return new Parabola(focusX, vertexY, a);
    }

    @Override
    public String toString() {
        return String.format("Parabola[vertex=(%.2f,%.2f), a=%.4f]", vertexX, vertexY, a);
    }
}

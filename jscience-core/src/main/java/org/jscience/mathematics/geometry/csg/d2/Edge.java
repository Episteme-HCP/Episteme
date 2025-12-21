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
package org.jscience.mathematics.geometry.csg.d2;

import org.jscience.mathematics.geometry.Point2D;

/**
 * Represents an edge (segment) in 2D CSG.
 * Analogous to Polygon in 3D CSG. Implements Graph.Edge for graph algorithms. * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 
 */
public class Edge implements org.jscience.mathematics.discrete.Graph.Edge<Point2D> {

    public final Point2D start;
    public final Point2D end;
    public final org.jscience.mathematics.geometry.Line2D line;

    public Edge(Point2D start, Point2D end) {
        this.start = start;
        this.end = end;
        org.jscience.mathematics.geometry.Vector2D dir = org.jscience.mathematics.geometry.Vector2D.of(
                end.getX().subtract(start.getX()),
                end.getY().subtract(start.getY()));
        this.line = new org.jscience.mathematics.geometry.Line2D(start, dir);
    }

    public Edge(Point2D start, Point2D end, org.jscience.mathematics.geometry.Line2D line) {
        this.start = start;
        this.end = end;
        this.line = line;
    }

    @Override
    public Point2D source() {
        return start;
    }

    @Override
    public Point2D target() {
        return end;
    }

    public Edge clone() {
        return new Edge(start, end, line);
    }

    public Edge flipped() {
        return new Edge(end, start, line.flipped());
    }

    @Override
    public String toString() {
        return "Edge[" + start + " -> " + end + "]";
    }
}

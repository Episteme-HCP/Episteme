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

package org.jscience.earth.loaders.gml;

import java.util.*;

/** LineString geometry.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GMLLineString extends GMLGeometry {
    private final List<double[]> points = new ArrayList<>();

    public void addPoint(double x, double y, double z) {
        points.add(new double[]{x, y, z});
    }
    
    public List<double[]> getPoints() { return Collections.unmodifiableList(points); }
    
    public int getPointCount() { return points.size(); }
    
    public double getLength() {
        double length = 0;
        for (int i = 1; i < points.size(); i++) {
            double[] p1 = points.get(i - 1);
            double[] p2 = points.get(i);
            double dx = p2[0] - p1[0];
            double dy = p2[1] - p1[1];
            length += Math.sqrt(dx*dx + dy*dy);
        }
        return length;
    }
    
    @Override
    public String getGeometryType() { return "LineString"; }
    
    @Override
    public double[] getBoundingBox() {
        if (points.isEmpty()) return null;
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE, maxY = -Double.MAX_VALUE;
        for (double[] p : points) {
            minX = Math.min(minX, p[0]);
            minY = Math.min(minY, p[1]);
            maxX = Math.max(maxX, p[0]);
            maxY = Math.max(maxY, p[1]);
        }
        return new double[]{minX, minY, maxX, maxY};
    }
    
    @Override
    public String toString() {
        return "LineString(" + points.size() + " points)";
    }
}

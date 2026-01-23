/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.earth.loaders.gml;

import java.util.*;

/** LineString geometry. */
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

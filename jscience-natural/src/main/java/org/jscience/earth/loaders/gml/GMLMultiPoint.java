/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.earth.loaders.gml;

import java.util.*;

/** Multi-point geometry. */
public class GMLMultiPoint extends GMLGeometry {
    private final List<GMLPoint> points = new ArrayList<>();

    public void addPoint(GMLPoint p) { if (p != null) points.add(p); }
    public List<GMLPoint> getPoints() { return Collections.unmodifiableList(points); }
    
    @Override
    public String getGeometryType() { return "MultiPoint"; }
    
    @Override
    public double[] getBoundingBox() {
        if (points.isEmpty()) return null;
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE, maxY = -Double.MAX_VALUE;
        for (GMLPoint p : points) {
            minX = Math.min(minX, p.getX());
            minY = Math.min(minY, p.getY());
            maxX = Math.max(maxX, p.getX());
            maxY = Math.max(maxY, p.getY());
        }
        return new double[]{minX, minY, maxX, maxY};
    }
}

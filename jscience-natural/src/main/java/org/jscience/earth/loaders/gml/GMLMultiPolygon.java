/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.earth.loaders.gml;

import java.util.*;

/** Multi-polygon geometry. */
public class GMLMultiPolygon extends GMLGeometry {
    private final List<GMLPolygon> polygons = new ArrayList<>();

    public void addPolygon(GMLPolygon p) { if (p != null) polygons.add(p); }
    public List<GMLPolygon> getPolygons() { return Collections.unmodifiableList(polygons); }
    
    public double getTotalArea() {
        return polygons.stream().mapToDouble(GMLPolygon::getArea).sum();
    }
    
    @Override
    public String getGeometryType() { return "MultiPolygon"; }
    
    @Override
    public double[] getBoundingBox() {
        if (polygons.isEmpty()) return null;
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE, maxY = -Double.MAX_VALUE;
        for (GMLPolygon p : polygons) {
            double[] bbox = p.getBoundingBox();
            if (bbox != null) {
                minX = Math.min(minX, bbox[0]);
                minY = Math.min(minY, bbox[1]);
                maxX = Math.max(maxX, bbox[2]);
                maxY = Math.max(maxY, bbox[3]);
            }
        }
        return new double[]{minX, minY, maxX, maxY};
    }
}

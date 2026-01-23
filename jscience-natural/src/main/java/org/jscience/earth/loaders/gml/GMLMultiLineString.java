/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.earth.loaders.gml;

import java.util.*;

/** Multi-linestring geometry. */
public class GMLMultiLineString extends GMLGeometry {
    private final List<GMLLineString> lineStrings = new ArrayList<>();

    public void addLineString(GMLLineString l) { if (l != null) lineStrings.add(l); }
    public List<GMLLineString> getLineStrings() { return Collections.unmodifiableList(lineStrings); }
    
    public double getTotalLength() {
        return lineStrings.stream().mapToDouble(GMLLineString::getLength).sum();
    }
    
    @Override
    public String getGeometryType() { return "MultiLineString"; }
    
    @Override
    public double[] getBoundingBox() {
        if (lineStrings.isEmpty()) return null;
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE, maxY = -Double.MAX_VALUE;
        for (GMLLineString l : lineStrings) {
            double[] bbox = l.getBoundingBox();
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

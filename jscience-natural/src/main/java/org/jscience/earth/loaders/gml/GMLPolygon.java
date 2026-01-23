/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.earth.loaders.gml;

import java.util.*;

/** Polygon geometry with exterior and interior rings. */
public class GMLPolygon extends GMLGeometry {
    private List<double[]> exteriorRing = new ArrayList<>();
    private final List<List<double[]>> interiorRings = new ArrayList<>();

    public List<double[]> getExteriorRing() { return exteriorRing; }
    public void setExteriorRing(List<double[]> ring) { this.exteriorRing = new ArrayList<>(ring); }
    
    public void addInteriorRing(List<double[]> ring) { interiorRings.add(new ArrayList<>(ring)); }
    public List<List<double[]>> getInteriorRings() { return Collections.unmodifiableList(interiorRings); }
    
    public int getHoleCount() { return interiorRings.size(); }
    
    /** Calculate approximate area using shoelace formula. */
    public double getArea() {
        return Math.abs(signedArea(exteriorRing)) - 
               interiorRings.stream().mapToDouble(r -> Math.abs(signedArea(r))).sum();
    }
    
    private double signedArea(List<double[]> ring) {
        if (ring.size() < 3) return 0;
        double area = 0;
        for (int i = 0; i < ring.size(); i++) {
            double[] p1 = ring.get(i);
            double[] p2 = ring.get((i + 1) % ring.size());
            area += p1[0] * p2[1] - p2[0] * p1[1];
        }
        return area / 2;
    }
    
    @Override
    public String getGeometryType() { return "Polygon"; }
    
    @Override
    public double[] getBoundingBox() {
        if (exteriorRing.isEmpty()) return null;
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE, maxY = -Double.MAX_VALUE;
        for (double[] p : exteriorRing) {
            minX = Math.min(minX, p[0]);
            minY = Math.min(minY, p[1]);
            maxX = Math.max(maxX, p[0]);
            maxY = Math.max(maxY, p[1]);
        }
        return new double[]{minX, minY, maxX, maxY};
    }
    
    @Override
    public String toString() {
        return "Polygon(" + exteriorRing.size() + " points, " + interiorRings.size() + " holes)";
    }
}

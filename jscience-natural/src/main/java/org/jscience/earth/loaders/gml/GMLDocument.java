/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.earth.loaders.gml;

import java.util.*;

/** GML document containing features. */
public class GMLDocument {
    private String srsName;
    private final List<GMLFeature> features = new ArrayList<>();

    public String getSrsName() { return srsName; }
    public void setSrsName(String srs) { this.srsName = srs; }
    
    public void addFeature(GMLFeature f) { if (f != null) features.add(f); }
    public List<GMLFeature> getFeatures() { return Collections.unmodifiableList(features); }
    
    public GMLFeature getFeatureById(String id) {
        for (GMLFeature f : features) {
            if (id.equals(f.getId())) return f;
        }
        return null;
    }
    
    /** Get bounding box of all features. */
    public double[] getBoundingBox() {
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE, maxY = -Double.MAX_VALUE;
        
        for (GMLFeature f : features) {
            if (f.getGeometry() != null) {
                double[] bbox = f.getGeometry().getBoundingBox();
                if (bbox != null && bbox.length >= 4) {
                    minX = Math.min(minX, bbox[0]);
                    minY = Math.min(minY, bbox[1]);
                    maxX = Math.max(maxX, bbox[2]);
                    maxY = Math.max(maxY, bbox[3]);
                }
            }
        }
        
        return new double[]{minX, minY, maxX, maxY};
    }
    
    @Override
    public String toString() {
        return "GMLDocument{features=" + features.size() + ", srs=" + srsName + "}";
    }
}

/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.social.geography.loaders.gml;

import java.util.ArrayList;
import java.util.List;

/**
 * GML Polygon DTO.
 */
public class GMLPolygon extends GMLGeometry {
    private List<double[]> exteriorRing;
    private final List<List<double[]>> interiorRings = new ArrayList<>();

    public List<double[]> getExteriorRing() {
        return exteriorRing;
    }

    public void setExteriorRing(List<double[]> exteriorRing) {
        this.exteriorRing = exteriorRing;
    }

    public void addInteriorRing(List<double[]> ring) {
        interiorRings.add(ring);
    }

    public List<List<double[]>> getInteriorRings() {
        return interiorRings;
    }
}


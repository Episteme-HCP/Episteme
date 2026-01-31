/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.social.geography.loaders.gml;

import java.util.ArrayList;
import java.util.List;

/**
 * GML MultiPolygon DTO.
 */
public class GMLMultiPolygon extends GMLGeometry {
    private final List<GMLPolygon> polygons = new ArrayList<>();

    public void addPolygon(GMLPolygon poly) {
        polygons.add(poly);
    }

    public List<GMLPolygon> getPolygons() {
        return polygons;
    }
}


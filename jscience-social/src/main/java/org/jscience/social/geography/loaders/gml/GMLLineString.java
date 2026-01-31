/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.social.geography.loaders.gml;

import java.util.ArrayList;
import java.util.List;

/**
 * GML LineString DTO.
 */
public class GMLLineString extends GMLGeometry {
    private final List<double[]> points = new ArrayList<>();

    public void addPoint(double x, double y, double z) {
        points.add(new double[]{x, y, z});
    }

    public List<double[]> getPoints() {
        return points;
    }
}


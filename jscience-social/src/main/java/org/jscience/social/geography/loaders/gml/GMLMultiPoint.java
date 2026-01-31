/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.social.geography.loaders.gml;

import java.util.ArrayList;
import java.util.List;

/**
 * GML MultiPoint DTO.
 */
public class GMLMultiPoint extends GMLGeometry {
    private final List<GMLPoint> points = new ArrayList<>();

    public void addPoint(GMLPoint pt) {
        points.add(pt);
    }

    public List<GMLPoint> getPoints() {
        return points;
    }
}


/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.geography.loaders.gml;

import java.util.ArrayList;
import java.util.List;

/**
 * GML MultiLineString DTO.
 */
public class GMLMultiLineString extends GMLGeometry {
    private final List<GMLLineString> lines = new ArrayList<>();

    public void addLineString(GMLLineString line) {
        lines.add(line);
    }

    public List<GMLLineString> getLineStrings() {
        return lines;
    }
}

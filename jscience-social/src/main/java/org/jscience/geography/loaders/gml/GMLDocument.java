/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.geography.loaders.gml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a parsed GML (Geography Markup Language) document.
 * <p>
 * This DTO (Data Transfer Object) stores the hierarchical structure
 * of GML features and their associated geometries.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 * @see GMLReader
 */
public class GMLDocument {
    private String srsName;
    private final List<GMLFeature> features = new ArrayList<>();

    public GMLDocument() {
    }

    public String getSrsName() {
        return srsName;
    }

    public void setSrsName(String srsName) {
        this.srsName = srsName;
    }

    public void addFeature(GMLFeature feature) {
        if (feature != null) {
            features.add(feature);
        }
    }

    public List<GMLFeature> getFeatures() {
        return Collections.unmodifiableList(features);
    }
}

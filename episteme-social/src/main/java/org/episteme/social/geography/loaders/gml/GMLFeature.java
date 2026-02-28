/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.social.geography.loaders.gml;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a geographic feature in a GML document.
 */
public class GMLFeature {
    private String id;
    private String typeName;
    private GMLGeometry geometry;
    private final Map<String, String> properties = new HashMap<>();

    public GMLFeature() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public GMLGeometry getGeometry() {
        return geometry;
    }

    public void setGeometry(GMLGeometry geometry) {
        this.geometry = geometry;
    }

    public void setProperty(String key, String value) {
        properties.put(key, value);
    }

    public String getProperty(String key) {
        return properties.get(key);
    }

    public Map<String, String> getProperties() {
        return properties;
    }
}

